package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;

public class SideAltSwitchAutonomous extends AutonomousCommand
{
	@Override
	public void loadPath(AutoStrategy strategy)
	{
		AutoStrategy prepareToEject = strategy.createBranch();
		
		AutoStrategy prepareGrabber = prepareToEject.createBranch();
		prepareGrabber.addSequential(new MoveElevator(7000, false));
		prepareGrabber.addSequential(new SetGrabberRaised(false));
		prepareToEject.addParallel(prepareGrabber);
		
		Point intermediate = new Point(220, Angle.fromDegrees(2.2));
		prepareToEject.addSequential(intermediate, 40);
		Point firstTarget = intermediate.plus(new Point(161.034, Angle.fromDegrees(-79.3))).plus(new Point(24, -8));
		prepareToEject.addSequential(firstTarget, 6);
		prepareToEject.addSequential(Angle.REVERSE, Angle.fromDegrees(10));
		
		strategy.addSequential(prepareToEject);
		
		strategy.addSequential(new TimedDrive(2));
//		strategy.addSequential(new CubeEject());
	}
}
