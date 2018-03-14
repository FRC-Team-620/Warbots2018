package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.util.PlainSendable;
import org.jmhsrobotics.hardwareinterface.Traveller;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class TravellerHardware extends PlainSendable implements Module, Traveller
{
	private WPI_TalonSRX motor;

	public TravellerHardware(int deviceID)
	{
		motor = new WPI_TalonSRX(deviceID);
		motor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		motor.config_kP(0, .5, 0);
		motor.config_kI(0, 0.002, 0);
		motor.config_kD(0, 0, 0);
		motor.config_IntegralZone(0, 500, 0);
		motor.configAllowableClosedloopError(0, 50, 0);
		motor.selectProfileSlot(0, 0);
	}
	
	@Override
	public void drive(double speed)
	{
		motor.set(speed);
	}
	
	@Override
	public void driveTo(int target)
	{
		motor.set(ControlMode.Position, target);
	}
	
	@Override
	public void reset(int currentHeight)
	{
		motor.getSensorCollection().setQuadraturePosition(currentHeight, 0);
	}
	
	@Override
	public int getHeight()
	{
		return motor.getSensorCollection().getQuadraturePosition();
	}
	
	@Override
	public boolean isBottomLimitSwitchPressed()
	{
		return motor.getSensorCollection().isRevLimitSwitchClosed();
	}
	
	@Override
	public void initSendable(SendableBuilder builder)
	{
	}

	@Override
	public Command getTest()
	{
		return null;
	}

	@Override
	public boolean isTopLimitSwitchPressed()
	{
		return motor.getSensorCollection().isFwdLimitSwitchClosed();
	}
}
