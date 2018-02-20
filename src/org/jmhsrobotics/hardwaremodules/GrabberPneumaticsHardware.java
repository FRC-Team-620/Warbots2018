package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.modulesystem.annotations.HardwareModule;
import org.jmhsrobotics.hardwareinterface.GrabberPneumatics;
import org.jmhsrobotics.hardwareinterface.Pneumatics;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

@HardwareModule
public class GrabberPneumaticsHardware implements Module, GrabberPneumatics
{
	private @Submodule Pneumatics pneumatics;
	
	Solenoid leftWrist, rightWrist, leftArm, rightArm, raise;
	
	public GrabberPneumaticsHardware(int leftArmPort, int leftWristPort, int rightArmPort, int rightWristPort, int raisePort)
	{
		leftArm = new Solenoid(pneumatics.getPort(), leftArmPort);
		leftWrist = new Solenoid(pneumatics.getPort(), leftWristPort);
		rightArm = new Solenoid(pneumatics.getPort(), rightArmPort);
		rightWrist = new Solenoid(pneumatics.getPort(), rightWristPort);
		raise = new Solenoid(pneumatics.getPort(), raisePort);
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