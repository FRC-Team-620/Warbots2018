package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.hardwareinterface.TurnTable;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Command;

public class TurnTableHardware implements Module, TurnTable
{
	private SpeedController motor;
	private DigitalInput limitSwitch;
	
	public TurnTableHardware(int motorPort, int limitSwitchPort)
	{
		motor = new Spark(motorPort);
		limitSwitch = new DigitalInput(limitSwitchPort);
	}
	
	@Override
	public void drive(double speed)
	{
		motor.set(speed);
	}

	@Override
	public double getSpeed()
	{
		return motor.get();
	}
	
	@Override
	public boolean readMiddleLimitSwitch()
	{
		return limitSwitch.get();
	}

	@Override
	public Command getTest()
	{
		return null;
	}
}