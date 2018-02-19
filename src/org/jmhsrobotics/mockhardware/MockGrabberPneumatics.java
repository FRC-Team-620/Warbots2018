package org.jmhsrobotics.mockhardware;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.hardwareinterface.GrabberPneumatics;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class MockGrabberPneumatics implements Module, GrabberPneumatics
{
	@Override
	public void setLeftWristExtended(boolean val)
	{
		System.out.println("Setting left wrist solenoid to " + (val ? "extended" : "retracted"));
	}

	@Override
	public void setRightWristExtended(boolean val)
	{
		System.out.println("Setting right wrist solenoid to " + (val ? "extended" : "retracted"));
	}

	@Override
	public void setLeftArmExtended(boolean val)
	{
		System.out.println("Setting left arm solenoid to " + (val ? "extended" : "retracted"));
	}

	@Override
	public void setRightArmExtended(boolean val)
	{
		System.out.println("Setting right arm solenoid to " + (val ? "extended" : "retracted"));
	}

	@Override
	public void setRaised(boolean val)
	{
		System.out.println((val ? "Rais" : "lower") + "ing grabber");
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
