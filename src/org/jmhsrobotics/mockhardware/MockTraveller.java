package org.jmhsrobotics.mockhardware;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.hardwareinterface.Traveller;

import edu.wpi.first.wpilibj.command.Command;

public class MockTraveller implements Module, Traveller
{
	@Override
	public void drive(double speed)
	{
		System.out.println("Driving traveller at " + speed);
	}

	@Override
	public void reset()
	{
		System.out.println("Resetting traveller");
	}

	@Override
	public double getHeight()
	{
		return 0;
	}

	@Override
	public boolean isBottomLimitSwitchPressed()
	{
		return false;
	}

	@Override
	public Command getTest()
	{
		return null;
	}
}