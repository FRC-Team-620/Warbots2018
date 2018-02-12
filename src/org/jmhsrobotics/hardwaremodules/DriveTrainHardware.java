package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.modulesystem.annotations.HardwareModule;
import org.jmhsrobotics.core.util.PlainSendable;
import org.jmhsrobotics.hardwareinterface.DriveMechanism;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

@HardwareModule
public class DriveTrainHardware extends PlainSendable implements DriveMechanism, Module
{
	private DifferentialDrive drive;
	
	public DriveTrainHardware(int leftPort1, int leftPort2, int rightPort1, int rightPort2)
	{
		SpeedController left = new SpeedControllerGroup(new Spark(leftPort1), new Spark(leftPort2));
		left.setInverted(true);
		SpeedController right = new SpeedControllerGroup(new Spark(rightPort1), new Spark(rightPort2));
		right.setInverted(true);

		drive = new DifferentialDrive(left, right);
		SmartDashboard.putNumber("min speed", .35);
		SmartDashboard.putNumber("speed curve", 1.2);
		SmartDashboard.putNumber("min turn", .35);
		SmartDashboard.putNumber("turn curve", 1.2);
	}

	@Override
	public Command getTest()
	{
		return DriveMechanism.makeTest(this);
	}

	@Override
	public void drive(double speed, double turn)
	{
		drive.arcadeDrive(speed, turn);
	}

	@Override
	public void initSendable(SendableBuilder builder)
	{
	}
}