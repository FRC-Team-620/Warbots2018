package org.jmhsrobotics.modules.drivecontrol;

import java.util.Optional;

import org.jmhsrobotics.core.modulesystem.DriveController;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.DummyPIDOutput;
import org.jmhsrobotics.core.util.DummyPIDSource;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CorrectiveDrive extends DriveController
{
	private @Submodule Localization localization;
	
	private double topSpeed;
	private double topTurnRate;
	
	private Optional<Angle> lockAngle;
	
	private DummyPIDSource posSource;
	private DummyPIDSource angleSource;
	
	private DummyPIDOutput speedOutput;
	private DummyPIDOutput turnOutput;
	
	private PIDController angleLockPID;
	private PIDController turnRatePID;
	private PIDController posRatePID;
	
	@Override
	public void onLink()
	{
		posSource = DummyPIDSource.fromDispAndRate(this::getOffsetInLockedDir, this::getSpeedInLockedDir);
		angleSource = DummyPIDSource.fromDispAndRate(localization::getAngleDegreesUnsigned, localization::getRotationRate);
		
		speedOutput = new DummyPIDOutput();
		turnOutput = new DummyPIDOutput();
		
		angleLockPID = new PIDController(0, 0, 0, angleSource, turnOutput);
		SmartDashboard.putData("Angle Lock PID", angleLockPID);
		
		turnRatePID = new PIDController(0, 0, 0, 0, angleSource, turnOutput);
		SmartDashboard.putData("Turn Rate PID", turnRatePID);
		posRatePID = new PIDController(0, 0, 0, 0, posSource, speedOutput);
		SmartDashboard.putData("Speed PID", posRatePID);
	}
	
	public void enable()
	{
		lockAngle();
		posRatePID.enable();
	}
	
	public void disable()
	{
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
		if(turn == 0)
		{
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
		Angle ang = localization.getAngle();
		lockAngle = Optional.of(ang);
		angleLockPID.setSetpoint(ang.measureDegreesUnsigned());
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
