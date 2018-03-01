package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.modulesystem.ControlScheme;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.RobotMath;
import org.jmhsrobotics.hardwareinterface.ElevatorController;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class TestLinearActuator extends ControlScheme
{
	private @Submodule ElevatorController elevator;
	
	private ElevatorControlCommand.Position setpoint;
	private boolean pneumatics;
	
	@Override
	protected void execute()
	{
		XboxController xbox = getOI().getXboxControllers().get(0);
		
		double axis = -xbox.getY(Hand.kRight);
		axis = RobotMath.xKinkedMap(axis, -1, 1, 0, -.2, .2, -1, 1);
		
		if(axis != 0)
			setpoint = null;
		
		if(xbox.getYButtonPressed())
			if(setpoint == null)
				setpoint = ElevatorControlCommand.Position.ground;
			else
				setpoint = setpoint.getAdjacentAbove();
		
		if(xbox.getAButtonPressed())
			if(setpoint == null)
				setpoint = ElevatorControlCommand.Position.ground;
			else
				setpoint = setpoint.getAdjacentBelow();
		
		if(xbox.getBButtonPressed())
			pneumatics = !pneumatics;
		
		if(setpoint == null)
			elevator.driveManual(axis, pneumatics);
		else
			elevator.goTo(setpoint);
	}
}
