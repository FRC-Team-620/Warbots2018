package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.modulesystem.ControlSchemeModule;
import org.jmhsrobotics.core.modulesystem.DriveController;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.RobotMath;
import org.jmhsrobotics.hardwaremodules.TurntableHardware;

import edu.wpi.first.wpilibj.Joystick;

public class DriveWithJoystick extends ControlSchemeModule
{
	private @Submodule DriveController drive;
	private @Submodule TurntableHardware motor;
	
	@Override
	public void execute()
	{
		Joystick js = getOI().getMainDriverJoystick();
		double y = -js.getY();
		double x = js.getX();

		double xadjusted = RobotMath.xKinkedMap(x, -1, 1, 0, -.2, .2, -1, 1);
		double yadjusted = RobotMath.xKinkedMap(y, -1, 1, 0, -.2, .2, -1, 1);
//		String xadjout = String.format("%.2g%n", xadjusted);
//		String yadjout = String.format("%.2g%n", yadjusted);
//		String xout = String.format("%.2g%n", x);
//		String yout = String.format("%.2g%n", y);
		
		if(js.getTriggerPressed())
			drive.setTarget(0, 0);
		
		if(js.getRawButton(4))
			motor.driveTurnTableMotor(.4);
		else if(js.getRawButton(5))
			motor.driveTurnTableMotor(-.4);
		else
			motor.driveTurnTableMotor(0);
		
		drive.drive(yadjusted, xadjusted);
	}
}
