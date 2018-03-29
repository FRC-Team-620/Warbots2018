package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;

public class SidePreferentialScaleAltSwitch extends SidePreferentialScaleAutonomous
{
	@Override
	protected void loadPath(AutoStrategy strategy)
	{
		Point origin = AutonomousCommand.getLeftOrigin();
		
		super.loadPath(strategy);
		
		AutoStrategy prepareForPickup = strategy.createBranch();
		
		prepareForPickup.addParallel(new MoveElevator(100, false));

		
		prepareForPickup.addSequential(Angle.REVERSE, Angle.fromDegrees(10));
		prepareForPickup.addSequential(origin.plus(new Point(-133.743, -88.75)), 30);
		prepareForPickup.addSequential(origin.plus(new Point(70.25, -88.75)), 8);
		
		strategy.addSequential(prepareForPickup);
		
		strategy.addSequential(new PickUpCube(origin.plus(new Point(70.25, -121.5))));
		strategy.addSequential(new MoveElevator(7500, false));
		strategy.addSequential(origin.plus(new Point(70.25, -109.25)), 6);
		strategy.addSequential(new CubeDrop());
	}
}
