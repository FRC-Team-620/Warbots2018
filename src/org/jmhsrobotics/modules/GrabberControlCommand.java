package org.jmhsrobotics.modules;

import java.util.Optional;

import org.jmhsrobotics.core.modules.SubsystemManager;
import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.PerpetualCommand;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.RobotMath;
import org.jmhsrobotics.hardwareinterface.GrabberController;
import org.jmhsrobotics.hardwareinterface.GrabberPneumatics;
import org.jmhsrobotics.hardwareinterface.GrabberWheels;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class GrabberControlCommand extends CommandModule implements GrabberController, PerpetualCommand
{
	private final static int INJECTION_TIMEOUT = (int)(1.5 * 50);
	private final static int TIME_TO_DROP_GRABBER = (int)(0.5 * 50);
	private final static int TIME_TO_EJECT_CUBE = (int)(1 * 50);

	private @Submodule Optional<SubsystemManager> subsystems;
	private @Submodule GrabberWheels wheels;
	private @Submodule GrabberPneumatics pneumatics;

	private boolean la, lw, ra, rw, raise;
	private double leftWheelSpeed, rightWheelSpeed;

	private int injectionTimer;

	@Override
	public void reset()
	{
		cancelIntake();
	}
	
	@Override
	protected void execute()
	{
		if (injectionTimer > -1)
		{
			++injectionTimer;
			setWheels(-1, 0);
			
			if (injectionTimer > INJECTION_TIMEOUT)
				cancelIntake();
			
			if (wheels.hasPrism())
				cancelIntake();
		}

		pneumatics.setRaised(raise);
		pneumatics.setLeftWristContracted(lw);
		pneumatics.setRightWristContracted(rw);
		pneumatics.setLeftArmContracted(la);
		pneumatics.setRightArmContracted(ra);
		
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
		return position == Position.extended;
	}

	private static boolean isArmContracted(Position position)
	{
		return position != Position.contracted;
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

	@Override
	public void setRaised(boolean raised)
	{
		raise = raised;
	}

	@Override
	public boolean isRaised()
	{
		return raise;
	}
	
	@Override
	public boolean hasPrism()
	{
		return wheels.hasPrism();
	}

	private static Position getPosition(boolean arm, boolean wrist)
	{
		if (arm)
			if (wrist)
				return Position.extended;
			else return Position.middle;
		else if (wrist)
			return Position.middle;
		else 
			return Position.contracted;
	}
	
	@Override
	public void spinWheels(double speed, double jank)
	{
		if(injectionTimer > -1)
			if(speed == 0 && jank == 0)
				return;
		
		setWheels(speed, jank);
	}

	private void setWheels(double speed, double jank)
	{
		if(jank == 0)
			leftWheelSpeed = rightWheelSpeed = speed;
		else
		{
			leftWheelSpeed = getSideComponent(speed, jank);
			rightWheelSpeed = getSideComponent(speed, -jank);
		}
	}
	
	private double getSideComponent(double speed, double turn)
	{
		return RobotMath.curve(RobotMath.curve(speed, 2) + RobotMath.curve(turn, 2), .5);
	}
	
	@Override
	public void intake()
	{
		if(injectionTimer > -1)
			return;
		
		System.out.println("Intaking");
		
		setWheels(-1, 0);
		injectionTimer = 0;
	}

	private void cancelIntake()
	{
		if(injectionTimer == -1)
			return;
		
		System.out.println("cancelling intake");
		injectionTimer = -1;
		leftWheelSpeed = rightWheelSpeed = 0;
	}

	@Override
	public void cancelAutomaticMovement()
	{
		cancelIntake();
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
