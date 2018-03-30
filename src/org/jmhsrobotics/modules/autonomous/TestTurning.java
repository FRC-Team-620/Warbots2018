package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Point;

public class TestTurning extends AutonomousCommand
{
	@Override
	protected void loadPath(AutoStrategy strategy)
	{
		strategy.addSequential(new Point(36, 36), 10);
	}
}
