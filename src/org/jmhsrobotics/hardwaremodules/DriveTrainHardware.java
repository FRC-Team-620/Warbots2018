package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.modulesystem.annotations.HardwareModule;
import org.jmhsrobotics.core.util.PlainSendable;
import org.jmhsrobotics.hardwareinterface.DriveMechanism;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

@HardwareModule
public class DriveTrainHardware extends PlainSendable implements DriveMechanism, Module
{
	private DifferentialDrive drive;
	
	public DriveTrainHardware(int leftPort1, int leftPort2, int rightPort1, int rightPort2)
	{
		WPI_TalonSRX left1 = new WPI_TalonSRX(leftPort1);
		WPI_TalonSRX left2 = new WPI_TalonSRX(leftPort2);
		WPI_TalonSRX right1 = new WPI_TalonSRX(rightPort1);
		WPI_TalonSRX right2 = new WPI_TalonSRX(rightPort2);
		
		SpeedController left = new SpeedControllerGroup(left1, left2);
		left.setInverted(true);
		SpeedController right = new SpeedControllerGroup(right1, right2);
		right.setInverted(true);
		
		drive = new DifferentialDrive(left, right);
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
	
	@Override
	public Command getTest()
	{
		return DriveMechanism.makeTest(this);
	}
}
