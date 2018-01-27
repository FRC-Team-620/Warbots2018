package org.jmhsrobotics.core.modulesystem;

import org.jmhsrobotics.core.modules.OperatorInterface;

import edu.wpi.first.wpilibj.command.Command;

public abstract class ControlSchemeModule extends CommandModule
{
	protected @Submodule OperatorInterface oi;

	@Override
	protected boolean isFinished()
	{
		return false;
	}

	@Override
	public Command getTest()
	{
		return new Command()
		{
			@Override
			protected void initialize()
			{
				ControlSchemeModule.this.start();
			}
			
			@Override
			protected boolean isFinished()
			{
				return timeSinceInitialized() > 30;
			}
			
			@Override
			protected void end()
			{
				ControlSchemeModule.this.cancel();
			}
		};
	}
}