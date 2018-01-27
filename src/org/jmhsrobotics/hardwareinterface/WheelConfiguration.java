package org.jmhsrobotics.hardwareinterface;

public enum WheelConfiguration
{
	standard, lateral;
	
	public WheelConfiguration opposite()
	{
		if(this == standard)
			return lateral;
		return standard;
	}
}
