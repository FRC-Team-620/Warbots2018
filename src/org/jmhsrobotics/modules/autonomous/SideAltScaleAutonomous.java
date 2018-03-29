package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;

public class SideAltScaleAutonomous extends AutonomousCommand
{
	@Override
	protected void loadPath(AutoStrategy strategy)
	{
		AutoStrategy prepareToEject = strategy.createBranch();
		
		prepareToEject.addParallel(new SetGrabberRaised(true));

		Point firstCorner = new Point(217.2, Angle.fromDegrees(0));
		strategy.addSequential(firstCorner, 12);
		
		Point secondCorner = firstCorner.plus(new Point(240, Angle.RIGHT));
		strategy.addSequential(secondCorner, 30);
		
		Point nextToScale = secondCorner.plus(new Point(89, Angle.fromDegrees(0)));
		strategy.addSequential(nextToScale, 8);
		
		AutoStrategy turnAndRaise = prepareToEject.createBranch();
		turnAndRaise.addParallel(new TurnTo(Angle.INVERSE_RIGHT, Angle.fromDegrees(10)));
		turnAndRaise.addParallel(new MoveElevator(11000, true));
		strategy.addSequential(turnAndRaise);
		
		strategy.addSequential(prepareToEject);
		
		strategy.addSequential(nextToScale.plus(new Point(-12, 0)), 6);
		
		strategy.addSequential(new SetGrabberRaised(false));
		
		strategy.addSequential(new CubeEject());
	}
}