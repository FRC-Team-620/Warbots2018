package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.Grabber;
import org.jmhsrobotics.hardwareinterface.GrabberPneumatics;
import org.jmhsrobotics.hardwareinterface.GrabberWheels;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder.BooleanConsumer;

public class GrabberController implements Module, Grabber
{
	private @Submodule GrabberWheels wheels;
	private @Submodule GrabberPneumatics pneumatics;
	
	private Position currentLeftPosition, currentRightPosition;

	@Override
	public void setLeftArm(Position position)
	{
		if (currentLeftPosition == position) return;

		currentLeftPosition = position;
		
		setArm(position, pneumatics::setLateralLeftEncoderExtended, pneumatics::setVerticalLeftEncoderExtended);
	}
	
	@Override
	public void setRightArm(Position position)
	{
		if (currentRightPosition == position) return;

		currentRightPosition = position;
		
		setArm(position, pneumatics::setLateralRightEncoderExtended, pneumatics::setVerticalRightEncoderExtended);
	}
	
	private void setArm(Position position, BooleanConsumer setLateral, BooleanConsumer setVertical)
	{
		switch(position)
		{
			case contracted:
				setLateral.accept(true);
				setVertical.accept(true);
				break;
			case extended:
				setLateral.accept(false);
				setVertical.accept(true);
				break;
			case raised:
				setLateral.accept(false);
				setVertical.accept(false);
				break;
			default:
				throw new RuntimeException("Unrecognised arm position: " + position);
		}
	}

	@Override
	public void spinLeftWheels(double speed)
	{
		wheels.setLeftWheels(speed);
	}

	@Override
	public void spinRightWheels(double speed)
	{
		wheels.setRightWheels(speed);
	}

	@Override
	public Position getLeftArmPosition()
	{
		return currentLeftPosition;
	}

	@Override
	public Position getRightArmPosition()
	{
		return currentRightPosition;
	}

	@Override
	public Command getTest()
	{
		return new InstantCommand()
		{
			@Override
			protected void initialize()
			{
				System.out.println("No test comand for grabber yet"); //TODO Add test command for grabber
			}
		};
	}
}
