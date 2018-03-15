package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.ElevatorController;

public class MoveElevator extends PathNode
{
	private final static int TIME_TO_RAISE_PISTONS = 5;
	
	private @Submodule ElevatorController elevator;
	
	private boolean changingPistons;
	
	public MoveElevator(int linearHeight, boolean pistons)
	{
		changingPistons = pistons != elevator.isPneumaticsExtended();
		elevator.goToRaw(linearHeight, pistons);
	}
	
	@Override
	protected boolean isFinished()
	{
		if(!elevator.onTarget())
			return false;
		
		if(changingPistons)
			if(timeSinceInitialized() < TIME_TO_RAISE_PISTONS)
				return false;
		
		return true;
	}
}
