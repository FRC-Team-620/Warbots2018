package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.modulesystem.ControlSchemeModule;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.GrabberPneumatics;
import org.jmhsrobotics.hardwareinterface.GrabberWheels;

import edu.wpi.first.wpilibj.Joystick;

public class DriveWithXbox extends ControlSchemeModule
{
	private @Submodule GrabberPneumatics grabber;
	private @Submodule GrabberWheels wheels;
	private @Submodule ElevatorControlCommand elevator;
	
	@Override
	public void execute()
	{
		Joystick js = getOI().getMainDriverJoystick();
		grabber.setLeftArmExtended(js.getRawButton(7));
		grabber.setLeftWristExtended(js.getRawButton(6));
		grabber.setRightArmExtended(js.getRawButton(10));
		grabber.setRightWristExtended(js.getRawButton(11));
		grabber.setRaised(js.getRawButton(8));
		
		if(js.getRawButton(4))
			wheels.setLeftWheels(js.getY());
		else
			wheels.setLeftWheels(0);
		
		if(js.getRawButton(5))
			wheels.setRightWheels(js.getY());
		else
			wheels.setRightWheels(0);
		
		if(js.getRawButton(3))
			elevator.driveManual(js.getY(), false);
		else
			elevator.driveManual(0, false);
	}
}
