package org.jmhsrobotics.hardwareinterface;

public interface WheelSwitchingDrive extends Drive
{
	public void setWheelConfiguration(WheelConfiguration wc);

	public WheelConfiguration getWheelConfiguration();

	public default void switchWheelConfigration()
	{
		setWheelConfiguration(getWheelConfiguration().opposite());
	}
}
