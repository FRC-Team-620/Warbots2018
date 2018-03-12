package org.jmhsrobotics.modules;

import java.util.Optional;

import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.PerpetualCommand;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.RobotMath;
import org.jmhsrobotics.hardwareinterface.ElevatorController;
import org.jmhsrobotics.hardwareinterface.Tower;
import org.jmhsrobotics.hardwareinterface.Traveller;

import edu.wpi.first.wpilibj.DriverStation;

public class ElevatorControlCommand extends CommandModule implements PerpetualCommand, ElevatorController
{
	private final static String DATA_FILE = "elevator";
	
	private static int TOWER_HEIGHT = 12140;
	
	private static double BUFFER_EXP = 3;
	private static double MAX_SAFE_SPEED = .2;
	
	private static int BOTTOM_BUFFER_ZONE = 1500;
	private static int LOWER_STOP_HEIGHT = 300;
	
	private static int TOP_BUFFER_ZONE = 1500;
	
	private @Submodule Optional<PersistantDataModule> fileSystem;
	private @Submodule Traveller traveller;
	private @Submodule Tower pneumatics;
	
	private double travellerSpeed;
	private boolean pneumaticsExtended;
	private boolean climbing;
	
	private boolean error;
	private boolean calibrated;
	
	private boolean hasTarget;
	
	@Override
	public void reset()
	{
		traveller.reset(0);
		calibrated = false;
		error = false;
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
	public void goToRaw(int linearHeight, boolean raisePneumatics)
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
		
		fileSystem.ifPresent(data -> 
		{
			try
			{
				String[] info = data.read(DATA_FILE);
				int height = Integer.parseInt(info[0]);
				data.write(DATA_FILE, new String[0]);
				traveller.reset(height);
				calibrated = true;
			}
			catch(Exception e)
			{
				System.out.println("Failed to read elevator data file because:");
				e.printStackTrace();
			}
		});
	}
	
	@Override
	protected void execute()
	{	
		traveller.printStuff();
		
		System.out.println("Height: " + traveller.getHeight());
		System.out.println("Bot: " + traveller.isBottomLimitSwitchPressed() + " Top: " + traveller.isTopLimitSwitchPressed());
		
		if(traveller.isBottomLimitSwitchPressed())
		{
			if(traveller.isTopLimitSwitchPressed())
			{
				DriverStation.reportError("LIMIT SWITCH ERROR: BOTH SWITCHES PRESSED", true);
				error = true;
			}
			
			if(!calibrated)
				System.out.println("Traveller calibrated");
			traveller.reset(0);
			calibrated = true;
		}
		else if(traveller.isTopLimitSwitchPressed())
		{
			traveller.reset(TOWER_HEIGHT);
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
			
			double adjustedHeight = Math.pow(height, BUFFER_EXP);
			
			double lowerRangeStart = 0;
			double lowerRangeEnd = Math.pow(BOTTOM_BUFFER_ZONE, BUFFER_EXP);
			double lowerSpeedConstraint = RobotMath.linearMap(adjustedHeight, lowerRangeStart, lowerRangeEnd, 0, -1);
			if(lowerSpeedConstraint > -MAX_SAFE_SPEED)
				lowerSpeedConstraint = -MAX_SAFE_SPEED;
			if(height < LOWER_STOP_HEIGHT)
				lowerSpeedConstraint = 0;
			
			double upperRangeStart = Math.pow(TOWER_HEIGHT, BUFFER_EXP);
			double upperRangeEnd = Math.pow(TOWER_HEIGHT - TOP_BUFFER_ZONE, BUFFER_EXP);
			double upperSpeedConstraint = RobotMath.linearMap(adjustedHeight, upperRangeStart, upperRangeEnd, 0, 1);
			if(upperSpeedConstraint < MAX_SAFE_SPEED)
				upperSpeedConstraint = MAX_SAFE_SPEED;
			
			if(error)
			{
				upperSpeedConstraint = MAX_SAFE_SPEED;
				lowerSpeedConstraint = -MAX_SAFE_SPEED;
			}
			
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
			traveller.drive(-MAX_SAFE_SPEED);
	}
	
	@Override
	protected boolean isFinished()
	{
		return false;
	}
	
	@Override
	protected void end()
	{
		fileSystem.ifPresent(data ->
		{
			try
			{
				data.write(DATA_FILE, new String[] { String.valueOf(traveller.getHeight()) });
			}
			catch(Exception e)
			{
				System.out.println("Failed to write data because:");
				e.printStackTrace();
			}
		});
	}
}