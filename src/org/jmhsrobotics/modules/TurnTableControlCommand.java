package org.jmhsrobotics.modules;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.OptionalDouble;

import org.jmhsrobotics.core.modules.SubsystemManager;
import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.PerpetualCommand;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.RobotMath;
import org.jmhsrobotics.hardwareinterface.TurnTable;
import org.jmhsrobotics.hardwareinterface.TurnTableController;

public class TurnTableControlCommand extends CommandModule implements PerpetualCommand, TurnTableController
{
	private final static String DATA_FILE_NAME = "turntable";

	private final static double BASE_SPEED = 0.4;
	private final static double MAX_SPEED_BOOST = 0.6;
	private final static double BASE_SPEED_BUFFER = 0.4;
	private final static double POSITION_ESTIMATION_COEFFICIENT = 0.06;
	private final static double BASELINE_POTENTIAL_ERROR = 0.01;
	private final static double INITIAL_FAR_SIDE_ERROR = 0.02;
	private final static double BAD_POSITION_ESTIMATE_RESET_VALUE = 0.1;
	private final static double BAD_POSITION_ESTIMATE_CONFIDANCE_PENALTY = 0.3;
	private final static double DECALIBRATION_THRESHOLD = 2;
	private final static double TIMED_MOVEMENT_OVERSHOOT = 0.2;
	private final static double INITIAL_CALIBRATION_OVERSHOOT = 0.5;
	private final static double CALIBRATION_OVERSHOOT_MULTIPLIER = 3;

	private @Submodule Optional<SubsystemManager> subsystems;
	private @Submodule Optional<PersistantDataModule> fileHandler;
	private @Submodule TurnTable table;

	private boolean calibrating;
	private double potentialError;
	private double estimatedContinuousPosition;
	private Position discretePosition;
	private OptionalDouble manualSpeed;
	private double target;

	@Override
	public void onLink()
	{
		subsystems.ifPresent(sm -> requires(sm.getSubsystem("TurnTable")));
	}
	
	@Override
	public void calibrate()
	{
		target = Math.copySign(INITIAL_CALIBRATION_OVERSHOOT, -estimatedContinuousPosition);
		calibrating = true;
		manualSpeed = OptionalDouble.empty();
	}

	@Override
	public boolean isCalibrated()
	{
		return potentialError < 2.5;
	}

	@Override
	public void goToPartial(Position position, double amount)
	{
		manualSpeed = OptionalDouble.empty();

		if (position == Position.center)
			System.out.println("Turning turn table to the center.");
		else System.out.println("Turning turn table " + amount * 100 + "% to the " + position);

		double newTarget = getContinuousPosition(position) * amount;
		double constrainedNewTarget = RobotMath.constrain(newTarget, -1, 1);
		if (constrainedNewTarget == target) return;
		target = RobotMath.constrain(getContinuousPosition(position) * amount, -1, 1);
	}

	@Override
	public void goTo(Position position)
	{
		goToPartial(position, 1);
	}

	public void driveManually(double speed)
	{
		manualSpeed = OptionalDouble.of(RobotMath.constrain(speed, -BASE_SPEED, BASE_SPEED));
	}

	@Override
	public Position getCurrentPosition()
	{
		return discretePosition;
	}

	@Override
	public Position getTargetPosition()
	{
		return getDiscretePosition(target);
	}

	@Override
	public void reset()
	{
		manualSpeed = OptionalDouble.empty();
		potentialError = DECALIBRATION_THRESHOLD + 1;
		discretePosition = Position.left; //If it can't read it's last position, just pick a side to guess.

		if (fileHandler.isPresent())
		{
			PersistantDataModule handler = fileHandler.get();

			try
			{
				String[] data = handler.read(DATA_FILE_NAME);
				System.out.println("Last saved position at time " + data[0]);
				Position lastPositionSavedInFile = Position.valueOf(data[1]);
				discretePosition = lastPositionSavedInFile;
			}
			catch (IllegalArgumentException e)
			{
				System.out.println("Failed to parse position: " + e.getMessage());
			}
			catch (IOException e)
			{
				System.out.println("Failed to open or read save file for turntable.");
			}
		}
		else System.out.println("No data persistance module present.");

		System.out.println("Guessed turntable location: " + discretePosition);

		if (discretePosition == Position.center) 
			discretePosition = Position.left;

		estimatedContinuousPosition = getContinuousPosition(discretePosition);
	}

	/**
	 * This function converts discrete positions (left, right, and center) into
	 * continuous
	 * positions. Although anything to the left of the center counts as "left,"
	 * and
	 * anything to the right of the center counts as "right," this function
	 * returns
	 * the continuous positions for <i>all the way</i> to the left and <i>all
	 * the way</i>
	 * to the right respectively.
	 * 
	 * @param discretePosition
	 *            The discrete position to be converted into a continuous
	 *            position.
	 * 
	 * @return The outermost continuous position expected for a given discrete
	 *         position.
	 * 
	 * @see #getDiscretePosition(double)
	 */
	private final static double getContinuousPosition(Position discretePosition)
	{
		switch (discretePosition)
		{
			case left:
				return 1;
			case right:
				return -1;
			default:
				return 0;
		}
	}

	/**
	 * This is the opposite of {@link #getContinuousPosition(Position)}
	 * However, it works with any continuous position, not just the extremes.
	 */
	private final static Position getDiscretePosition(double continuousPosition)
	{
		if (continuousPosition > 0)
			return Position.left;
		else if (continuousPosition < 0)
			return Position.right;
		else return Position.center;
	}

	@Override
	protected void execute()
	{
		double lastTableSpeed = table.getSpeed();
		estimatedContinuousPosition += RobotMath.curve(lastTableSpeed, 2) * POSITION_ESTIMATION_COEFFICIENT;

		if (lastTableSpeed != 0)
			potentialError += 0.1 * Math.pow(Math.abs(lastTableSpeed) - BASE_SPEED, 2) + BASELINE_POTENTIAL_ERROR;

		//Update position and error estimates based on sensor data
		//By sensor data I mean that one single limit switch that I can read @Tom
		if (table.isInCenter())
		{
			discretePosition = Position.center;
			estimatedContinuousPosition = 0;
			potentialError = 0;
			
			if(calibrating)
			{
				System.out.println("Calibrated");
				target = 0;
				calibrating = false;
			}
		}
		else if (discretePosition == Position.center) //It was in the center, now it isn't
			discretePosition = getDiscretePosition(lastTableSpeed); //Assume it moved in the last direction it was set to
		else
		{
			double continuousPositionSignum = Math.signum(estimatedContinuousPosition);
			double discretePositionSignum = getContinuousPosition(discretePosition);
			
			//If its running estimate says it's on one side, but it hasn't crossed the middle limit switch from the other side
			if (discretePositionSignum != continuousPositionSignum)
			{
				System.out.println("Bad Position");
				estimatedContinuousPosition = BAD_POSITION_ESTIMATE_RESET_VALUE * discretePositionSignum;
				potentialError += BAD_POSITION_ESTIMATE_CONFIDANCE_PENALTY;
			}
			//If it's calibrated and it thinks it's all the way to one side (including overshoot)
			else if (isCalibrated() && Math.abs(estimatedContinuousPosition) >= 1 + TIMED_MOVEMENT_OVERSHOOT)
				potentialError = INITIAL_FAR_SIDE_ERROR; //Reset the error, but not all the way to zero because it still isn't measurable
		}
		
		//Operation make it move
		if (manualSpeed.isPresent())
			table.drive(manualSpeed.getAsDouble());
		else if(calibrating)
		{
//			System.out.println("Calibrating");
//			System.out.println("Estimated Position: " + estimatedContinuousPosition);
//			System.out.println("Target: " + target);
			
			if (Math.signum(target) * estimatedContinuousPosition < Math.abs(target))
				table.drive(Math.copySign(BASE_SPEED, target));
			else
				target *= -CALIBRATION_OVERSHOOT_MULTIPLIER;
		}
		else
		{
			double overshootTarget = target * (1 + TIMED_MOVEMENT_OVERSHOOT);
			double targetDisplacement = overshootTarget - estimatedContinuousPosition;
			
			if(Math.signum(targetDisplacement) == -Math.signum(overshootTarget))
				targetDisplacement = 0;
			
			double constrainedTargetDisplacement = RobotMath.constrain(targetDisplacement, -1, 1);
			double speedBoost;
			if(isCalibrated())
				speedBoost = RobotMath.xKinkedMap(constrainedTargetDisplacement, -1, 1, 0, -BASE_SPEED_BUFFER, BASE_SPEED_BUFFER, -MAX_SPEED_BOOST, MAX_SPEED_BOOST);
			else
				speedBoost = 0;
			double speed = Math.signum(targetDisplacement) * BASE_SPEED + speedBoost;
			
//			System.out.println("Discrete Position: " + discretePosition);
//			System.out.println("Estimated Position: " + estimatedContinuousPosition);
//			System.out.println("Target: " + overshootTarget);
//			System.out.println("Displacement: " + constrainedTargetDisplacement);
//			System.out.println("Uncertainty: " + potentialError);
//			System.out.println("Speed Boost: " + speedBoost);
//			System.out.println("Speed: " + speed);
//			System.out.println();
			
			table.drive(speed);
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
			data[1] = discretePosition.toString();
			System.out.println("Saving current position of turn table: " + discretePosition);
			try
			{
				handler.write(DATA_FILE_NAME, data);
			}
			catch (IOException e)
			{
				System.out.println("Failed to save turntable position because of " + e);
			}
		});
	}
}