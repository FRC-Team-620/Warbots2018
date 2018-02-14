package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.modulesystem.ControlSchemeModule;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.RobotMath;
import org.jmhsrobotics.hardwareinterface.Grabber;
import org.jmhsrobotics.hardwareinterface.Grabber.Position;
import org.jmhsrobotics.hardwareinterface.HybridLifter;
import org.jmhsrobotics.hardwareinterface.TurnTable;

import edu.wpi.first.wpilibj.Joystick;

public class DriveClawMechWithTwoJoysticks extends ControlSchemeModule
{
	private final static double THRESHOLD_OPEN = 0.7, THRESHOLD_CLOSE = 0.7;
	
	private @Submodule TurnTable turnTable;
	private @Submodule HybridLifter elevator;
	private @Submodule Grabber claw;
	
	@Override
	protected void execute()
	{
		Joystick left = getOI().getJoysticks().get(1);
		Joystick right = getOI().getJoysticks().get(0);
		
		if(left.getTrigger())
		{
			double x = left.getX();
			if(x < -THRESHOLD_OPEN)
				claw.setLeftArm(Position.extended);
			else if(x > THRESHOLD_CLOSE)
				claw.setLeftArm(Position.contracted);
			else
				claw.setLeftArm(Position.raised);
		}
		
		if(right.getTrigger())
		{
			double x = right.getX();
			if(x > THRESHOLD_OPEN)
				claw.setRightArm(Position.extended);
			else if(x < -THRESHOLD_CLOSE)
				claw.setRightArm(Position.contracted);
			else
				claw.setRightArm(Position.raised);
		}
		
		claw.spinLeftWheels(RobotMath.xKinkedMap(left.getY(), -1, 1, 0, -.2, .2, -1, 1));
		claw.spinRightWheels(RobotMath.xKinkedMap(right.getY(), -1, 1, 0, -.2, .2, -1, 1));
		
		if(left.getRawButtonPressed(2))
			elevator.goTo(elevator.getCurrentLifterPosition().getAdjacentAbove());
		else if(left.getRawButtonPressed(3))
			elevator.goTo(elevator.getCurrentLifterPosition().getAdjacentBelow());
		
		if(right.getRawButtonPressed(3))
			turnTable.goTo(turnTable.getCurrentTurnTablePosition().getLeftAdjacent());
		else if(right.getRawButtonPressed(4))
			turnTable.goTo(turnTable.getCurrentTurnTablePosition().getRightAdjacent());
	}
}
