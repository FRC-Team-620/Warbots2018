package org.jmhsrobotics.core.modulesystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public abstract class CommandGroupModule extends CommandGroup implements Module
{
	@Override
	public Command getTest()
	{
		return this;
	}
}