package org.jmhsrobotics.mockhardware;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.util.PlainSendable;
import org.jmhsrobotics.hardwareinterface.DriveMechanism;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class MockDrive extends PlainSendable implements Module, DriveMechanism
{
	@Override
	public void initSendable(SendableBuilder builder)
	{
		
	}

	@Override
	public void drive(double speed, double turn)
	{
//		System.out.println("Driving " + speed + " " + turn);
	}

	@Override
	public Command getTest()
	{
		return null;
	}

}
