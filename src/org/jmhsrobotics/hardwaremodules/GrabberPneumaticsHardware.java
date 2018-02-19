package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.hardwareinterface.GrabberPneumatics;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class GrabberPneumaticsHardware implements Module, GrabberPneumatics
{
	Solenoid latleft, latright, vertleft, vertright;
	
	@Override
	public void onLink()
	{
		latleft = new Solenoid(1);
		latright = new Solenoid(2);
		vertleft = new Solenoid(3);
		vertright = new Solenoid(4);
	}

	@Override
	public void setLeftWristExtended(boolean val)
	{
		latleft.set(val);
	}

	@Override
	public void setRightWristExtended(boolean val)
	{
		latright.set(val);
	}

	@Override
	public void setLeftArmExtended(boolean val)
	{
		vertleft.set(val);
	}

	@Override
	public void setRightArmExtended(boolean val)
	{
		vertright.set(val);
	}

	@Override
	public Command getTest()
	{
		return new InstantCommand()
		{

		};
	}
}