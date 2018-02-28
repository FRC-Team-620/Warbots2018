package org.jmhsrobotics.mockhardware;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.hardwareinterface.GrabberPneumatics;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class MockGrabberPneumatics implements Module, GrabberPneumatics
{
	private boolean lw, rw, la, ra, raise;
	
	@Override
	public void setLeftWristContracted(boolean val)
	{
		if(val != lw)
			System.out.println("Setting left wrist solenoid to " + (val ? "extended" : "retracted"));
		lw = val;
	}

	@Override
	public void setRightWristContracted(boolean val)
	{
		if(val != rw)
			System.out.println("Setting right wrist solenoid to " + (val ? "extended" : "retracted"));
		rw = val;
	}

	@Override
	public void setLeftArmContracted(boolean val)
	{
		if(val != la)
			System.out.println("Setting left arm solenoid to " + (val ? "extended" : "retracted"));
		la = val;
	}

	@Override
	public void setRightArmContracted(boolean val)
	{
		if(val != ra)
			System.out.println("Setting right arm solenoid to " + (val ? "extended" : "retracted"));
		ra = val;
	}

	@Override
	public void setRaised(boolean val)
	{
		if(val != raise)
			System.out.println((val ? "Rais" : "lower") + "ing grabber");
		raise = val;
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
