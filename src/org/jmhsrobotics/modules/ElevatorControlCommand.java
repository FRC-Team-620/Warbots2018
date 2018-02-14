package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.hardwareinterface.ElevatorController;

public class ElevatorControlCommand extends CommandModule implements ElevatorController
{

	@Override
	protected boolean isFinished()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void goTo(Position position)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Position getCurrentLifterPosition()
	{
		// TODO Auto-generated method stub
		return null;
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

}
