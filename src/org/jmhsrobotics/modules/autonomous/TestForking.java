package org.jmhsrobotics.modules.autonomous;

public class TestForking extends AutonomousCommand
{
	@Override
	protected void loadPath(AutoStrategy strategy)
	{
		strategy.addSequential(new TestPathNode("Test 1", 2));
	}
}
