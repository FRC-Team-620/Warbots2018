package org.jmhsrobotics.modules.teleop;

import org.jmhsrobotics.core.modulesystem.ControlScheme;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.ElevatorController;
import org.jmhsrobotics.hardwareinterface.GrabberController;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class SeanControlScheme extends ControlScheme
{
	private final static double[] triggerArmThresholds = { 0.2, 1.2 };

	private @Submodule GrabberController grabber;
	private @Submodule ElevatorController elevator;

	private XboxController xbox;
	private int hasCubeTimer;

	public SeanControlScheme(XboxController xbox)
	{
		this.xbox = xbox;
	}
	
	@Override
	protected void initialize()
	{
		hasCubeTimer = 0;
	}

	@Override
	protected void execute()
	{
		final GrabberController.Position contracted = GrabberController.Position.contracted;
		
		@SuppressWarnings("unused")
		double rightStickX = deadZone(xbox.getX(Hand.kRight), .2, .1);
		double rightStickY = deadZone(xbox.getY(Hand.kRight), .2, .1);

		double wheelSpin = -deadZone(xbox.getY(Hand.kLeft), .2, .1);
		double wheelJank = deadZone(xbox.getX(Hand.kLeft), .2, .1);
		
		grabber.setWheels(wheelSpin, wheelJank);
		
		elevator.manualDrive(-rightStickY);

		boolean open = open();
		boolean hasCube = grabber.hasPrism();

		if(hasCube)
			++hasCubeTimer;
		else
			hasCubeTimer = 0;
		
		GrabberController.Position newLeftArmPosition, newRightArmPosition;
		newLeftArmPosition = getGrabberArmPosition(xbox.getTriggerAxis(Hand.kLeft));
		newRightArmPosition = getGrabberArmPosition(xbox.getTriggerAxis(Hand.kRight));
		
		if (newLeftArmPosition == contracted && newRightArmPosition == contracted)
			hasCubeTimer = -20;
		
		if(hasCubeTimer > 10)
		{
			newLeftArmPosition = contracted;
			newRightArmPosition = contracted;
		}
		
		if (hasCubeTimer >= 0 && open)
			grabber.setWheels(-1, 0);
		if (armsAreClosing(newLeftArmPosition, newRightArmPosition))
			grabber.intake();

		grabber.setLeftArm(newLeftArmPosition);
		grabber.setRightArm(newRightArmPosition);

		if (xbox.getBackButtonPressed())
		{
			grabber.cancelAutomaticMovement();
			elevator.manualDrive(0);
		}
		
		if (xbox.getAButtonPressed())
			grabber.setRaised(!grabber.isRaised());
		
		if (xbox.getYButtonPressed())
			elevator.setPneumatics(!elevator.isPneumaticsExtended());
	}

	private GrabberController.Position getGrabberArmPosition(double axis)
	{
		int posNum = 0;
		while (posNum < triggerArmThresholds.length && axis > triggerArmThresholds[posNum])
			++posNum;

		return GrabberController.Position.values()[posNum];
	}

	private boolean armsAreClosing(GrabberController.Position newLeftArm, GrabberController.Position newRightArm)
	{
		final GrabberController.Position contracted = GrabberController.Position.contracted;

		if (newLeftArm != contracted) 
			return false;

		if (newRightArm != contracted)
			return false;

		return open();
	}
	
	private boolean open()
	{
		final GrabberController.Position contracted = GrabberController.Position.contracted;
		
		if (grabber.getLeftArmPosition() != contracted)
			return true;
		
		if (grabber.getRightArmPosition() != contracted)
			return true;
		
		return false;
	}
}
