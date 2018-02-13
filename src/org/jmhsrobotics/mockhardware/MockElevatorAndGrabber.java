package org.jmhsrobotics.mockhardware;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.hardwareinterface.Grabber;
import org.jmhsrobotics.hardwareinterface.HybridLifter;
import org.jmhsrobotics.hardwareinterface.TurnTable;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class MockElevatorAndGrabber implements Module, Grabber, TurnTable, HybridLifter
{
	private HybridLifter.Position lifterPosition;
	private TurnTable.Position turnTablePosition;
	private Grabber.Position leftArmPosition;
	private Grabber.Position rightArmPosition;
	
	@Override
	public void goTo(HybridLifter.Position position)
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
	public void goTo(TurnTable.Position position)
	{
		System.out.println("Turning turntable to " + position);
		turnTablePosition = position;
	}

	@Override
	public void setLeftArm(Grabber.Position position)
	{
		System.out.println("Moving grabber left arm to " + position);
		leftArmPosition = position;
	}

	@Override
	public void setRightArm(Grabber.Position position)
	{
		System.out.println("Moving grabber right arm to " + position);
		rightArmPosition = position;
	}

	@Override
	public void spinLeftWheels(double speed)
	{
		System.out.println("Spinning left grabber wheels at " + speed);
	}

	@Override
	public void spinRightWheels(double speed)
	{
		System.out.println("Spinning right grabber wheels at " + speed);
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
	public Grabber.Position getLeftArmPosition()
	{
		return leftArmPosition;
	}

	@Override
	public Grabber.Position getRightArmPosition()
	{
		return rightArmPosition;
	}

	@Override
	public HybridLifter.Position getCurrentLifterPosition()
	{
		return lifterPosition;
	}

	@Override
	public TurnTable.Position getCurrentTurnTablePosition()
	{
		return turnTablePosition;
	}
}