package org.jmhsrobotics.modules;

import java.util.Optional;

import org.jmhsrobotics.core.modules.SubsystemManager;
import org.jmhsrobotics.core.modulesystem.ControlSchemeModule;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.hardwareinterface.Drive;

import edu.wpi.first.wpilibj.Joystick;

public class DriveWithJoystick extends ControlSchemeModule
{
	private @Submodule Optional<SubsystemManager> subsystems;
	private @Submodule Drive drive;
	private @Submodule Optional<TurnAngle> turnFactory;

	@Override
	public void onLink()
	{
		subsystems.ifPresent(s -> requires(s.getSubsystem("DriveTrain")));
	}

	@Override
	public void execute()
	{
		Joystick js = oi.getMainDriverJoystick();
		double speed = -js.getY();
		double turn = js.getX();

		if (Math.abs(turn) < 0.2) turn = 0;
		if (Math.abs(speed) < 0.1) speed = 0;

		
		if(js.getRawButton(4))
			turnFactory.ifPresent(tf -> tf.newInstance(Angle.INVERSE_RIGHT).start());
		if(js.getRawButton(5))
			turnFactory.ifPresent(tf -> tf.newInstance(Angle.RIGHT).start());
	}
}
