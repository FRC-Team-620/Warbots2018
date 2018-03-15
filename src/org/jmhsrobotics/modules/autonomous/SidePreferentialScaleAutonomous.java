package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;

public class SidePreferentialScaleAutonomous extends AutonomousCommand
{
	@Override
	protected void loadPath(AutoStrategy strategy)
	{
		Point beforeSwitch = new Point(96.083, Angle.fromDegrees(4.4));
		strategy.addSequential(beforeSwitch, 24);
		
		Point afterSwitch = beforeSwitch.plus(new Point(101.68, Angle.ZERO));
		strategy.addSequential(afterSwitch, 24);
		
		Point atScale = afterSwitch.plus(new Point(60.85, Angle.fromDegrees(-160.8)));
		AutoStrategy moveAndRaise = strategy.createFork();
		moveAndRaise.addParallel(new DriveTo(atScale, 6));
		
		strategy.addSequential(new LowerGrabber());
		strategy.addSequential(new TimedCubeEject());
	}
}
