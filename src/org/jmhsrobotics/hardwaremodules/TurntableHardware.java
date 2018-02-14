package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.hardwareinterface.TurnTableMotorWithLimitSwitches;

public class TurntableHardware implements TurnTableMotorWithLimitSwitches
{
	@Override
	public void driveTurnTableMotor(double speed)
	{
	}

	@Override
	public boolean readMiddleEncoder()
	{
		return false;
	}
}