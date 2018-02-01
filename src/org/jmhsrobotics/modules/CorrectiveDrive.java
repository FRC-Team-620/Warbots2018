package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.modulesystem.DriveController;
import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.DriveMechanism;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class CorrectiveDrive implements Module, DriveMechanism
{
	private @Submodule DriveController driveTrain;
	private @Submodule Gyro gyro;
	
	@Override
	public void drive(double speed, double turn)
	{
		driveTrain.drive(speed, turn);
	}

	@Override
	public Command getTest()
	{
		return DriveMechanism.makeTest(this);
	}
}
