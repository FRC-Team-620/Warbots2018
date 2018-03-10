package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;

public class CenterSwitchAutonomous extends AutonomousCommand
{
	@Override
	protected void loadPath(AutoStrategy strategy)
	{
		Point firstTarget = new Point(116.306, Angle.fromDegrees(30.3)).plus(new Point(0, -12));
		strategy.addSequential(firstTarget, 20);
		strategy.addSequential(Angle.ZERO, Angle.fromDegrees(10));
		strategy.addSequential(new TimedDrive(2));
		strategy.addSequential(new TimedCubeEject());
	}
}
