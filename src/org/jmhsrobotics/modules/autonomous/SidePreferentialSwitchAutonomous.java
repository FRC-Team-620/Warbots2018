package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;

public class SidePreferentialSwitchAutonomous extends AutonomousCommand
{
	@Override
	public void loadPath(AutoStrategy strategy)
	{
		strategy.addSequential(new Point(77.003, Angle.fromDegrees(-1.5)), 40);
		Point firstTarget = new Point(149.320, Angle.fromDegrees(-0.6));
		strategy.addSequential(firstTarget, 6);
		strategy.addSequential(Angle.RIGHT, Angle.fromDegrees(10));
		strategy.addSequential(new TimedDrive(2));
		strategy.addSequential(new TimedCubeEject());
	}
}
