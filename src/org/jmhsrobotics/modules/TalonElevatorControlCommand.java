package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.ElevatorController;
import org.jmhsrobotics.hardwareinterface.Tower;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class TalonElevatorControlCommand extends CommandModule implements ElevatorController
{
	private @Submodule Tower pneumatics;
	
	private Position lastSetPosition = Position.ground;
	private WPI_TalonSRX motor;
	
	@Override
	public void onLink()
	{
		motor = new WPI_TalonSRX(-1);
		motor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		motor.getSensorCollection().setQuadraturePosition(0, 0);
	}
	
	@Override
	protected void initialize()
	{
		motor.setSafetyEnabled(true);
		motor.setExpiration(0.1);
	}
	
	@Override
	public void goTo(Position position)
	{
		goToRaw(getLinearHeight(position), getPistonsExtended(position));
	}
	
	private static double getLinearHeight(Position position)
	{
		switch(position)
		{
			case ground:
				return 0;
			case exchangePortal:
				return 130;
			case arcadeSwitch:
				return 120;
			case scaleLow:
				return 1000;
			case scaleMedium:
				return 1100;
			case scaleHigh:
				return 1200;
			default:
				return 0;
		}
	}
	
	private static boolean getPistonsExtended(Position position)
	{
		return position.compareTo(Position.scaleMedium) > 0;
	}

	@Override
	public Position getCurrentLifterPosition()
	{
		return lastSetPosition;
	}

	@Override
	public void goToRaw(double linearHeight, boolean raisePneumatics)
	{
		motor.set(ControlMode.Position, linearHeight);
		
		if(pneumatics.isExtended() != raisePneumatics)
			if(raisePneumatics)
				pneumatics.raise();
			else
				pneumatics.lower();
	}

	@Override
	public void driveManual(double linearSpeed, boolean raisePneumatics)
	{
		motor.set(ControlMode.Current, linearSpeed);
	}

	@Override
	public void climbFullPower()
	{
		pneumatics.climb();
	}
	
	@Override
	protected boolean isFinished()
	{
		return false;
	}
}