package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.modulesystem.annotations.CommandFactoryModule;

import edu.wpi.first.wpilibj.command.Command;

@CommandFactoryModule
public class DriveStraightDistance implements Module
{
	private @Submodule DriveStraight drive;

	public Command newInstance(int distance)
	{
		return new Command()
		{
			@Override
			protected void execute()
			{
				drive.driveStraight(0.8);
			}
			
			@Override
			protected boolean isFinished()
			{
				return timeSinceInitialized() > distance;
			}
		};
	}

	@Override
	public Command getTest()
	{
		return newInstance(5);
	}
}
