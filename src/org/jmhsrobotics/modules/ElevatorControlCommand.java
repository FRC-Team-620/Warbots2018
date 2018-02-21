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