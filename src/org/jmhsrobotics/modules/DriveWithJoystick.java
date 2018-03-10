package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.modulesystem.ControlScheme;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.DriveController;

import edu.wpi.first.wpilibj.Joystick;

public class DriveWithJoystick extends ControlScheme
{
	private @Submodule DriveController drive;
	
	private Joystick js;
	
	public DriveWithJoystick(Joystick js)
	{
		this.js = js;
	}
	
	@Override
	public void execute()
	{
		double y = -deadZone(js.getY(), .2, .1);
		double x = deadZone(js.getX(), .2, .1);
		drive.drive(y, x);
	}
}
