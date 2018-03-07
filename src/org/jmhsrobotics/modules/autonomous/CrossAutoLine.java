package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.modulesystem.Sublinker;
import org.jmhsrobotics.core.util.Point;

public class CrossAutoLine extends AutonomousCommand
{
	@Override
	public void onLink(Sublinker sublinker)
	{
		PathLinker path = new PathLinker(sublinker);
		path.addSequential(new Point(Double.POSITIVE_INFINITY, 0), 0);
	}
}
