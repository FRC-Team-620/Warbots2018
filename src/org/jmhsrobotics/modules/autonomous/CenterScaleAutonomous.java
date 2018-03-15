package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;

public class CenterScaleAutonomous extends AutonomousCommand
{
	@Override
	protected void loadPath(AutoStrategy strategy)
	{
		Point p = new Point(56.6, Angle.fromDegrees(125.517));
		strategy.addSequential(p, 30);
		
		p = p.plus(new Point(112.62, Angle.fromDegrees(13.2)));
		strategy.addSequential(p, 30);
		
		p = p.plus(new Point(124.551, Angle.fromDegrees(-9.5)));
		strategy.addSequential(p, 10);
		
		AutoStrategy turnAndRaise = strategy.createFork();
		turnAndRaise.addParallel(new TurnTo(Angle.RIGHT, Angle.fromDegrees(0)));
	}
}
