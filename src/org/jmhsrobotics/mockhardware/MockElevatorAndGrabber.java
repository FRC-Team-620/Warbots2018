package org.jmhsrobotics.mockhardware;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.hardwareinterface.GrabberController;
import org.jmhsrobotics.hardwareinterface.ElevatorController;
import org.jmhsrobotics.hardwareinterface.TurnTableController;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class MockElevatorAndGrabber implements Module, GrabberController, TurnTableController, ElevatorController
{
	private ElevatorController.Position lifterPosition;
	private TurnTableController.Position turnTablePosition;
	private GrabberController.Position leftArmPosition;
	private GrabberController.Position rightArmPosition;
	
	@Override
	public void goTo(ElevatorController.Position position)
	{
		System.out.println("Moving lifter to " + position);
		lifterPosition = position;
	}

	@Override
	public void goToRaw(double linearHeight, boolean raisePneumatics)
	{
		System.out.println("Moving linear actuator to " + linearHeight + " and " + (raisePneumatics ? "raising" : "lowering") + " pneumatics.");
	}

	@Override
	public void driveManual(double linearSpeed, boolean raisePneumatics)
	{
		System.out.println("Driving linear actuator at " + linearSpeed + " and " + (raisePneumatics ? "raising" : "lowering") + " pneumatics.");
	}

	@Override
	public void climbFullPower()
	{
		System.out.println("Lowering penumatic lift with both pistons");
	}

	@Override
	public void goTo(TurnTableController.Position position)
	{
		System.out.println("Turning turntable to " + position);
		turnTablePosition = position;
	}

	@Override
	public void setLeftArm(GrabberController.Position position)
	{
		System.out.println("Moving grabber left arm to " + position);
		leftArmPosition = position;
	}

	@Override
	public void setRightArm(GrabberController.Position position)
	{
		System.out.println("Moving grabber right arm to " + position);
		rightArmPosition = position;
	}

	@Override
	public void spinLeftWheels(double speed)
	{
//		System.out.println("Spinning left grabber wheels at " + speed);
	}

	@Override
	public void spinRightWheels(double speed)
	{
//		System.out.println("Spinning right grabber wheels at " + speed);
	}

	@Override
	public Command getTest()
	{
		return new InstantCommand()
		{
			@Override
			protected void initialize()
			{
				System.out.println("Testing mock Elevator, Turntable, and Grabber");
			}
		};
	}

	@Override
	public GrabberController.Position getLeftArmPosition()
	{
		return leftArmPosition;
	}

	@Override
	public GrabberController.Position getRightArmPosition()
	{
		return rightArmPosition;
	}

	@Override
	public ElevatorController.Position getCurrentLifterPosition()
	{
		return lifterPosition;
	}

	@Override
	public TurnTableController.Position getCurrentTurnTablePosition()
	{
		return turnTablePosition;
	}
}