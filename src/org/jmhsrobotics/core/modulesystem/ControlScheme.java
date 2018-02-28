package org.jmhsrobotics.core.modulesystem;

import org.jmhsrobotics.core.modules.OperatorInterface;

import edu.wpi.first.wpilibj.command.Command;

public abstract class ControlScheme extends CommandModule
{
	private @Submodule OperatorInterface oi;

	protected OperatorInterface getOI()
	{
		return oi;
	}
	
	@Override
	protected abstract void execute();
	
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
				ControlScheme.this.start();
			}
			
			@Override
			protected boolean isFinished()
			{
				return timeSinceInitialized() > 30;
			}
			
			@Override
			protected void end()
			{
				ControlScheme.this.cancel();
			}
		};
	}
}