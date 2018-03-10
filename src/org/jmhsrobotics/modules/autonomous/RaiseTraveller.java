package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.hardwareinterface.ElevatorController.Position;

public class RaiseTraveller extends PathNode
{
	public RaiseTraveller(Position position)
	{
		
	}
	
	@Override
	protected boolean isFinished()
	{
		return false;
	}
}
