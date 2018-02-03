package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.modulesystem.annotations.HardwareModule;
import org.jmhsrobotics.hardwareinterface.DriveMechanism;
import org.jmhsrobotics.modules.CalibrateDriveTrain;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

@HardwareModule
public class DriveTrain implements DriveMechanism, Module
{
	private DifferentialDrive drive;
	
	public DriveTrain()
	{
		SpeedController left = new SpeedControllerGroup(new Spark(0), new Spark(2));
		left.setInverted(true);
		SpeedController right = new SpeedControllerGroup(new Spark(1), new Spark(3));
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
		double minSpeed = SmartDashboard.getNumber("min speed", .3);
		double speedCurve = SmartDashboard.getNumber("speed curve", 1);
		double minTurn = SmartDashboard.getNumber("min turn", .3);
		double turnCurve = SmartDashboard.getNumber("turn curve", 1);
		
//		speed = Math.signum(speed) * (Math.pow(Math.abs(speed), speedCurve) * (1 - minSpeed) + minSpeed);
//		turn = Math.signum(turn) * (Math.pow(Math.abs(turn), turnCurve) * (1 - minTurn) + minTurn);
		System.out.println("Speed: " + speed + " Turn: " + turn);
		drive.arcadeDrive(speed, turn);
	}
}