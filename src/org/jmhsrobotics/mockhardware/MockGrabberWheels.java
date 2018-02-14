package org.jmhsrobotics.mockhardware;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.hardwareinterface.GrabberWheels;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class MockGrabberWheels implements Module, GrabberWheels
{
	@Override
	public void setLeftWheels(double speed)
	{
		System.out.println("Spinning left wheels at " + speed);
	}

	@Override
	public void setRightWheels(double speed)
	{
		System.out.println("Spinning right wheels at " + speed);
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
