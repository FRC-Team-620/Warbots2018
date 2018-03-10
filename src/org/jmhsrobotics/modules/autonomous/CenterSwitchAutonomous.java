package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.modulesystem.Sublinker;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;

public class CenterSwitchAutonomous extends AutonomousCommand
{
	@Override
	public void onLink(Sublinker sublinker)
	{
		PathLinker path = new PathLinker(sublinker);
		
		Point firstTarget = new Point(116.306, Angle.fromDegrees(30.3)).plus(new Point(0, -12));
		path.addSequential(firstTarget, 20);
		path.addSequential(Angle.ZERO, Angle.fromDegrees(10));
		path.addSequential(new TimedDrive(2));
		path.addSequential(new TimedCubeEject());
	}
}
