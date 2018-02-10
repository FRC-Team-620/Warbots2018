package org.jmhsrobotics.core.util;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class DummyPIDOutput extends PlainSendable implements PIDOutput
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

	@Override
	public void initSendable(SendableBuilder builder)
	{
		builder.addDoubleProperty("value", this::get, this::pidWrite);
	}
}
