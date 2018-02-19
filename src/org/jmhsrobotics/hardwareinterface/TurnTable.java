package org.jmhsrobotics.hardwareinterface;

public interface TurnTable
{
	public void drive(double speed);
	public double getSpeed();
	public boolean readMiddleLimitSwitch();
}
