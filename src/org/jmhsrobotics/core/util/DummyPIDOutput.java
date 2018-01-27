package org.jmhsrobotics.core.util;

import edu.wpi.first.wpilibj.PIDOutput;

public class DummyPIDOutput implements PIDOutput
{
	private double value;
	
	public DummyPIDOutput()
	{
		value = 0;
	}
	
	@Override
	public void pidWrite(double output)
	{
		value = output;
	}
	
	public double get()
	{
		return value;
	}
}
