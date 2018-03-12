package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Point;
import org.jmhsrobotics.modules.drivecontrol.DoubleDerivativeOutputSmoother;
import org.jmhsrobotics.modules.drivecontrol.OutputSmoother;

public class TestAutonomous extends AutonomousCommand
{	
	@Override
	protected void loadPath(AutoStrategy strategy)
	{
		OutputSmoother speed = new DoubleDerivativeOutputSmoother(1, .03, .05);
		OutputSmoother turn = new DoubleDerivativeOutputSmoother(1, .08, Double.POSITIVE_INFINITY);
		strategy.addSequential(new ChangeDriveOutputSmoothing(speed, turn));
		strategy.addSequential(new Point(0, 72), 10);
	}
}
