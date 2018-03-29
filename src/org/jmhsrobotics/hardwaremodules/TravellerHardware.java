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
		motor.selectProfileSlot(0, 0);
	}
	
	@Override
	public void drive(double speed)
	{
		motor.set(speed);
	}
	
	@Override
	public double getMovementRate()
	{
		return motor.getSensorCollection().getQuadratureVelocity();
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

	@Override
	public void setPID(double p, double i, double d, int integralZone, int maxError, double rampRate, double maxOutput)
	{
		motor.config_kP(0, p, 0);
		motor.config_kI(0, i, 0);
		motor.config_kD(0, d, 0);
		motor.config_IntegralZone(0, integralZone, 0);
		motor.configAllowableClosedloopError(0, maxError, 0);
		motor.configClosedloopRamp(rampRate, 0);
//		motor.configClosedLoopPeakOutput(0, maxOutput, 0);
	}

	@Override
	public double getError()
	{
		return motor.getClosedLoopError(0);
	}

	@Override
	public int getHeight()
	{
		return motor.getSensorCollection().getQuadraturePosition();
	}
}
