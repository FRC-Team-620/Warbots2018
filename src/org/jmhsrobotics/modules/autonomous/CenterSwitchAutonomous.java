package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;

public class CenterSwitchAutonomous extends AutonomousCommand
{
	@Override
	protected void loadPath(AutoStrategy strategy)
	{
		AutoStrategy prepareToEject = strategy.createBranch();
		
		AutoStrategy prepareGrabber = prepareToEject.createBranch();
		prepareGrabber.addSequential(new MoveElevator(7000, false));
		prepareGrabber.addSequential(new SetGrabberRaised(false));
		
		prepareToEject.addParallel(prepareGrabber);
		
		Point firstTarget = new Point(116.306, Angle.fromDegrees(30.3)).plus(new Point(12, -12));
		prepareToEject.addSequential(firstTarget, 20);
		prepareToEject.addSequential(Angle.ZERO, Angle.fromDegrees(10));
		
		strategy.addSequential(prepareToEject);
		
		strategy.addSequential(new TimedDrive(2));
		strategy.addSequential(new CubeDrop());		
	}
}
