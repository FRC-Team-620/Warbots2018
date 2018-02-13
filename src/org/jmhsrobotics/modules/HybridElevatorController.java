package org.jmhsrobotics.modules;

import org.jmhsrobotics.hardwareinterface.HybridLifter;

public class HybridElevatorController implements HybridLifter
{
	private Position currentPosition;
	
	@Override
	public void goTo(Position position)
	{
		if(currentPosition == position)
			return;
		
		currentPosition = position;
	}

	@Override
	public void goToRaw(double linearHeight, boolean raisePneumatics)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void driveManual(double linearSpeed, boolean raisePneumatics)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void climbFullPower()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Position getCurrentLifterPosition()
	{
		return currentPosition;
	}
}