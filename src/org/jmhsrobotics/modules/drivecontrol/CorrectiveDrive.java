package org.jmhsrobotics.modules.drivecontrol;

import java.util.Optional;

import org.jmhsrobotics.core.modulesystem.DriveController;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.DummyPIDOutput;
import org.jmhsrobotics.core.util.PIDCalculator;
import org.jmhsrobotics.core.util.SensorSource;

import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CorrectiveDrive extends DriveController
{
	private @Submodule Localization localization;
	
	private boolean enabled;
	
	private double topSpeed;
	private double topTurnRate;
	
	private Optional<Angle> lockAngle;
	
	private SensorSource posSource;
	private SensorSource angleSource;
	
	private DummyPIDOutput speedOutput;
	private DummyPIDOutput turnOutput;
	
	private PIDCalculator angleLockPID;
	private PIDCalculator turnRatePID;
	private PIDCalculator posRatePID;
	
	@Override
	public void onLink()
	{
		posSource = SensorSource.fromDispAndRate(this::getOffsetInLockedDir, this::getSpeedInLockedDir);
		angleSource = SensorSource.fromDispAndRate(localization::getAngleDegreesUnsigned, localization::getRotationRate);
		
		speedOutput = new DummyPIDOutput();
		turnOutput = new DummyPIDOutput();
		
		angleLockPID = new PIDCalculator(0.04, 0, 5, angleSource, turnOutput);
		angleLockPID.setInputRange(0, 360);
		angleLockPID.setContinuous();
		angleLockPID.setOutputRange(-1, 1);
		SmartDashboard.putData("Angle Lock PID", angleLockPID);
		
		turnRatePID = new PIDCalculator(0, 0, 0, 0, angleSource, turnOutput);
		SmartDashboard.putData("Turn Rate PID", turnRatePID);
		posRatePID = new PIDCalculator(0, 0, 0, 0, posSource, speedOutput);
		SmartDashboard.putData("Speed PID", posRatePID);
	}
	
	@Override
	public void enable()
	{
		if(enabled)
			return;
		
		localization.enable();
		enabled = true;
		lockAngle();
		posRatePID.enable();
	}
	
	@Override
	public void disable()
	{
		if(!enabled)
			return;
		
		localization.disable();
		enabled = false;
		posRatePID.disable();
		turnRatePID.disable();
	}
	
	private double getOffsetInLockedDir()
	{
		return localization.getPos(lockAngle.get());
	}
	
	private double getSpeedInLockedDir()
	{
		return localization.getSpeed(lockAngle.get());
	}
	
	@Override
	public void drive(double speed, double turn)
	{	
		if(!enabled)
			throw new RuntimeException("Attempted to drive without enabling corrective drive");
		
		if(turn == 0)
		{
			System.out.println("Zero turn, " + lockAngle + " " + turnOutput.get());
			
			if(lockAngle.isPresent())
				driveRaw(speed, turnOutput.get());
			else
			{
				lockAngle();
				driveRaw(speed, 0);
			}
		}
		else
		{
			if(lockAngle.isPresent())
			{
				useTurnRate();
				driveRaw(speed, turn);
			}
			else
			{
				turnRatePID.setSetpoint(angularVelocityOf(turn));
				driveRaw(speed, turnOutput.get());
			}
		}
	}
	
	private void lockAngle()
	{
		turnRatePID.disable();
		angleSource.setPIDSourceType(PIDSourceType.kDisplacement);
		lockAngle = Optional.of(localization.getAngle());
		angleLockPID.setRelativeSetpoint(0);
		angleLockPID.enable();
	}
	
	private void useTurnRate()
	{
		angleLockPID.disable();
		angleSource.setPIDSourceType(PIDSourceType.kRate);
		lockAngle = Optional.empty();
		turnRatePID.enable();
	}
	
	private double angularVelocityOf(double turnRate)
	{
		return turnRate * topTurnRate;
	}
	
	@SuppressWarnings("unused")
	private double velocityOf(double speed)
	{
		return speed * topSpeed;
	}
	
	@Override
	public Command getDriveCommand(double dist)
	{
		return new DriveDistance(this, localization, dist);
	}
	
	@Override
	public Command getTurnCommand(Angle angle)
	{
		return new TurnAngle(this, localization, angle);
	}
	
	@Override
	public Optional<Angle> getLockAngle()
	{
		return lockAngle;
	}
}
