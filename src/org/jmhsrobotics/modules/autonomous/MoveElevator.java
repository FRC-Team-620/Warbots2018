package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.ElevatorController;

public class MoveElevator extends PathNode
{
	private @Submodule ElevatorController elevator;
	
	public MoveElevator(int linearHeight, boolean pistons)
	{
		
	}
	
	@Override
	protected boolean isFinished()
	{
		return false;
	}
}
