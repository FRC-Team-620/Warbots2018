package org.jmhsrobotics.core.util;

public abstract class RobotMath
{
	public static double linearMap(double n, double sourceMin, double sourceMax, double outputMin, double outputMax)
	{
		return (n - sourceMin) / (sourceMax - sourceMin) * (outputMax - outputMin) + outputMin;
	}
	
	public static double xKinkedMap(double n, double sourceMin, double sourceMax, double flatVal, double flatStart, double flatEnd, double outputMin, double outputMax)
	{
		if(n < flatStart)
			return linearMap(n, sourceMin, flatStart, outputMin, flatVal);
		else if(n > flatEnd)
			return linearMap(n, flatEnd, sourceMax, flatVal, outputMax);
		else
			return flatVal;
	}
	
	public static double yKinkedMap(double n, double sourceMin, double sourceMax, double jumpN, double jumpMin, double jumpMax, double outputMin, double outputMax)
	{
		if(n < jumpN)
			return linearMap(n, sourceMin, jumpN, outputMin, jumpMin);
		else if(n > jumpN)
			return linearMap(n, jumpN, sourceMax, jumpMax, outputMax);
		else
			return (jumpMin + jumpMax) / 2;
	}
	
	public static double curve(double n, double curve)
	{
		return Math.signum(n) * Math.pow(Math.abs(n), curve);
	}
}
