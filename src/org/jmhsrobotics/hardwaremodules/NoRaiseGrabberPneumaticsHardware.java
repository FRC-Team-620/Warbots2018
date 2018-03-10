package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.hardwareinterface.GrabberPneumatics;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class NoRaiseGrabberPneumaticsHardware implements Module, GrabberPneumatics
{
	private Solenoid leftWrist, rightWrist, leftArm, rightArm;
	private boolean raise;
	
	public NoRaiseGrabberPneumaticsHardware(int canId, int leftArmPort, int leftWristPort, int rightArmPort, int rightWristPort)
	{
		leftArm = new Solenoid(canId, leftArmPort);
		leftWrist = new Solenoid(canId, leftWristPort);
		rightArm = new Solenoid(canId, rightArmPort);
		rightWrist = new Solenoid(canId, rightWristPort);
	}
	
	@Override
	public void setLeftWristContracted(boolean val)
	{
		leftWrist.set(val);
	}

	@Override
	public void setRightWristContracted(boolean val)
	{
		rightWrist.set(val);
	}

	@Override
	public void setLeftArmContracted(boolean val)
	{
		leftArm.set(val);
	}

	@Override
	public void setRightArmContracted(boolean val)
	{
		rightArm.set(val);
	}
	
	@Override
	public void setRaised(boolean val)
	{
		if(raise != val)
		{
			System.out.println((val ? "Rais" : "Lower") + "ing Grabber");
		}
		
		raise = val;
	}

	@Override
	public Command getTest()
	{
		return new InstantCommand()
		{

		};
	}
}
