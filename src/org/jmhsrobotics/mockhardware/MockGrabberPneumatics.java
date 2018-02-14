package org.jmhsrobotics.mockhardware;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.hardwareinterface.GrabberPneumatics;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class MockGrabberPneumatics implements Module, GrabberPneumatics
{
	@Override
	public void setLateralLeftPistonExtended(boolean val)
	{
		System.out.println("Setting lateral left solenoid to " + (val ? "extended" : "retracted"));
	}

	@Override
	public void setLateralRightEncoderExtended(boolean val)
	{
		System.out.println("Setting lateral right solenoid to " + (val ? "extended" : "retracted"));
	}

	@Override
	public void setVerticalLeftPistonExtended(boolean val)
	{
		System.out.println("Setting vertical left solenoid to " + (val ? "extended" : "retracted"));
	}

	@Override
	public void setVerticalRightEncoderExtended(boolean val)
	{
		System.out.println("Setting vercial right solenoid to " + (val ? "extended" : "retracted"));
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
