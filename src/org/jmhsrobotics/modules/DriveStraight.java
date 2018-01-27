package org.jmhsrobotics.modules;

import org.jmhsrobotics.hardwareinterface.Drive;
import org.jmhsrobotics.hardwareinterface.Gyro;
import org.jmhsrobotics.modulesystem.Module;
import org.jmhsrobotics.modulesystem.Submodule;
import org.jmhsrobotics.modulesystem.annotations.FunctionModule;
import org.jmhsrobotics.util.DummyPIDOutput;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;

@FunctionModule
public class DriveStraight implements Module
{
	private @Submodule Drive drive;
	private @Submodule Gyro gyro;
	private PIDController pid;
	private DummyPIDOutput dummy;

	@Override
	public void onLink()
	{
		dummy = new DummyPIDOutput();
		pid = new PIDController(0.1, 0, 0.05, 0.05, gyro.getAnglePIDSource(), dummy);
		pid.setContinuous();
		pid.setPercentTolerance(0);
		pid.setInputRange(0, 360);
		pid.setOutputRange(-1, 1);
	}

	public void driveStraight(double speed)
	{
		drive.drive(speed, dummy.get());
	}
	
	public void lock()
	{
		pid.setSetpoint(gyro.getAngle().measureDegreesUnsigned());
		pid.enable();
	}
	
	public void release()
	{
		pid.disable();
	}

	public boolean isLocked()
	{
		return pid.isEnabled();
	}
	
	@Override
	public Command getTest()
	{
		return new Command()
		{
			@Override
			protected void execute()
			{
				DriveStraight.this.driveStraight(0.7);
			}
			
			@Override
			protected boolean isFinished()
			{
				return timeSinceInitialized() > 5;
			}
		};
	}
}
