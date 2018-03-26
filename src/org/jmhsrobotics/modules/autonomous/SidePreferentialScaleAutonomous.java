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

		strategy.addSequential(new Point(305.2, Angle.fromDegrees(-2.2)), 8);
		
		AutoStrategy turnAndRaise = prepareToEject.createBranch();
		turnAndRaise.addParallel(new TurnTo(Angle.INVERSE_RIGHT, Angle.fromDegrees(10)));
		turnAndRaise.addParallel(new MoveElevator(11000, true));
		strategy.addSequential(turnAndRaise);
		
		strategy.addSequential(prepareToEject);
		
		strategy.addSequential(new CubeEject());
	}
}
