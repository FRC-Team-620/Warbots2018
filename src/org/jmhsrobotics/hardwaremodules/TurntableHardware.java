package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.hardwareinterface.TurnTableMotor;

public class TurntableHardware implements TurnTableMotor
{
	@Override
	public void driveTurnTableMotor(double speed)
	{
	}

	@Override
	public boolean readMiddleLimitSwitch()
	{
		return false;
	}
}