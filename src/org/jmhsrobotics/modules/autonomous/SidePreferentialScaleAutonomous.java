package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;

public class SidePreferentialScaleAutonomous extends AutonomousCommand
{
	@Override
	protected void loadPath(AutoStrategy strategy)
	{
		AutoStrategy prepareToEject = strategy.createBranch();
		
		prepareToEject.addParallel(new SetGrabberRaised(false));

		Point thePoint = new Point(305.2, Angle.ZERO).plus(new Point(-12, 0));
		strategy.addSequential(thePoint, 8);
		
		AutoStrategy turnAndRaise = prepareToEject.createBranch();
		turnAndRaise.addParallel(new MoveElevator(11000, true));
		turnAndRaise.addParallel(new TurnTo(Angle.RIGHT, Angle.fromDegrees(10)));
		strategy.addSequential(turnAndRaise);
		
		strategy.addSequential(prepareToEject);
		
		strategy.addSequential(thePoint.plus(new Point(35, 0)), 10);
		strategy.addSequential(new CubeEject());
	}
}
