package org.jmhsrobotics.core.modulesystem;

import org.jmhsrobotics.core.util.RobotMath;

import edu.wpi.first.wpilibj.command.Command;

public abstract class ControlScheme extends CommandModule
{
	@Override
	protected abstract void execute();
	
	@Override
	protected boolean isFinished()
	{
		return false;
	}
	
	protected final double deadZone(double value, double deadZone, double endZone)
	{
		value = RobotMath.linearMap(value, -1 + endZone, 1 - endZone, -1, 1);
		value = RobotMath.constrain(value, -1, 1);
		return RobotMath.xKinkedMap(value, -1, 1, 0, -deadZone, deadZone, -1, 1);
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