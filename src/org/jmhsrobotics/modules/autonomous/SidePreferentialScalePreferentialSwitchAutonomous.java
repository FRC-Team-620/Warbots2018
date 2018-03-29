package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;

public class SidePreferentialScalePreferentialSwitchAutonomous extends SidePreferentialScaleAutonomous
{
	@Override
	protected void loadPath(AutoStrategy strategy)
	{
		Point origin = AutonomousCommand.getLeftOrigin();
		
		super.loadPath(strategy);
		
		strategy.addSequential(Angle.REVERSE, Angle.fromDegrees(10));
		strategy.addSequential(origin.plus(new Point(-88.75, 133.743)), 30);
	}
}
