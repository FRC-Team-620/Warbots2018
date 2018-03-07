package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.modulesystem.Sublinker;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;

public class SideAltSwitchPreferentialScaleAutonomous extends AutonomousCommand
{
	@Override
	public void onLink(Sublinker sublinker)
	{
		PathLinker path = new PathLinker(sublinker);
		
		Point intermediate = new Point(229.874, Angle.fromDegrees(2.2));
		path.addSequential(intermediate, 40);
		Point firstTarget = intermediate.plus(new Point(161.034, Angle.fromDegrees(-79.3))).plus(new Point(24, -2));
		path.addSequential(firstTarget, 6);
		path.addSequential(Angle.REVERSE, Angle.fromDegrees(10));
		path.addSequential(new TimedDrive(2));
		path.addSequential(new TimedCubeEject());
	}
}
