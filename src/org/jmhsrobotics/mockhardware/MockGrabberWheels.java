package org.jmhsrobotics.mockhardware;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.util.RobotMath;
import org.jmhsrobotics.hardwareinterface.GrabberWheels;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class MockGrabberWheels implements Module, GrabberWheels
{
	private double speed, jank;
	
	@Override
	public void set(double speed, double jank)
	{
		if(RobotMath.oneNonZero(this.speed - speed, this.jank - jank))
			System.out.println("Setting grabber wheel speed to " + speed + " and jank to " + jank);
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

	@Override
	public boolean hasPrism()
	{
		return false;
	}
}
