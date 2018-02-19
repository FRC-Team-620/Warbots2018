package org.jmhsrobotics.modules;

import java.util.Optional;

import org.jmhsrobotics.core.modules.SubsystemManager;
import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.GrabberController;
import org.jmhsrobotics.hardwareinterface.GrabberPneumatics;
import org.jmhsrobotics.hardwareinterface.GrabberWheels;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class GrabberControlCommand extends CommandModule implements GrabberController
{
	private @Submodule Optional<SubsystemManager> subsystems;
	private @Submodule GrabberWheels wheels;
	private @Submodule GrabberPneumatics pneumatics;
	
	private boolean pistonSideLeft, pistonSideRight, pistonTopLeft = true, pistonTopRight = true;
	private double leftWheelSpeed, rightWheelSpeed;
	
	public GrabberControlCommand()
	{
		subsystems.ifPresent(sm -> requires(sm.getSubsystem("Grabber")));
	}
	
	@Override
	protected void execute()
	{
		pneumatics.setLeftWristExtended(pistonSideLeft);
		pneumatics.setRightWristExtended(pistonSideRight);
		pneumatics.setLeftArmExtended(pistonTopLeft);
		pneumatics.setRightArmExtended(pistonTopRight);
		
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
		pistonSideLeft = isLateralPistonExtended(position);
		pistonTopLeft = isVerticalPistonExtended(position);
	}
	
	@Override
	public void setRightArm(Position position)
	{
		pistonSideRight = isLateralPistonExtended(position);
		pistonTopRight = isVerticalPistonExtended(position);
	}
	
	private static boolean isVerticalPistonExtended(Position position)
	{
		return position != Position.raised;
	}
	
	private static boolean isLateralPistonExtended(Position position)
	{
		return position == Position.contracted;
	}
	
	@Override
	public Position getLeftArmPosition()
	{
		return getPosition(pistonSideLeft, pistonTopLeft);
	}

	@Override
	public Position getRightArmPosition()
	{
		return getPosition(pistonSideRight, pistonTopRight);
	}
	
	private static Position getPosition(boolean lateralPistonExtended, boolean verticalPistonExtended)
	{
		if(lateralPistonExtended)
			if(verticalPistonExtended)
				return Position.contracted;
			else
				return null;
		else
			if(verticalPistonExtended)
				return Position.extended;
			else
				return Position.raised;
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
