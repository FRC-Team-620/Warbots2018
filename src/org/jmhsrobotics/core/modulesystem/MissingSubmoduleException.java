package org.jmhsrobotics.core.modulesystem;

import java.util.NoSuchElementException;

public class MissingSubmoduleException extends NoSuchElementException
{
	private static final long serialVersionUID = 1L;

	private Class<?> expectedType;
	private ModuleManager manager;
	private Module module;
	
	public MissingSubmoduleException(Class<?> type, ModuleManager manager, Module module)
	{
		super(type.toString());
		
		expectedType = type;
		this.manager = manager;
		this.module = module;
	}
	
	public Class<?> getDesiredSubmoduleType()
	{
		return expectedType;
	}
	
	public ModuleManager getModuleManager()
	{
		return manager;
	}
	
	public Module getFaultyModule()
	{
		return module;
	}
}