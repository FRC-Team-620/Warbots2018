package org.jmhsrobotics.modules;

import java.util.Optional;

import org.jmhsrobotics.core.modules.SubsystemManager;
import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.RobotMath;
import org.jmhsrobotics.hardwareinterface.TurnTable;
import org.jmhsrobotics.hardwareinterface.TurnTableController;

public class TurnTableControlCommand extends CommandModule implements TurnTableController
{
	private final static double BASE_TURN_SPEED = 0.4;
	private final static double MAX_SPEED_BOOST = 0.6;
	
	private @Submodule Optional<SubsystemManager> subsystems;
	private @Submodule TurnTable tableHardware;
	
	private double positionEstimate;
	private Position currentPosition;
	private Position targetPosition;
	
	@Override
	public void onLink()
	{
		subsystems.ifPresent(sm -> requires(sm.getSubsystem("TurnTable")));
		
		currentPosition = Position.left;
		targetPosition = Position.left;
		positionEstimate = -1;
	}
	
	@Override
	public void goTo(Position position)
	{
		System.out.println("turning table to " + position);
		
		if(position == targetPosition)
			return;
		targetPosition = position;
	}

	@Override
	public Position getCurrentTurnTablePosition()
	{
		return currentPosition;
	}
	
	@Override
	protected void execute()
	{
		positionEstimate += RobotMath.curve(tableHardware.getSpeed(), 2) * .06;
		if(currentPosition == Position.center)
			positionEstimate = 0;
		
		double targetPos;
		switch(targetPosition)
		{
			case left:
				targetPos = 1;
				break;
			case right:
				targetPos = -1;
				break;
			default:
				targetPos = 0;
				break;
		}
		
		double distanceFromTarget = Math.abs(targetPos - positionEstimate);
		
		double speedBoost = RobotMath.xKinkedMap(distanceFromTarget, -1, 1, 0, -.3, .3, -MAX_SPEED_BOOST, MAX_SPEED_BOOST);
//		System.out.println("Speed Boost: " + speedBoost);
		
		System.out.println("Boost: " + speedBoost + " Absolute: " + currentPosition + " Estimate: " + positionEstimate + " Target: " + targetPosition);
		
		double turnSpeed = BASE_TURN_SPEED + speedBoost;
		
		if(currentPosition == targetPosition)
			if(Math.abs(positionEstimate) < 1.2)
				tableHardware.drive(Math.signum(targetPosition.compareTo(Position.center)) * turnSpeed);
			else
				tableHardware.drive(0);
		else
		{
			tableHardware.drive(Math.copySign(turnSpeed, targetPosition.compareTo(currentPosition)));
			
			if(!tableHardware.readMiddleLimitSwitch())
				currentPosition = Position.center;
			else if(currentPosition == Position.center)
				currentPosition = targetPosition;
		}
	}

	@Override
	protected boolean isFinished()
	{
		return false;
	}
}