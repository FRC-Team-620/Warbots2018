package org.jmhsrobotics.modules.teleop;

import org.jmhsrobotics.core.modulesystem.ControlScheme;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.RobotMath;
import org.jmhsrobotics.hardwareinterface.GrabberController;
import org.jmhsrobotics.hardwareinterface.GrabberController.Position;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class ControlGrabberWithXbox extends ControlScheme
{
	private final static int AUTO_GRAB_TIME = 10;
	private final static int AUTO_GRAB_HOLD_TIME = 200;
	private final static int AUTO_GRAB_DECAY_RATE = 2;
	private final static int DROP_TIME = 50;
	private final static int INITIAL_EJECTION_TIME = 5;
	private final static double[] TRIGGER_ARM_THRESHOLDS = { 0.01, 0.99 };

	private @Submodule GrabberController grabber;

	private XboxController xbox;
	private Hand joystickSide;
	private Hand ejectSide;

	private int hasPrismTimer;

	public ControlGrabberWithXbox(XboxController xbox, Hand joystickSide, Hand ejectSide)
	{
		this.xbox = xbox;
		this.joystickSide = joystickSide;
		this.ejectSide = ejectSide;
	}

	@Override
	protected void initialize()
	{
		hasPrismTimer = 0;
	}

	@Override
	protected void execute()
	{
		double wheelSpeed, wheelJank;
		if(joystickSide != null)
		{
			wheelSpeed = -deadZone(xbox.getY(joystickSide), .2, .1);
			wheelJank = deadZone(xbox.getX(joystickSide), .2, .1);
		}
		else
		{
			wheelSpeed = wheelJank = 0;
		}

		Position left = getGrabberArmPosition(Hand.kLeft);
		Position right = getGrabberArmPosition(Hand.kRight);

		if (left != Position.contracted || right != Position.contracted)
			if (grabber.hasPrism())
			{
				if (hasPrismTimer < AUTO_GRAB_TIME + AUTO_GRAB_HOLD_TIME)
					++hasPrismTimer;
				else
					hasPrismTimer = AUTO_GRAB_TIME - DROP_TIME;
			}
			else
				hasPrismTimer = Math.max(hasPrismTimer - AUTO_GRAB_DECAY_RATE, 0);
		else if (grabber.hasPrism() && hasPrismTimer >= 0)
			hasPrismTimer = AUTO_GRAB_TIME + AUTO_GRAB_HOLD_TIME;
		else if (hasPrismTimer > 0)
		{
			grabber.intake();
			hasPrismTimer = 0;
		}

		if (hasPrismTimer > AUTO_GRAB_TIME && hasPrismTimer < AUTO_GRAB_TIME + AUTO_GRAB_HOLD_TIME)
		{
			System.out.println("Contracting");
			grabber.setLeftArm(Position.contracted);
			grabber.setRightArm(Position.contracted);

			if (RobotMath.oneNonZero(wheelSpeed, wheelJank))
			{
				hasPrismTimer = AUTO_GRAB_TIME + AUTO_GRAB_HOLD_TIME;
				grabber.setWheels(wheelSpeed, wheelJank);
			}
			else
				grabber.setWheels(0, 0);
		}
		else if (armsAreClosing(left, right))
			grabber.intake();
		else if (xbox.getBumperPressed(ejectSide))
		{
			grabber.extake();
			hasPrismTimer = -INITIAL_EJECTION_TIME;
		}
		else
		{
			grabber.setLeftArm(left);
			grabber.setRightArm(right);

			if (open())
				if (RobotMath.oneNonZero(wheelSpeed, wheelJank))
					grabber.setWheels(wheelSpeed, wheelJank);
				else if (hasPrismTimer == 0)
					grabber.setWheels(-1, 0);
				else
					grabber.setWheels(0, 0);
			else
				grabber.setWheels(wheelSpeed, wheelJank);
		}

		if(xbox.getAButtonPressed())
			grabber.setRaised(!grabber.isRaised());
		
		if (xbox.getBackButton())
			grabber.cancelAutomaticMovement();
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
