package org.jmhsrobotics.hardwareinterface;

public interface Traveller
{
	public void drive(double speed);
	public void reset();
	public double getHeight();
	public boolean isBottomLimitSwitchPressed();
}
