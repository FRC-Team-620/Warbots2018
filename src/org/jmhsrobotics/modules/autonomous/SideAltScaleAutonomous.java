package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;
import org.jmhsrobotics.hardwareinterface.ElevatorController;

public class SideAltScaleAutonomous extends AutonomousCommand
{
	@Override
	protected void loadPath(AutoStrategy strategy)
	{
		Point beforeSwitch = new Point(96.083, Angle.fromDegrees(4.4));
		strategy.addSequential(beforeSwitch, 24);
		
		Point afterSwitch = beforeSwitch.plus(new Point(101.68, Angle.ZERO));
		strategy.addSequential(afterSwitch, 24);
		
		Point beforeScale = afterSwitch.plus(new Point(192.92, Angle.fromDegrees(-97.1)));
		strategy.addSequential(beforeScale, 32);
		
		Point atScale = beforeScale.plus(new Point(34.66, Angle.fromDegrees(90)));
		AutoStrategy moveAndRaise = strategy.createFork();
		moveAndRaise.addParallel(new DriveTo(atScale, 6));
		
		strategy.addSequential(new LowerGrabber());
		strategy.addSequential(new TimedCubeEject());
	}
}