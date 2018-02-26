package org.jmhsrobotics.mockhardware;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.hardwareinterface.GrabberWheels;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class MockGrabberWheels implements Module, GrabberWheels
{
	private double leftSpeed, rightSpeed;
	
	@Override
	public void setLeftWheels(double speed)
	{
		if(speed != leftSpeed)
			System.out.println("Spinning left wheels at " + speed);
		leftSpeed = speed;
	}

	@Override
	public void setRightWheels(double speed)
	{
		if(speed != rightSpeed)
			System.out.println("Spinning right wheels at " + speed);
		rightSpeed = speed;
	}
	
	@Override
	public Command getTest()
	{
		return new InstantCommand()
		{
			@Override
			protected void initialize()
			{
				System.out.println("Testing mock grabber pneumatics");
			}
		};
	}
}
