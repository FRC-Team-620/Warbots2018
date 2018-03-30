package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;

public class CenterSwitchAutonomous extends AutonomousCommand
{
	@Override
	protected void loadPath(AutoStrategy strategy)
	{
		AutoStrategy prepareToEject = strategy.createBranch();
		
		prepareToEject.addParallel(new MoveElevator(7500, false));
		
		Point firstTarget = new Point(116.306, Angle.fromDegrees(-30.3)).plus(new Point(12, -12));
		prepareToEject.addSequential(firstTarget, 20);
		prepareToEject.addSequential(Angle.ZERO, Angle.fromDegrees(10));
		
		strategy.addSequential(prepareToEject);
		
		strategy.addSequential(new TimedDrive(1.5));
		strategy.addSequential(new SetGrabberRaised(false));
		strategy.addSequential(new CubeEject());
		strategy.addSequential(new CubeDrop());
	}
}
