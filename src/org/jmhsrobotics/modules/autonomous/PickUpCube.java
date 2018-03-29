package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Point;
import org.jmhsrobotics.hardwareinterface.DriveController;
import org.jmhsrobotics.hardwareinterface.GrabberController;
import org.jmhsrobotics.hardwareinterface.GrabberController.Position;

public class PickUpCube extends PathNode
{
	private final static int MIN_TIME_WITH_PRISM = 20;
	private final static int TIME_WITH_PRISM_DECAY_RATE = 2;
	private final static int PICK_UP_DISTANCE = 15;
	private final static int GIVE_UP_DISTANCE = 8;
	private final static int OPEN_DISTANCE = 70;
	private final static double CUBE_OVERSHOOT = -20;

	private @Submodule DriveController drive;
	private @Submodule GrabberController grabber;

	private Point cubeLocation;

	private int timeWithPrism;

	public PickUpCube(Point location)
	{
		cubeLocation = location;
	}

	@Override
	protected void initialize()
	{
		grabber.setLeftArm(Position.contracted);
		grabber.setRightArm(Position.contracted);
		timeWithPrism = 0;
		
		drive.setOvershootTarget(cubeLocation, CUBE_OVERSHOOT, false);
	}

	@Override
	protected void execute()
	{
		if (grabber.hasPrism())
		{
			++timeWithPrism;
			grabber.setWheels(0, 0);
			drive.removeTarget();
			grabber.setLeftArm(Position.contracted);
			grabber.setRightArm(Position.contracted);
		} else
		{
			timeWithPrism = Math.max(0, timeWithPrism - TIME_WITH_PRISM_DECAY_RATE);
			grabber.setWheels(-1, 0);

			double dist = drive.getDistanceToTargetPoint();

			if (dist < GIVE_UP_DISTANCE)
				drive.removeTarget();
			else
			{
				drive.setOvershootTarget(cubeLocation, CUBE_OVERSHOOT, false);

				if (dist < PICK_UP_DISTANCE)
				{
					grabber.setLeftArm(Position.contracted);
					grabber.setRightArm(Position.contracted);
				}
				else if (dist < OPEN_DISTANCE)
				{
					grabber.setLeftArm(Position.middle);
					grabber.setRightArm(Position.middle);
					grabber.setWheels(-1, 0);
				}
				else
				{
					grabber.setLeftArm(Position.contracted);
					grabber.setRightArm(Position.contracted);
					grabber.setWheels(0, 0);
				}
			}
		}
	}

	@Override
	protected boolean isFinished()
	{
		return timeWithPrism > MIN_TIME_WITH_PRISM;
	}

	@Override
	protected void end()
	{
		drive.removeTarget();
	}
}
