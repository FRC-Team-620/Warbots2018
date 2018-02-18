package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.hardwareinterface.TurnTable;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Command;

public class TurnTableHardware implements Module, TurnTable
{
	SpeedController motor;
	
	public TurnTableHardware(int port)
	{
		motor = new Spark(port);
	}
	
	@Override
	public void driveTurnTableMotor(double speed)
	{
		motor.set(speed);
	}

	@Override
	public boolean readMiddleLimitSwitch()
	{
		return false;
	}

	@Override
	public Command getTest()
	{
		return null;
	}
}