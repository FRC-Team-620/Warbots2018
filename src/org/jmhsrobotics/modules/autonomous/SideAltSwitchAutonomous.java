package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;

public class SideAltSwitchAutonomous extends AutonomousCommand
{
	@Override
	public void loadPath(AutoStrategy strategy)
	{
		Point intermediate = new Point(229.874, Angle.fromDegrees(2.2));
		strategy.addSequential(intermediate, 40);
		Point firstTarget = intermediate.plus(new Point(161.034, Angle.fromDegrees(-79.3))).plus(new Point(24, -2));
		strategy.addSequential(firstTarget, 6);
		strategy.addSequential(Angle.REVERSE, Angle.fromDegrees(10));
		strategy.addSequential(new TimedDrive(2));
		strategy.addSequential(new TimedCubeEject());
	}
}
