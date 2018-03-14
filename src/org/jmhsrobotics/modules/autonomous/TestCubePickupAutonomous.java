package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Point;
import org.jmhsrobotics.modules.drivecontrol.DoubleDerivativeOutputSmoother;
import org.jmhsrobotics.modules.drivecontrol.OutputSmoother;

public class TestCubePickupAutonomous extends AutonomousCommand
{	
	@Override
	protected void loadPath(AutoStrategy strategy)
	{
		strategy.addSequential(new PickUpCube(new Point(0, 60)));
	}
}
