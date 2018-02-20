package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.PlainSendable;
import org.jmhsrobotics.core.util.RobotMath;
import org.jmhsrobotics.hardwareinterface.DriveMechanism;
import org.jmhsrobotics.hardwareinterface.WheelEncoders;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class CalibrateDriveTrain extends PlainSendable implements Module, DriveMechanism
{
	private @Submodule DriveMechanism driveTrain;
	private @Submodule WheelEncoders encoders;
	// Setting the Jump Mins/Maxes to 0
	double jumpMinSpeed = -.25;
	double jumpMaxSpeed = .25;
	double jumpMinTurn = -.25;
	double jumpMaxTurn = .25;
	double maxSpeed = 0;
	//4237.077843122658  test 1
	// 4097.239933834993 test 2

	@Override
	public void drive(double speed, double turn)
	{
		speed = RobotMath.yKinkedMap(speed, -1, 1, 0, jumpMinSpeed, jumpMaxSpeed, -1, 1);
		turn = RobotMath.yKinkedMap(turn, -1, 1, 0, jumpMinTurn, jumpMaxTurn, -1, 1);
		driveTrain.drive(speed, turn);
		if(encoders.average().getRate() > maxSpeed) {
			maxSpeed = encoders.average().getRate();
		}
	}

	@Override
	public Command getTest()	
	{
		return new InstantCommand(); // TODO: add test command
	}

	@Override
	public void initSendable(SendableBuilder builder)
	{
	}
}
