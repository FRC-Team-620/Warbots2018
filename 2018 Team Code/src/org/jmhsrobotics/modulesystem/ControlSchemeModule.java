package org.jmhsrobotics.modulesystem;

import org.jmhsrobotics.modules.OperatorInterface;

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
				if(timeSinceInitialized() > 30)
				{
					ControlSchemeModule.this.cancel();
					return true;
				}
				
				return false;
			}
		};
	}
}