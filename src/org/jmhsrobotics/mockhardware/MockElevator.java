package org.jmhsrobotics.mockhardware;

import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.PerpetualCommand;
import org.jmhsrobotics.hardwareinterface.ElevatorController;

import edu.wpi.first.wpilibj.command.Command;

public class MockElevator extends CommandModule implements PerpetualCommand, ElevatorController
{
	private Position pos;

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
	public void reset()
	{
		System.out.println("Resetting mock elevator control command");
	}
	
	@Override
	protected void initialize()
	{
		System.out.println("Starting mock elevator control command");
	}
	
	@Override
	protected void end()
	{
		System.out.println("Closing mock elevator control command");
	}
	
	@Override
	protected boolean isFinished()
	{
		return false;
	}
	
	@Override
	public Command getTest()
	{
		return null;
	}
}