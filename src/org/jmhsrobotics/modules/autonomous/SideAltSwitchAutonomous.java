package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;

public class SideAltSwitchAutonomous extends AutonomousCommand
{
	@Override
	public void loadPath(AutoStrategy strategy)
	{	
		Point origin = AutonomousCommand.getLeftOrigin();
		
		AutoStrategy prepareToEject = strategy.createBranch();
		
		AutoStrategy prepareGrabber = prepareToEject.createBranch();
		prepareGrabber.addSequential(new MoveElevator(7000, false));
		prepareGrabber.addSequential(new SetGrabberRaised(false));
		prepareToEject.addParallel(prepareGrabber);
		
		prepareToEject.addSequential(origin.plus(new Point(134, 88.75)), 30);
		prepareToEject.addSequential(origin.plus(new Point(100, 88.75)), 15);
		prepareToEject.addSequential(origin.plus(new Point(-55.36, 92.23)), 8);
		prepareToEject.addSequential(Angle.REVERSE, Angle.fromDegrees(5));
		prepareToEject.addSequential(origin.plus(new Point(-55.36, 109.25)), 6);
		
		strategy.addSequential(prepareToEject);
		
		strategy.addSequential(new TimedDrive(2));
		strategy.addSequential(new CubeEject());
	}
}
