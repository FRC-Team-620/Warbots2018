package org.jmhsrobotics.core.modulesystem;

import edu.wpi.first.wpilibj.command.Command;

public interface Module
{
	public Command getTest();
	
	public default void onLink(Sublinker linker)
	{
		onLink();
	}
	
	public default void onLink()
	{}
}