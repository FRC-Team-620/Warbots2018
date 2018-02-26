package org.jmhsrobotics.mockhardware;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.hardwareinterface.ElevatorController;

import edu.wpi.first.wpilibj.command.Command;

public class MockElevator implements Module, ElevatorController
{
	private Position pos;
	
	@Override
	public void start()
	{
		System.out.println("Starting elevator control command");
	}

	@Override
	public void cancel()
	{
		System.out.println("Stopping elevator control command");
	}

	@Override
	public void goTo(Position position)
	{
		System.out.println("Moving elevator to " + position);
		pos = position;
	}

	@Override
	public Position getCurrentLifterPosition()
	{
		return pos;
	}

	@Override
	public void goToRaw(double linearHeight, boolean raisePneumatics)
	{
		System.out.println("Moving elevator to raw position " + linearHeight + " with pneumatics " + (raisePneumatics ? "raised." : "lowered."));
	}

	@Override
	public void driveManual(double linearSpeed, boolean raisePneumatics)
	{
		System.out.println("Driving elevator at speed " + linearSpeed + " with pneumatics " + (raisePneumatics ? "raised." : "lowered."));
	}

	@Override
	public void climbFullPower()
	{
		System.out.println("Climbing");
	}

	@Override
	public Command getTest()
	{
		return null;
	}
}
