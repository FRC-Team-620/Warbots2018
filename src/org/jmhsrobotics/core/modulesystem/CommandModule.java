package org.jmhsrobotics.core.modulesystem;

import edu.wpi.first.wpilibj.command.Command;

public abstract class CommandModule extends Command implements Module
{
	@Override
	public Command getTest()
	{
		return this;
	}
}
