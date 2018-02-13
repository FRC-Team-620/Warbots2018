package org.jmhsrobotics.modules;

import org.jmhsrobotics.hardwareinterface.TurnTable;

public class TurnTableController implements TurnTable
{
	private Position currentPosition;
	
	@Override
	public void goTo(Position position)
	{
		if(position == currentPosition)
			return;
		
		currentPosition = position;
	}

	@Override
	public Position getCurrentTurnTablePosition()
	{
		return currentPosition;
	}
}