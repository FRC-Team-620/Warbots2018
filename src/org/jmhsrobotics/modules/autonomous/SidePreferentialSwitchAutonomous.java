package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;

public class SidePreferentialSwitchAutonomous extends AutonomousCommand
{
	@Override
	public void loadPath(AutoStrategy strategy)
	{
		AutoStrategy prepareToEject = strategy.createBranch();
		
		AutoStrategy prepareGrabber = prepareToEject.createBranch();
		prepareGrabber.addSequential(new MoveElevator(7000, false));
		prepareGrabber.addSequential(new SetGrabberRaised(false));
		
		prepareToEject.addParallel(prepareGrabber);
		
		prepareToEject.addSequential(new Point(77.003, Angle.fromDegrees(-1.5)), 40);
		prepareToEject.addSequential(new Point(149.320, Angle.fromDegrees(-0.6)).plus(new Point(0, -12)), 6);
		prepareToEject.addSequential(Angle.RIGHT, Angle.fromDegrees(10));
		
		strategy.addSequential(prepareToEject);
		
		strategy.addSequential(new TimedDrive(2));
		strategy.addSequential(new CubeDrop());
	}
}
