package org.jmhsrobotics.modules.teleop;

import org.jmhsrobotics.core.modulesystem.ControlScheme;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.GrabberController;
import org.jmhsrobotics.hardwareinterface.GrabberController.Position;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class ControlGrabberWithXbox extends ControlScheme
{
	private final static int AUTO_GRAB_TIME = 10;
	private final static int AUTO_GRAB_HOLD_TIME = 5;
	private final static int AUTO_GRAB_DECAY_RATE = 2;
	private final static double[] TRIGGER_ARM_THRESHOLDS = { 0.2, 0.8 };

	private @Submodule GrabberController grabber;

	private XboxController xbox;
	private Hand side;

	private int hasPrismTimer;

	public ControlGrabberWithXbox(XboxController xbox, Hand side)
	{
		this.xbox = xbox;
		this.side = side;
	}

	@Override
	protected void initialize()
	{
		hasPrismTimer = 0;
	}

	@Override
	protected void execute()
	{
		double wheelSpeed = -deadZone(xbox.getY(side), .2, .1);
		double wheelJank = deadZone(xbox.getX(side), .2, .1);

		Position left = getGrabberArmPosition(Hand.kLeft);
		Position right = getGrabberArmPosition(Hand.kRight);

		grabber.setLeftArm(left);
		grabber.setRightArm(right);
		grabber.setWheels(wheelSpeed, wheelJank);
		
//		System.out.println(hasPrismTimer);
//		
//		if (open())
//			if (grabber.hasPrism())
//			{
//				if (hasPrismTimer < AUTO_GRAB_TIME + AUTO_GRAB_HOLD_TIME)
//					++hasPrismTimer;
//			}
//			else
//				hasPrismTimer = Math.max(hasPrismTimer - AUTO_GRAB_DECAY_RATE, 0);
//		else if (grabber.hasPrism())
//			hasPrismTimer = AUTO_GRAB_TIME + AUTO_GRAB_HOLD_TIME;
//		else
//			hasPrismTimer = 0;
//
//		if (hasPrismTimer > AUTO_GRAB_TIME && hasPrismTimer < AUTO_GRAB_TIME + AUTO_GRAB_HOLD_TIME)
//		{
//			grabber.setLeftArm(Position.contracted);
//			grabber.setRightArm(Position.contracted);
//
//			if (RobotMath.oneNonZero(wheelSpeed, wheelJank))
//			{
//				hasPrismTimer = AUTO_GRAB_TIME + AUTO_GRAB_HOLD_TIME;
//				grabber.setWheels(wheelSpeed, wheelJank);
//			}
//		}
//		else if (armsAreClosing(left, right))
//			grabber.intake();
//		else
//		{
//			grabber.setLeftArm(left);
//			grabber.setRightArm(right);
//
//			if (open())
//				if (RobotMath.oneNonZero(wheelSpeed, wheelJank))
//					grabber.setWheels(wheelSpeed, wheelJank);
//				else
//					grabber.setWheels(-1, 0);
//			else if (hasPrismTimer >= AUTO_GRAB_TIME + AUTO_GRAB_HOLD_TIME)
//				hasPrismTimer = 0;
//		}
//
//		if (xbox.getBackButton())
//			grabber.cancelAutomaticMovement();
	}

	private Position getGrabberArmPosition(Hand hand)
	{
		return getGrabberArmPosition(xbox.getTriggerAxis(hand));
	}

	private static Position getGrabberArmPosition(double axis)
	{
		int posNum = 0;
		while (posNum < TRIGGER_ARM_THRESHOLDS.length && axis > TRIGGER_ARM_THRESHOLDS[posNum])
			++posNum;

		return Position.values()[posNum];
	}

	private boolean armsAreClosing(Position newLeftArmPosition, Position newRightArmPosition)
	{
		if (newLeftArmPosition != Position.contracted)
			return false;

		if (newRightArmPosition != Position.contracted)
			return false;

		return open();
	}

	private boolean open()
	{
		if (grabber.getLeftArmPosition() != Position.contracted)
			return true;

		if (grabber.getRightArmPosition() != Position.contracted)
			return true;

		return false;
	}
}
