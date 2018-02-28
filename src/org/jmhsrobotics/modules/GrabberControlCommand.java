package org.jmhsrobotics.modules;

import java.util.Optional;

import org.jmhsrobotics.core.modules.SubsystemManager;
import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.PerpetualCommand;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.GrabberController;
import org.jmhsrobotics.hardwareinterface.GrabberPneumatics;
import org.jmhsrobotics.hardwareinterface.GrabberWheels;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class GrabberControlCommand extends CommandModule implements GrabberController, PerpetualCommand
{
	private @Submodule Optional<SubsystemManager> subsystems;
	private @Submodule GrabberWheels wheels;
	private @Submodule GrabberPneumatics pneumatics;
	
	private boolean la, lw, ra, rw, raise;
	private double leftWheelSpeed, rightWheelSpeed;
	
	@Override
	public void reset()
	{
	}
	
	@Override
	protected void execute()
	{
		pneumatics.setLeftWristContracted(lw);
		pneumatics.setRightWristContracted(rw);
		pneumatics.setLeftArmContracted(la);
		pneumatics.setRightArmContracted(ra);
		pneumatics.setRaised(raise);
		
		wheels.setLeftWheels(leftWheelSpeed);
		wheels.setRightWheels(rightWheelSpeed);
	}
	
	@Override
	protected boolean isFinished()
	{
		return false;
	}
	
	@Override
	public void setLeftArm(Position position)
	{
		la = isArmContracted(position);
		lw = isWristContracted(position);
	}
	
	@Override
	public void setRightArm(Position position)
	{
		ra = isArmContracted(position);
		rw = isWristContracted(position);
	}
	
	private static boolean isWristContracted(Position position)
	{
		return position == Position.contracted;
	}
	
	private static boolean isArmContracted(Position position)
	{
		return position != Position.extended;
	}
	
	@Override
	public Position getLeftArmPosition()
	{
		return getPosition(la, lw);
	}

	@Override
	public Position getRightArmPosition()
	{
		return getPosition(ra, rw);
	}
	
	public void setRaised(boolean raised)
	{
		raise = raised;
	}
	
	public boolean isRaised()
	{
		return raise;
	}
	
	private static Position getPosition(boolean arm, boolean wrist)
	{
		if(arm)
			if(wrist)
				return Position.extended;
			else
				return Position.middle;
		else
			if(wrist)
				return Position.middle;
			else
				return Position.contracted;
	}
	
	@Override
	public void spinLeftWheels(double speed)
	{
		leftWheelSpeed = speed;
	}

	@Override
	public void spinRightWheels(double speed)
	{
		rightWheelSpeed = speed;
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
