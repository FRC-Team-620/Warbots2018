package org.jmhsrobotics.hardwareinterface;

public interface TurnTable
{
	public void driveTurnTableMotor(double speed);
	public boolean readMiddleLimitSwitch();
}
