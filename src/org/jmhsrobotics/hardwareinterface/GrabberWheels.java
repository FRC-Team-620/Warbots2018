package org.jmhsrobotics.hardwareinterface;

import org.jmhsrobotics.core.util.RobotMath;

public interface GrabberWheels
{
	public void set(double speed, double jank);
	public boolean hasPrism();
	
	public static double getSideComponent(double speed, double jank)
	{
		return RobotMath.curve(RobotMath.curve(speed, 2) + RobotMath.curve(jank, 2), .5);
	}
}
