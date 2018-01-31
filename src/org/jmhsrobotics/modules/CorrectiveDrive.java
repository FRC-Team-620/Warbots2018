package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.Drive;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class CorrectiveDrive implements Module, Drive
{
	private @Submodule Drive driveTrain;
	private @Submodule Gyro gyro;
	
	@Override
	public void drive(double speed, double turn)
	{
	}

	@Override
	public Command getTest()
	{
		return Drive.makeTest(this);
	}
}
