package org.jmhsrobotics.hardwareinterface;

public interface Traveller
{
	public void drive(double speed);
	public int getHeight();
	public boolean isBottomLimitSwitchPressed();
	public boolean isTopLimitSwitchPressed();
	void driveTo(int target);
	void printStuff();
	public void reset(int currentHeight);
}
