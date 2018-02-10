package org.jmhsrobotics.hardwareinterface;

public interface HybridLifter
{
	public static enum Position
	{
		ground, exchangePortal, arcadeSwitch, scaleLow, scaleMedium, scaleHigh
	}
	
	public void goTo(Position position);
	public void goToRaw(double linearHeight, boolean raisePneumatics);
	public void driveManual(double linearSpeed, boolean raisePneumatics);
	public void climbFullPower();
}