package org.jmhsrobotics.modules.autonomous;

public class TestTowerAutonomous extends AutonomousCommand
{
	@Override
	protected void loadPath(AutoStrategy strategy)
	{
		strategy.addSequential(new MoveElevator(10000, true));
	}
}
