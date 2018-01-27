package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.modulesystem.annotations.HardwareModule;
import org.jmhsrobotics.hardwareinterface.Drive;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

@HardwareModule
public class DriveTrain implements Drive, Module
{
	private DifferentialDrive drive;

	public DriveTrain()
	{
		SpeedController left = new SpeedControllerGroup(new Spark(0), new Spark(2));
		left.setInverted(true);
		SpeedController right = new SpeedControllerGroup(new Spark(1), new Spark(3));
		right.setInverted(true);

		drive = new DifferentialDrive(left, right);
	}

	@Override
	public Command getTest()
	{
		return Drive.makeTest(this);
	}

	@Override
	public void drive(double speed, double turn)
	{
		drive.arcadeDrive(speed, turn);
	}
}