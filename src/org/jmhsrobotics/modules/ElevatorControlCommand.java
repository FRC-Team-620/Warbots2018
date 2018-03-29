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
	private final static boolean PRINT_ENCODER_AND_SWITCH_DATA = false;
	
	private final static String DATA_FILE = "elevator";

	private final static int CALLIBRATION_TIMEOUT = 1500;
	
	private final static int ALLOWABLE_SETPOINT_ERROR = 50;
	
	private final static int TOWER_HEIGHT = 12000;
	
	private final static double BUFFER_EXP = .8;
	
	private final static double MAX_SAFE_SPEED_DOWN = .3;
	private final static int BOTTOM_BUFFER_ZONE = 4000;
	private final static int BOTTOM_STOP_HEIGHT = 200;
	
	private final static double MAX_SAFE_SPEED_UP = .5;
	private final static int TOP_BUFFER_ZONE = 2000;
	private final static int TOP_STOP_HEIGHT = 0;
	
	private final static double ANTI_SKIP_RAMP_RATE = 0.2;
	
	private @Submodule Optional<PersistantDataModule> fileSystem;
	private @Submodule Traveller traveller;
	private @Submodule Tower pneumatics;
	
	private double targetTravellerSpeed;
	private double travellerSpeed;
	private boolean pneumaticsExtended;
	private boolean climbing;
	
	private boolean error;
	private boolean calibrated;
	private int calibrationTime;
	
	private boolean hasTarget;
	
	private boolean pidSetForGoingUp;
	
	private int pistonActivations;
	
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
	public boolean onTarget()
	{
		return traveller.getError() <= ALLOWABLE_SETPOINT_ERROR;
	}

	@Override
	public void manualDrive(double speed)
	{
		if(speed != 0)
			hasTarget = false;
		targetTravellerSpeed = speed;
	}
	
	@Override
	public void setPneumatics(boolean state)
	{
		if (pneumaticsExtended && !state)
			++pistonActivations;
		
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
		pistonActivations = 0;
		calibrated = false;
		calibrationTime = 0;
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
		
		setPidForGoingDown();
	}
	
	@SuppressWarnings("unused")
	@Override
	protected void execute()
	{	
		if (PRINT_ENCODER_AND_SWITCH_DATA)
		{
			System.out.println("HEIGHT: " + traveller.getHeight());
			System.out.println("LOWER: " + traveller.isBottomLimitSwitchPressed());
			System.out.println("UPPER: " + traveller.isTopLimitSwitchPressed());
		}
		
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
			calibrationTime = 0;
			
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
			
			if(hasTarget)
			{
				if (traveller.getError() > 0 && !pidSetForGoingUp)
					setPidForGoingUp();
				else if (traveller.getError() < 0 && pidSetForGoingUp)
					setPidForGoingDown();
			}
			else
			{
				if (traveller.getMovementRate() < 0 && targetTravellerSpeed - travellerSpeed > ANTI_SKIP_RAMP_RATE)
					travellerSpeed += ANTI_SKIP_RAMP_RATE;
				else
					travellerSpeed = targetTravellerSpeed;
				
				double height = traveller.getHeight();
				double speed = travellerSpeed;
				
				double adjustedHeight = Math.pow(height, BUFFER_EXP);
				
				double lowerRangeStart = Math.pow(BOTTOM_STOP_HEIGHT, BUFFER_EXP);
				double lowerRangeEnd = Math.pow(BOTTOM_BUFFER_ZONE + BOTTOM_STOP_HEIGHT, BUFFER_EXP);
				double lowerSpeedConstraint = RobotMath.linearMap(adjustedHeight, lowerRangeStart, lowerRangeEnd, -MAX_SAFE_SPEED_DOWN, -1);
				if(lowerSpeedConstraint > -MAX_SAFE_SPEED_DOWN)
					lowerSpeedConstraint = -MAX_SAFE_SPEED_DOWN;
				if(height < BOTTOM_STOP_HEIGHT)
					lowerSpeedConstraint = 0;
				
				double upperRangeStart = Math.pow(TOWER_HEIGHT - TOP_STOP_HEIGHT, BUFFER_EXP);
				double upperRangeEnd = Math.pow(TOWER_HEIGHT - TOP_STOP_HEIGHT - TOP_BUFFER_ZONE, BUFFER_EXP);
				double upperSpeedConstraint = RobotMath.linearMap(adjustedHeight, upperRangeStart, upperRangeEnd, MAX_SAFE_SPEED_UP, 1);
				if(upperSpeedConstraint < MAX_SAFE_SPEED_UP)
					upperSpeedConstraint = MAX_SAFE_SPEED_UP;
				if(TOP_STOP_HEIGHT > 0 && height > TOWER_HEIGHT - TOP_STOP_HEIGHT)
					upperSpeedConstraint = 0;
				
				if(error)
				{
					upperSpeedConstraint = MAX_SAFE_SPEED_UP;
					lowerSpeedConstraint = -MAX_SAFE_SPEED_DOWN;
				}
				
				speed = RobotMath.constrain(speed, lowerSpeedConstraint, upperSpeedConstraint);
				
				traveller.drive(speed);
			}
		}
		else
		{
			traveller.drive(-MAX_SAFE_SPEED_DOWN);
			++calibrationTime;
			
			if(calibrationTime > CALLIBRATION_TIMEOUT)
			{
				calibrated = true;
				error = true;
			}
		}
	}
	
	private void setPidForGoingUp()
	{
		traveller.setPID(.8, 0.005, 0, 500, ALLOWABLE_SETPOINT_ERROR, .1, 1);
		pidSetForGoingUp = true;
	}
	
	private void setPidForGoingDown()
	{
		traveller.setPID(.4, 0.002, 0.03, 500, ALLOWABLE_SETPOINT_ERROR, .1, .8);
		pidSetForGoingUp = false;
	}
	
	@Override
	protected boolean isFinished()
	{
		return false;
	}
	
	@Override
	protected void end()
	{
		System.out.println("Piston Activations: " + pistonActivations);
		
		fileSystem.ifPresent(data ->
		{
			try
			{
				if (!error)
				{
					data.write(DATA_FILE, new String[] { String.valueOf(traveller.getHeight()) });
					System.out.println("Successfully saved talon state");
				}
			}
			catch(Exception e)
			{
				System.out.println("Failed to write data because:");
				e.printStackTrace();
			}
		});
	}
}