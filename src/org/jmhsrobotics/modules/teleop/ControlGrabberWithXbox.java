package org.jmhsrobotics.modules.teleop;

import org.jmhsrobotics.core.modulesystem.ControlScheme;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.RobotMath;
import org.jmhsrobotics.hardwareinterface.GrabberController;
import org.jmhsrobotics.hardwareinterface.GrabberController.Position;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.XboxController;

public class ControlGrabberWithXbox extends ControlScheme
{
	private final static int AUTO_GRAB_TIME = 10;
	private final static int AUTO_GRAB_HOLD_TIME = 200;
	private final static int AUTO_GRAB_DECAY_RATE = 2;
	private final static int DROP_TIME = 50;
	
	private final static double[] TRIGGER_ARM_THRESHOLDS = { 0.01, 0.99 };

	private @Submodule GrabberController grabber;

	private XboxController xbox;
	private Hand joystickSide;
	private Hand ejectSide;

	private boolean holding;
	
	private int pushingCubeTimer;
	private boolean overridingDriver;
	private int overrideDriverTimer;
	
	private boolean dropping;
	private int dropTimer;

	public ControlGrabberWithXbox(XboxController xbox, Hand joystickSide, Hand ejectSide)
	{
		this.xbox = xbox;
		this.joystickSide = joystickSide;
		this.ejectSide = ejectSide;
	}

	@Override
	protected void initialize()
	{
		holding = grabber.hasPrism();
		
		pushingCubeTimer = 0;
		overridingDriver = false;
		overrideDriverTimer = 0;
		dropping = false;
		dropTimer = 0;
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

		int autoWheelSpeed = 0;
		
		if (open(left, right))
		{
			if (grabber.hasPrism())
			{
				if (overridingDriver)
				{
					++overrideDriverTimer;
					if(overrideDriverTimer > AUTO_GRAB_HOLD_TIME)
					{
						overrideDriverTimer = 0;
						overridingDriver = false;
					}
				}
				else if (holding)
				{
					dropping = true;
					dropTimer = 0;
				}
				else
				{
					++pushingCubeTimer;
					if(pushingCubeTimer > AUTO_GRAB_TIME)
					{
						holding = true;
						pushingCubeTimer = 0;
						overridingDriver = true;
						overrideDriverTimer = 0;
					}
				}
			}
			else if (dropping)
			{
				holding = false;
				
				++dropTimer;
				if(dropTimer > DROP_TIME)
				{
					dropTimer = 0;
					dropping = false;
				}
			}
			else
			{
				pushingCubeTimer = Math.max(pushingCubeTimer - AUTO_GRAB_DECAY_RATE, 0);
				autoWheelSpeed = -1;
			}
		}
		else
		{
			overridingDriver = false;
			overrideDriverTimer = 0;
			dropping = false;
			dropTimer = 0;
			
			if (xbox.getBumperPressed(ejectSide))
			{
				holding = false;
				grabber.extake();
			}
			
			if (grabber.hasPrism() && !grabber.isExtaking())
				holding = true;
			else
			{
				if (holding)
					grabber.intake();
				
				holding = false;
				
				if (open())
					grabber.intake();
			}
		}
		
		if (RobotMath.oneNonZero(wheelSpeed, wheelJank))
			grabber.setWheels(wheelSpeed, wheelJank);
		else
			grabber.setWheels(autoWheelSpeed, 0);
		
		if (overridingDriver)
		{
			xbox.setRumble(RumbleType.kLeftRumble, 1);
			xbox.setRumble(RumbleType.kRightRumble, 1);
			
			grabber.setLeftArm(Position.contracted);
			grabber.setRightArm(Position.contracted);
		}
		else
		{
			xbox.setRumble(RumbleType.kLeftRumble, 0);
			xbox.setRumble(RumbleType.kRightRumble, 0);
			
			grabber.setLeftArm(left);
			grabber.setRightArm(right);
		}
		
//		System.out.println("*");
//		System.out.println("Holding: " + holding);
//		System.out.println("Pushing Cube Timer: " + pushingCubeTimer);
//		System.out.println("Overriding Driver: " + overridingDriver);
//		System.out.println("Override Driver Timer: " + overrideDriverTimer);
//		System.out.println("Dropping: " + dropping);
//		System.out.println("Drop Timer: " + dropTimer);
//		System.out.println("*");
		
		if(xbox.getAButtonPressed())
			grabber.setRaised(!grabber.isRaised());
		
		if (xbox.getBackButton())
			grabber.cancelAutomaticMovement();
	}

	private static Position getGrabberArmPosition(double axis)
	{
		int posNum = 0;
		while (posNum < TRIGGER_ARM_THRESHOLDS.length && axis > TRIGGER_ARM_THRESHOLDS[posNum])
			++posNum;

		return Position.values()[posNum];
	}
	
	private Position getGrabberArmPosition(Hand hand)
	{
		return getGrabberArmPosition(xbox.getTriggerAxis(hand));
	}

	private static boolean open(Position left, Position right)
	{
		if (left != Position.contracted)
			return true;
		
		if (right != Position.contracted)
			return true;
		
		return false;
	}
	
	private boolean open()
	{
		return open(grabber.getLeftArmPosition(), grabber.getRightArmPosition());
	}
	
	private boolean armsAreClosing(Position newLeftArmPosition, Position newRightArmPosition)
	{
		if (open(newLeftArmPosition, newRightArmPosition))
			return false;
		
		return open();
	}
}
