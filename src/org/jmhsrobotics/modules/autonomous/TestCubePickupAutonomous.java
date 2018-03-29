package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Point;

public class TestCubePickupAutonomous extends AutonomousCommand
{	
	@Override
	protected void loadPath(AutoStrategy strategy)
	{
		strategy.addSequential(new SetGrabberRaised(false));
		strategy.addSequential(new TestPathNode("soup", 1));
		strategy.addSequential(new PickUpCube(new Point(0, 101.5)));
	}
}
