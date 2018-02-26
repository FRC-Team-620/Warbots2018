package org.jmhsrobotics.mockhardware;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.hardwareinterface.DriveMechanism;
import org.jmhsrobotics.hardwareinterface.Gyro;
import org.jmhsrobotics.hardwareinterface.TurnTable;

import edu.wpi.first.wpilibj.command.Command;

public class RobotAsTurnTable implements Module, TurnTable
{
	private @Submodule Gyro gyro;
	private @Submodule DriveMechanism drive;

	private double turnSpeed;
	
	@Override
	public void onLink()
	{
		gyro.reset();
		turnSpeed = 0;
	}
	
	@Override
	public void drive(double speed)
	{
		Angle angle = gyro.getAngle();
		boolean leftLock = angle.compareTo(Angle.INVERSE_RIGHT) <= 0;
		boolean rightLock = angle.compareTo(Angle.RIGHT) >= 0;
		
		if(speed > 0)
			leftLock = false;
		else if(speed < 0)
			rightLock = false;
		
		boolean lock = leftLock || rightLock;
		
		if(lock)
			drive.drive(0, 0);
		else
			drive.drive(0, speed);
		
		turnSpeed = speed;
	}

	@Override
	public double getSpeed()
	{
		return turnSpeed;
	}

	@Override
	public boolean isInCenter()
	{
		return gyro.getAngle().absoluteValue().compareTo(Angle.fromDegrees(15)) <= 0;
	}

	@Override
	public Command getTest()
	{
		return null;
	}
}