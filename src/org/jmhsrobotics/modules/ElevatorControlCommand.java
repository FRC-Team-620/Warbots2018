package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.PerpetualCommand;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.ElevatorController;
import org.jmhsrobotics.hardwareinterface.Tower;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class ElevatorControlCommand extends CommandModule implements PerpetualCommand, ElevatorController
{
	private @Submodule Tower pneumatics;
	
	private Position lastSetPosition = Position.ground;
	private WPI_TalonSRX motor;
	
	public ElevatorControlCommand(int talonCanId)
	{
		motor = new WPI_TalonSRX(talonCanId);
		motor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
	}
	
	@Override
	public void reset()
	{
//		motor.getSensorCollection().setQuadraturePosition(0, 0);
//		motor.setSafetyEnabled(true);
//		motor.setExpiration(0.1);
	}
	
	@Override
	public void goTo(Position position)
	{
		goToRaw(getRawHeight(position), getPistonsExtended(position));
	}
	
	private static double getRawHeight(Position position)
	{
		return worldHeightToRawUnits(getWorldHeight(position));
	}
	
	private static double getWorldHeight(Position position)
	{
		switch(position)
		{
			case ground:
				return 0;
			case exchangePortal:
				return 8;
			case arcadeSwitch:
				return 16;
			case scaleLow:
				return 24;
			case scaleMedium:
				return 32;
			case scaleHigh:
				return 40;
			default:
				return 0;
		}
	}
	
	private static double worldHeightToRawUnits(double height)
	{
		return height / 4.7;
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
		setPneumatics(raisePneumatics);
	}

	@Override
	public void driveManual(double linearSpeed, boolean raisePneumatics)
	{
		System.out.println("Manually driving elevator at " + linearSpeed);
		motor.set(linearSpeed);
		setPneumatics(raisePneumatics);
	}
	
	private void setPneumatics(boolean state)
	{
		if(pneumatics.isExtended() != state)
			if(state)
				pneumatics.raise();
			else
				pneumatics.lower();
	}

	@Override
	public void climbFullPower()
	{
		pneumatics.climb();
	}
	
	@Override
	protected void execute()
	{
		System.out.println("Encoder: " + motor.getSensorCollection().getQuadraturePosition());
	}
	
	@Override
	protected boolean isFinished()
	{
		return false;
	}
}