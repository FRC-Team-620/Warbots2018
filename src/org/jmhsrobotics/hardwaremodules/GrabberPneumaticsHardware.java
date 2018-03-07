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
	
	public GrabberPneumaticsHardware(int canId, int leftArmPort, int leftWristPort, int rightArmPort, int rightWristPort, int raisePort)
	{
		leftArm = new Solenoid(canId, leftArmPort);
		leftWrist = new Solenoid(canId, leftWristPort);
		rightArm = new Solenoid(canId, rightArmPort);
		rightWrist = new Solenoid(canId, rightWristPort);
		raise = new Solenoid(canId, raisePort);
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
		raise.set(!val);
	}

	@Override
	public Command getTest()
	{
		return new InstantCommand()
		{

		};
	}
}