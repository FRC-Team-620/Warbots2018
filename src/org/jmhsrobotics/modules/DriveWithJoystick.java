package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.modulesystem.ControlScheme;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.RobotMath;
import org.jmhsrobotics.hardwareinterface.DriveController;

import edu.wpi.first.wpilibj.Joystick;

public class DriveWithJoystick extends ControlScheme
{
	private @Submodule DriveController drive;
//	private @Submodule TurnTableController turnTableDrive;
	
	@Override
	public void execute()
	{
		Joystick js = getOI().getMainDriverJoystick();
		
		double y = -js.getY();
		double x = js.getX();
		double xadjusted = RobotMath.xKinkedMap(x, -1, 1, 0, -.2, .2, -1, 1);
		double yadjusted = RobotMath.xKinkedMap(y, -1, 1, 0, -.2, .2, -1, 1);
		drive.drive(yadjusted, xadjusted);
		
//		String xadjout = String.format("%.2g%n", xadjusted);
//		String yadjout = String.format("%.2g%n", yadjusted);
//		String xout = String.format("%.2g%n", x);
//		String yout = String.format("%.2g%n", y);
		
//		if(js.getTriggerPressed())
//			turnTableDrive.calibrate();
//		
//		if(js.getRawButtonPressed(4))
//			turnTableDrive.goTo(turnTableDrive.getCurrentPosition().getLeftAdjacent());
//		
//		if(js.getRawButtonPressed(3))
//			turnTableDrive.goTo(turnTableDrive.getCurrentPosition().getRightAdjacent());
	}
}
