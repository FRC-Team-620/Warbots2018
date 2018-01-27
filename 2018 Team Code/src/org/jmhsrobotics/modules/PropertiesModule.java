package org.jmhsrobotics.modules;

import java.util.Properties;

import org.jmhsrobotics.modulesystem.WrapperModule;

public class PropertiesModule extends WrapperModule<Properties> //We probably won't use this, it's just a demo of WrapperModule
{
	public PropertiesModule()
	{
		super(new Properties());
	}
}
