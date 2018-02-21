package org.jmhsrobotics.modules;

import java.io.File;
import java.util.Date;
import java.util.Optional;

import org.jmhsrobotics.core.modules.SubsystemManager;
import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.PerpetualCommand;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.RobotMath;
import org.jmhsrobotics.hardwareinterface.TurnTable;
import org.jmhsrobotics.hardwareinterface.TurnTableController;

import edu.wpi.first.wpilibj.DriverStation;

public class TurnTableControlCommand extends CommandModule implements PerpetualCommand, TurnTableController
{
	private final static double INITIAL_CALIBRATION_OVERSHOOT = 0.4;
	private final static double CALIBRATION_OVERSHOOT_MULTIPLIER = 1.5;
	private final static double BASE_TURN_SPEED = 0.4;
	private final static double MAX_SPEED_BOOST = 0.6;

	private @Submodule Optional<SubsystemManager> subsystems;
	private @Submodule Optional<PersistantDataModule> fileHandler;
	private @Submodule TurnTable tableHardware;

	private File dataFile;
	
	private double positionEstimate;
	private double calibrationRange;

	private Position currentPosition = null;
	private Position targetPosition = Position.center;

	@Override
	public void onLink()
	{
		subsystems.ifPresent(sm -> requires(sm.getSubsystem("TurnTable")));
	}

	@Override
	public void goTo(Position position)
	{
		System.out.println("turning table to " + position);

		if (position == targetPosition) return;
		targetPosition = position;
	}

	@Override
	public Position getCurrentTurnTablePosition()
	{
		return currentPosition;
	}

	@Override
	public void reset()
	{
		targetPosition = Position.center;
		currentPosition = null;
		positionEstimate = 1;
		
		fileHandler.ifPresent(handler ->
		{
			try
			{
				dataFile = handler.getDataFile("turntable");
				String[] data = handler.read(dataFile);
				System.out.println("Last saved position at time " + data[0]);
				Position savedPosition = Position.valueOf(data[1]);
				if(savedPosition == null)
					System.out.println("Could not read last saved position: " + data[1]);
				else
					positionEstimate = getPosition(savedPosition);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			System.out.println("Guessed turntable location: " + positionEstimate);
		});
		if (!fileHandler.isPresent()) DriverStation.reportError("No file handler in robot!! Turntable may take longer to calibrate.", false);
		
		if(positionEstimate == 0)
			positionEstimate = 1;
		
		calibrationRange = Math.copySign(INITIAL_CALIBRATION_OVERSHOOT, -positionEstimate);
	}

	private final static double getPosition(Position position)
	{
		switch (position)
		{
			case left:
				return 1;
			case right:
				return -1;
			default:
				return 0;
		}
	}

	@Override
	protected void execute()
	{
		positionEstimate += RobotMath.curve(tableHardware.getSpeed(), 2) * .06;
		
		if (currentPosition == null) //Calibrate turn table
		{
			if(!tableHardware.readMiddleLimitSwitch())
			{
				positionEstimate = 0;
				currentPosition = Position.center;
			}
			else
			{
				if(calibrationRange > 0)
					if(positionEstimate < calibrationRange)
						tableHardware.drive(BASE_TURN_SPEED);
					else
						calibrationRange *= -CALIBRATION_OVERSHOOT_MULTIPLIER;
				else
					if(positionEstimate > calibrationRange)
						tableHardware.drive(-BASE_TURN_SPEED);
					else
						calibrationRange *= -CALIBRATION_OVERSHOOT_MULTIPLIER;
			}
		}
		else
		{
			if (currentPosition == Position.center) positionEstimate = 0;

			double targetPos = getPosition(targetPosition);
			double distanceFromTarget = Math.abs(targetPos - positionEstimate);
			double speedBoost = RobotMath.xKinkedMap(distanceFromTarget, -1, 1, 0, -.3, .3, -MAX_SPEED_BOOST, MAX_SPEED_BOOST);
			double turnSpeed = BASE_TURN_SPEED + speedBoost;
			if (currentPosition == targetPosition)
				if (Math.abs(positionEstimate) < 1.2)
					tableHardware.drive(Math.signum(targetPosition.compareTo(Position.center)) * turnSpeed);
				else tableHardware.drive(0);
			else
			{
				tableHardware.drive(Math.copySign(turnSpeed, targetPosition.compareTo(currentPosition)));

				if (!tableHardware.readMiddleLimitSwitch())
					currentPosition = Position.center;
				else if (currentPosition == Position.center) currentPosition = targetPosition;
			}
		}
	}

	@Override
	protected boolean isFinished()
	{
		return false;
	}

	@Override
	protected void end()
	{
		fileHandler.ifPresent(handler ->
		{
			String[] data = new String[2];
			data[0] = handler.getDateFormat().format(new Date());
			data[1] = currentPosition.toString();
			System.out.println("Saving current position of turn table: " + currentPosition);
			try
			{
				handler.write(dataFile, data);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
	}
}