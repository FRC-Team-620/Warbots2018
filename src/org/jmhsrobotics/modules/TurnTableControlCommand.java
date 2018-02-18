package org.jmhsrobotics.modules;

import java.util.Optional;

import org.jmhsrobotics.core.modules.SubsystemManager;
import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.TurnTableController;
import org.jmhsrobotics.hardwareinterface.TurnTable;

public class TurnTableControlCommand extends CommandModule implements TurnTableController
{
	private final static double TURN_SPEED = 0.3;
	
	private @Submodule Optional<SubsystemManager> subsystems;
	private @Submodule TurnTable tableHardware;
	
	private Position currentPosition;
	private Position targetPosition;
	
	@Override
	public void onLink()
	{
		subsystems.ifPresent(sm -> requires(sm.getSubsystem("Grabber")));
		
		currentPosition = Position.center;
		targetPosition = Position.center;
	}
	
	@Override
	public void goTo(Position position)
	{
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
		if(currentPosition == targetPosition)
			tableHardware.driveTurnTableMotor(Math.signum(targetPosition.compareTo(Position.center)) * TURN_SPEED);
		else
		{
			tableHardware.driveTurnTableMotor(Math.signum(targetPosition.compareTo(currentPosition)) * TURN_SPEED);
			
			if(tableHardware.readMiddleLimitSwitch())
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