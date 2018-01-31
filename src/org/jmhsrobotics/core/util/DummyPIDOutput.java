package org.jmhsrobotics.core.util;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class DummyPIDOutput implements PIDOutput, Sendable
{
	private String name;
	private String subsystem;
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
	public String getName()
	{
		return name;
	}

	@Override
	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String getSubsystem()
	{
		return subsystem;
	}

	@Override
	public void setSubsystem(String subsystem)
	{
		this.subsystem = subsystem;
	}

	@Override
	public void initSendable(SendableBuilder builder)
	{
		builder.addDoubleProperty("value", this::get, this::pidWrite);
	}
}
