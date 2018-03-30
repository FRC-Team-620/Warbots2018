package org.jmhsrobotics.mockhardware;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.hardwareinterface.Traveller;

import edu.wpi.first.wpilibj.command.Command;

public class MockTraveller implements Module, Traveller
{

	@Override
	public void reset(int currentHeight)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drive(double speed)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getMovementRate()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void driveTo(int target)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPID(double p, double i, double d, int integralZone, int maxError, double rampRate, double maxOutput)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getError()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isBottomLimitSwitchPressed()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTopLimitSwitchPressed()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Command getTest()
	{
		// TODO Auto-generated method stub
		return null;
	}

}