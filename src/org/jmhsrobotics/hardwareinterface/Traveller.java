package org.jmhsrobotics.hardwareinterface;

public interface Traveller
{
	public void reset(int currentHeight);
	
	public void drive(double speed);
	public double getMovementRate();
	
	public void driveTo(int target);
	public void setPID(double p, double i, double d, int integralZone, int maxError, double rampRate, double maxOutput);
	public double getError();
	
	public int getHeight();
	public boolean isBottomLimitSwitchPressed();
	public boolean isTopLimitSwitchPressed();
}
