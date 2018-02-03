package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.DriveMechanism;
import org.jmhsrobotics.hardwareinterface.WheelEncodersInterface;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CalibrateDriveTrain implements Module, DriveMechanism
{
	private @Submodule DriveMechanism driveTrain;
	private @Submodule WheelEncodersInterface encoders;
	
	@Override
	public void drive(double speed, double turn){
		double minSpeed = SmartDashboard.getNumber("min speed", .3);
		double speedCurve = SmartDashboard.getNumber("speed curve", 1);
		double minTurn = SmartDashboard.getNumber("min turn", .3);
		double turnCurve = SmartDashboard.getNumber("turn curve", 1);
		speed = Math.signum(speed) * (Math.pow(Math.abs(speed), speedCurve) * (1 - minSpeed) + minSpeed);
		turn = Math.signum(turn) * (Math.pow(Math.abs(turn), turnCurve) * (1 - minTurn) + minTurn);
		driveTrain.drive(speed, turn);
	}
	
	@Override
	public Command getTest()
	{
		return new InstantCommand(); //TODO: add test command
	}
	
//	
//	@Override
//	protected void initialize()
//	{
//	}
//	
//	static double rightOut = 0.5;
//	static double leftOut = 0.5;
//	static double turn = 0;
//	
//	@Override
//	protected void execute()
//	{
//		double leftSpeed = encoders.left().getRate(); //get speed of left pid
//		double rightSpeed = encoders.right().getRate(); //Get speed of right pid
//		double diffSpeed = encoders.diff().getRate(); //Get the difference in speed for the pids
//		System.out.println(leftSpeed + " " + rightSpeed + " " + diffSpeed);
//	}
//	
//	@Override
//	protected boolean isFinished()
//	{
//		return false;
//	}
}
