package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.modulesystem.annotations.HardwareModule;
import org.jmhsrobotics.hardwareinterface.GrabberPneumatics;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

@HardwareModule
public class GrabberPneumaticsHardware implements Module, GrabberPneumatics
{
	Solenoid leftWrist, rightWrist, leftArm, rightArm, raise;
	
	public GrabberPneumaticsHardware(int leftArmPort, int leftWristPort, int rightArmPort, int rightWristPort, int raisePort)
	{
		leftArm = new Solenoid(leftArmPort);
		leftWrist = new Solenoid(leftWristPort);
		rightArm = new Solenoid(rightArmPort);
		rightWrist = new Solenoid(rightWristPort);
		raise = new Solenoid(raisePort);
	}
	
	@Override
	public void setLeftWristExtended(boolean val)
	{
		leftWrist.set(val);
	}

	@Override
	public void setRightWristExtended(boolean val)
	{
		rightWrist.set(val);
	}

	@Override
	public void setLeftArmExtended(boolean val)
	{
		leftArm.set(val);
	}

	@Override
	public void setRightArmExtended(boolean val)
	{
		rightArm.set(val);
	}
	
	@Override
	public void setRaised(boolean val)
	{
		raise.set(val);
	}

	@Override
	public Command getTest()
	{
		return new InstantCommand()
		{

		};
	}
}