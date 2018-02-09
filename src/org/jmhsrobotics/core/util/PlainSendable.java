package org.jmhsrobotics.core.util;
import edu.wpi.first.wpilibj.Sendable;

public abstract class PlainSendable implements Sendable
{
	private String name, system;
	
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
		return system;
	}

	@Override
	public void setSubsystem(String subsystem)
	{
		system = subsystem;
	}
}
