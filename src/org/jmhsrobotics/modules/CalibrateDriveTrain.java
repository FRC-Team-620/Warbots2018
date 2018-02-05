package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.RobotMath;
import org.jmhsrobotics.hardwareinterface.DriveMechanism;
import org.jmhsrobotics.hardwareinterface.WheelEncodersInterface;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CalibrateDriveTrain implements Module, DriveMechanism
{
	private @Submodule DriveMechanism driveTrain;
	private @Submodule WheelEncodersInterface encoders;
	//Setting the Jump Mins/Maxes to 0
	double jumpMinSpeed = -.3;
	double jumpMaxSpeed = .3;
	double jumpMinTurn = -.33;
	double jumpMaxTurn = .33;

	@Override
	public void drive(double speed, double turn)
	{
		speed = RobotMath.yKinkedMap(speed, -1, 1, 0, jumpMinSpeed, jumpMaxSpeed, -1, 1);
		turn = RobotMath.yKinkedMap(turn, -1, 1, 0, jumpMinTurn, jumpMaxTurn, -1, 1);
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
