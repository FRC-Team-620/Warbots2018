package org.jmhsrobotics.mockhardware;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.util.PlainSendable;
import org.jmhsrobotics.hardwareinterface.DriveMechanism;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class MockDrive extends PlainSendable implements Module, DriveMechanism
{
	private double speed, turn;
	
	@Override
	public void initSendable(SendableBuilder builder)
	{
		
	}

	@Override
	@SuppressWarnings("hiding")
	public void drive(double speed, double turn)
	{
		if(this.speed == speed && this.turn == turn)
			return;
		
		System.out.println("Driving " + speed + " " + turn);
		this.speed = speed;
		this.turn = turn;
	}

	@Override
	public Command getTest()
	{
		return null;
	}

}
