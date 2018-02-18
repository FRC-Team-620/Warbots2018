package org.jmhsrobotics.core.modulesystem;

import java.util.function.Supplier;

class DummySendableEntry
{
	private String key;
	private Supplier<String> getter;
	
	public DummySendableEntry(String key, Supplier<String> getter)
	{
		this.key = key;
		this.getter = getter;
	}
	
	public String getName()
	{
		return key;
	}
	
	public String getValue()
	{
		return getter.get();
	}
}
