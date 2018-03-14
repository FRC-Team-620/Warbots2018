package org.jmhsrobotics.hardwareinterface;

public interface Traveller
{
	public void reset(int currentHeight);
	public void drive(double speed);
	public void driveTo(int target);
	public int getHeight();
	public boolean isBottomLimitSwitchPressed();
	public boolean isTopLimitSwitchPressed();
}
