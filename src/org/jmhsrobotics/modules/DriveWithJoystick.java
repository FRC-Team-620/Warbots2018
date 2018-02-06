package org.jmhsrobotics.modules;

import java.util.Optional;

import org.jmhsrobotics.core.modules.SubsystemManager;
import org.jmhsrobotics.core.modulesystem.ControlSchemeModule;
import org.jmhsrobotics.core.modulesystem.DriveController;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.RobotMath;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class DriveWithJoystick extends ControlSchemeModule
{
	private @Submodule Optional<SubsystemManager> subsystems;
	private @Submodule DriveController drive;

	@Override
	public void onLink()
	{
		subsystems.ifPresent(s -> requires(s.getSubsystem("DriveTrain")));
	}

	@Override
	public void execute()
	{
		Joystick js = getOI().getMainDriverJoystick();
		double y = -js.getY();
		double x = js.getX();

		double xadjusted = RobotMath.xKinkedMap(x, -1, 1, 0, -.2, .2, -1, 1);
		double yadjusted = RobotMath.xKinkedMap(y, -1, 1, 0, -.2, .2, -1, 1);
		String xadjout = String.format("%.2g%n", xadjusted);
		String yadjout = String.format("%.2g%n", yadjusted);
		String xout = String.format("%.2g%n", x);
		String yout = String.format("%.2g%n", y);
		System.out.print("X adjusted: " + xadjout + " Y adjusted: " + yadjout + " X: " + xout + " Y: " + yout);
		drive.drive(yadjusted, xadjusted);
	}
}
