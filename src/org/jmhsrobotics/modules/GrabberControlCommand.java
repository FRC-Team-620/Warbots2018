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
	private final static int EJECTION_TIME = (int)(.6 * 50);

	private @Submodule Optional<SubsystemManager> subsystems;
	private @Submodule GrabberWheels wheels;
	private @Submodule GrabberPneumatics pneumatics;

	private boolean la, lw, ra, rw, raise = true;
	private double wheelSpeed, wheelJank;

	private int intakeTimer;
	private int extakeTimer;

	@Override
	public void reset()
	{
		cancelIntake();
		cancelExtake();
	}
	
	@Override
	protected void execute()
	{
		if (intakeTimer > -1)
		{
			++intakeTimer;
			
			if (RobotMath.oneNonZero(wheelSpeed, wheelJank))
			{
				System.out.println("Internal cancel of intake for wheels");
				cancelIntake();
			}
			else if (intakeTimer > INJECTION_TIMEOUT)
			{
				System.out.println("Internal cancel of intake for timeout");
				cancelIntake();
			}
			else if (wheels.hasPrism())
			{
				System.out.println("Internal cancel of intake for prism");
				cancelIntake();
			}
			else
				wheels.set(-1, 0);
		}
		else if (extakeTimer > -1)
		{
			++extakeTimer;
			
			if (RobotMath.oneNonZero(wheelSpeed, wheelJank))
				cancelExtake();
			else if (extakeTimer > EJECTION_TIME)
				cancelExtake();
			else
				wheels.set(1, 0);
		}
		else
			wheels.set(wheelSpeed, wheelJank);

		pneumatics.setRaised(raise);
		pneumatics.setLeftWristContracted(lw);
		pneumatics.setRightWristContracted(rw);
		pneumatics.setLeftArmContracted(la);
		pneumatics.setRightArmContracted(ra);
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
	public void setWheels(double speed, double jank)
	{
		if(isAutomaticMovement())
			if(!RobotMath.oneNonZero(speed, jank))
				return;
		
		cancelAutomaticMovement();
		
		wheelSpeed = speed;
		wheelJank = jank;
	}
	
	@Override
	public void intake()
	{
		if(intakeTimer > -1)
			return;
		
		cancelExtake();
		
		setLeftArm(Position.contracted);
		setRightArm(Position.contracted);
		
		wheelSpeed = wheelJank = 0;
		intakeTimer = 0;
	}
	
	@Override
	public boolean isIntaking()
	{
		return intakeTimer > -1;
	}
	
	@Override
	public void extake()
	{
		if(extakeTimer > -1)
			return;
		
		cancelIntake();
		
		wheelSpeed = wheelJank = 0;
		extakeTimer = 0;
	}
	
	@Override
	public boolean isExtaking()
	{
		return extakeTimer > -1;
	}

	private void cancelIntake()
	{
		if(intakeTimer == -1)
			return;
		
		System.out.println("cancelling intake" + System.currentTimeMillis());
		
		intakeTimer = -1;
		wheelSpeed = wheelJank = 0;
	}
	
	private void cancelExtake()
	{
		if(extakeTimer == -1)
			return;
		
		extakeTimer = -1;
		wheelSpeed = wheelJank = 0;
	}

	@Override
	public void cancelAutomaticMovement()
	{
		cancelIntake();
		cancelExtake();
	}
	
	private boolean isAutomaticMovement()
	{
		return extakeTimer > -1 || intakeTimer > -1;
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
