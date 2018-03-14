package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;
import org.jmhsrobotics.hardwareinterface.DriveController;
import org.jmhsrobotics.hardwareinterface.GrabberController;
import org.jmhsrobotics.hardwareinterface.GrabberController.Position;

public class PickUpCube extends PathNode
{
	private final static int MIN_TIME_WITH_PRISM = 10;
	private final static int TIME_WITH_PRISM_DECAY_RATE = 2;
	private final static int GIVE_UP_TIME = 150;
	private final static int PICK_UP_DISTANCE = 20;
	private final static int GIVE_UP_DISTANCE = 8;
	private final static int OPEN_DISTANCE = 50;
	private final static int REVERSE_BUFFER = 8;
	private final static int INITIAL_TRIES = 3;
	private final static int POSITION_OFFSET_COEFFICIENT = 6;
	
	private @Submodule DriveController drive;
	private @Submodule GrabberController grabber;
	
	private Point cubeLocation;
	
	private boolean movingTowardTarget;
	private int timeAtTarget;
	private int timeWithPrism;
	private int tries;
	
	public PickUpCube(Point location)
	{
		cubeLocation = location;
	}
	
	@Override
	protected void initialize()
	{
		drive.setTarget(cubeLocation);
		movingTowardTarget = true;
		tries = 0;
		grabber.setLeftArm(Position.contracted);
		grabber.setRightArm(Position.contracted);
		timeWithPrism = 0;
	}
	
	@Override
	protected void execute()
	{
		if (movingTowardTarget)
		{
			if (drive.getDistanceToTargetPoint() < PICK_UP_DISTANCE)
			{
				++timeAtTarget;
				grabber.setLeftArm(Position.contracted);
				grabber.setRightArm(Position.contracted);
				
				if(grabber.hasPrism())
				{
					++timeWithPrism;
					grabber.setWheels(0, 0);
				}
				else 
				{
					timeWithPrism = Math.max(0, timeWithPrism - TIME_WITH_PRISM_DECAY_RATE);
					grabber.setWheels(-1, 0);
				}
				
				if (drive.getDistanceToTargetPoint() < GIVE_UP_DISTANCE)
				{
					drive.drive(0, 0);
					timeAtTarget = GIVE_UP_TIME;
				}
				
				if(timeAtTarget > GIVE_UP_TIME && timeWithPrism == 0)
				{
					timeAtTarget = 0;
					timeWithPrism = 0;
					movingTowardTarget = false;
					drive.setRelativeTarget(new Point(40, drive.getTargetAngle().plus(Angle.REVERSE)), true);
					++tries;
				}
			}
			else if (drive.getDistanceToTargetPoint() < OPEN_DISTANCE)
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
		else
		{
			grabber.setLeftArm(Position.middle);
			grabber.setRightArm(Position.middle);
			grabber.setWheels(0, 0);
			
			if(drive.getDistanceToTargetPoint() < REVERSE_BUFFER)
			{
				movingTowardTarget = true;
				
				if(tries <= INITIAL_TRIES)
					drive.setTarget(cubeLocation);
				else
				{
					int dx = 0, dy = 0;
					int tryCycle = (tries - INITIAL_TRIES) % 4;
					int tryMagnitude = (int) Math.pow(2, (tries - INITIAL_TRIES - 1) / 4);
					if(tryCycle % 4 < 2)
						if(tryCycle % 2 == 0)
							dx = POSITION_OFFSET_COEFFICIENT * tryMagnitude;
						else
							dx = -POSITION_OFFSET_COEFFICIENT * tryMagnitude;
					else
						if(tryCycle % 2 == 0)
							dy = POSITION_OFFSET_COEFFICIENT * tryMagnitude;
						else
							dy = -POSITION_OFFSET_COEFFICIENT * tryMagnitude;
					drive.setTarget(cubeLocation.plus(new Point(dx, dy)));
				}
			}
		}
	}
	
	@Override
	protected boolean isFinished()
	{
		return timeWithPrism > MIN_TIME_WITH_PRISM;
	}
}
