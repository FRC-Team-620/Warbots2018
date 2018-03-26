package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;
import org.jmhsrobotics.modules.drivecontrol.ConstraintOutputSmoother;

public class CenterScaleAutonomous extends AutonomousCommand
{
	@Override
	protected void loadPath(AutoStrategy strategy)
	{	
		Point p = new Point(56.6, Angle.fromDegrees(125.517));
		strategy.addSequential(new ChangeDriveOutputSmoothing(new ConstraintOutputSmoother(0.5), new ConstraintOutputSmoother(.8)));
		strategy.addSequential(p, 30);
		
		p = p.plus(new Point(112.62, Angle.fromDegrees(13.2)));
		strategy.addSequential(p, 30);
		
		p = p.plus(new Point(124.551, Angle.fromDegrees(-9.5)));
		strategy.addSequential(p, 10);
		
		AutoStrategy turnAndRaise = strategy.createBranch();
		turnAndRaise.addParallel(new MoveElevator(10000, true));
		turnAndRaise.addParallel(new TurnTo(Angle.ZERO, Angle.fromDegrees(10)));
		strategy.addSequential(turnAndRaise);
		
		strategy.addSequential(new SetGrabberRaised(false));
		strategy.addSequential(new CubeDrop());
	}
}
