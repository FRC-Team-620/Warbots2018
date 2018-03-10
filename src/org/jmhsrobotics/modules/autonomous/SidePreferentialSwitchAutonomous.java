package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.modulesystem.Sublinker;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;

public class SidePreferentialSwitchAutonomous extends AutonomousCommand
{
	@Override
	public void onLink(Sublinker sublinker)
	{
		PathLinker path = new PathLinker(sublinker);
		
		path.addSequential(new Point(77.003, Angle.fromDegrees(-1.5)), 40);
		Point firstTarget = new Point(149.320, Angle.fromDegrees(-0.6));
		path.addSequential(firstTarget, 6);
		path.addSequential(Angle.RIGHT, Angle.fromDegrees(10));
		path.addSequential(new TimedDrive(2));
		path.addSequential(new TimedCubeEject());
	}
}
