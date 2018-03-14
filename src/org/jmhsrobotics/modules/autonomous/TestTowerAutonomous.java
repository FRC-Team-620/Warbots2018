package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Point;

public class TestTowerAutonomous extends AutonomousCommand
{
	@Override
	protected void loadPath(AutoStrategy strategy)
	{
		strategy.addSequential(new Point(30, 0), 20);
		AutoStrategy moveAndRaise = strategy.createFork();
	}
}
