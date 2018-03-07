package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.modulesystem.annotations.HardwareModule;
import org.jmhsrobotics.hardwareinterface.GrabberWheels;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Command;

@HardwareModule
public class GrabberWheelsHardware implements Module, GrabberWheels
{
	private SpeedController leftWheels, rightWheels;
	private DigitalInput cubeLimitSwitch;
	
	public GrabberWheelsHardware(int leftWheelsPort, int rightWheelsPort, int cubeLimitSwitchPort)
	{
		leftWheels = new Spark(leftWheelsPort);
		rightWheels = new Spark(rightWheelsPort);
		cubeLimitSwitch = new DigitalInput(cubeLimitSwitchPort);
	}
	
	@Override
	public void setLeftWheels(double speed)
	{
		leftWheels.set(speed);
	}

	@Override
	public void setRightWheels(double speed)
	{
		rightWheels.set(speed);
	}
	
	@Override
	public boolean hasPrism()
	{
		return cubeLimitSwitch.get();
	}
	
	@Override
	public Command getTest()
	{
		return null;
	}
}