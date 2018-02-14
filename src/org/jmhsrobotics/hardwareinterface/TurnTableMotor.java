package org.jmhsrobotics.hardwareinterface;

public interface TurnTableMotor
{
	public void driveTurnTableMotor(double speed);
	public boolean readMiddleLimitSwitch();
}
