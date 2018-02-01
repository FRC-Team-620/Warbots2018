package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.core.modulesystem.DriveController;

import edu.wpi.first.wpilibj.command.Command;

public class CorrectiveDrive extends DriveController
{
	@Override
	public void drive(double speed, double turn)
	{
		driveRaw(speed, turn);
	}
}
