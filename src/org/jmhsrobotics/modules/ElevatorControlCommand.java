package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.PerpetualCommand;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.RobotMath;
import org.jmhsrobotics.hardwareinterface.ElevatorController;
import org.jmhsrobotics.hardwareinterface.Tower;
import org.jmhsrobotics.hardwareinterface.Traveller;

public class ElevatorControlCommand extends CommandModule implements PerpetualCommand, ElevatorController
{
	private static double BOTTOM_BUFFER_ZONE = 1500;
	private static double BUFFER_EXP = 3;
	private static double MIN_SAFE_SPEED = .2;
	private static double SOFTWARE_STOP_HEIGHT = 300;
	
	private @Submodule Traveller traveller;
	private @Submodule Tower pneumatics;
	
	private double travellerSpeed;
	private boolean pneumaticsExtended;
	private boolean climbing;
	
	private boolean calibrated;
	
	private boolean hasTarget;
	
	@Override
	public void reset()
	{
		traveller.reset();
		calibrated = false;
	}
	
	@Override
	public void goTo(Position position)
	{
		//TODO
	}
	
	@Override
	public Position getCurrentLifterPosition()
	{
		//TODO
		return null;
	}

	@Override
	public void goToRaw(double linearHeight, boolean raisePneumatics)
	{
		setPneumatics(raisePneumatics);
		traveller.driveTo(linearHeight);
		hasTarget = true;
	}

	@Override
	public void manualDrive(double speed)
	{
		if(speed != 0)
			hasTarget = false;
		travellerSpeed = speed;
	}
	
	@Override
	public void setPneumatics(boolean state)
	{
		pneumaticsExtended = state;
	}

	@Override
	public boolean isPneumaticsExtended()
	{
		return pneumaticsExtended;
	}
	
	@Override
	public void climbFullPower()
	{
		climbing = true;
	}
	
	@Override
	protected void initialize()
	{
		calibrated = false;
		climbing = false;
		pneumaticsExtended = false;
	}
	
	@Override
	protected void execute()
	{	
		traveller.printStuff();
		
		System.out.println(traveller.getHeight());
		System.out.println("Bot: " + traveller.isBottomLimitSwitchPressed() + " Top: " + traveller.isTopLimitSwitchPressed());
		
		if(traveller.isBottomLimitSwitchPressed())
		{
			if(!calibrated)
				System.out.println("Traveller calibrated");
			traveller.reset();
			calibrated = true;
		}
		
		if (calibrated)
		{
			if(climbing)
			{
				if(pneumatics.isExtended())
					pneumatics.lower();
				
				pneumatics.climb();
			}
			else if(pneumaticsExtended)
			{
				if(!pneumatics.isExtended())
					pneumatics.raise();
			}
			else
			{
				if(pneumatics.isExtended())
					pneumatics.lower();
			}
			
			double height = traveller.getHeight();
			double speed = travellerSpeed;
			
			double lowerSpeedConstraint = RobotMath.linearMap(Math.pow(height, BUFFER_EXP), 0, Math.pow(BOTTOM_BUFFER_ZONE, BUFFER_EXP), 0, -1);
			if(lowerSpeedConstraint > -MIN_SAFE_SPEED)
				lowerSpeedConstraint = -MIN_SAFE_SPEED;
			if(height < SOFTWARE_STOP_HEIGHT)
				lowerSpeedConstraint = 0;
			
			double upperSpeedConstraint = Double.POSITIVE_INFINITY;
			
			speed = RobotMath.constrain(speed, lowerSpeedConstraint, upperSpeedConstraint);

			if(hasTarget)
				System.out.println("Has Target");
			else
			{
				System.out.println("Driving at " + speed);
				traveller.drive(speed);
			}
		}
		else
			traveller.drive(-MIN_SAFE_SPEED);
	}
	
	@Override
	protected boolean isFinished()
	{
		return false;
	}
}