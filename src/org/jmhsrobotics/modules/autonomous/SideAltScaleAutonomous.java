package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;

public class SideAltScaleAutonomous extends AutonomousCommand
{
	@Override
	protected void loadPath(AutoStrategy strategy)
	{
		AutoStrategy prepareToEject = strategy.createBranch();
		
		prepareToEject.addParallel(new SetGrabberRaised(false));

		Point firstCorner = new Point(217.2, Angle.fromDegrees(0));
		strategy.addSequential(firstCorner, 12);
		
		Point secondCorner = firstCorner.plus(new Point(204.9, Angle.RIGHT));
		strategy.addSequential(secondCorner, 30);
		
		Point nextToScale = secondCorner.plus(new Point(89, Angle.fromDegrees(0)));
		strategy.addSequential(nextToScale, 8);
		
		AutoStrategy turnAndRaise = prepareToEject.createBranch();
		turnAndRaise.addParallel(new TurnTo(Angle.INVERSE_RIGHT, Angle.fromDegrees(10)));
		turnAndRaise.addParallel(new MoveElevator(11000, true));
		strategy.addSequential(turnAndRaise);
		
		strategy.addSequential(prepareToEject);
		
		strategy.addSequential(new CubeEject());
	}
}