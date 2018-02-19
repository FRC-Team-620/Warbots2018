package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.core.modulesystem.annotations.HardwareModule;
import org.jmhsrobotics.hardwareinterface.GrabberWheels;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;

@HardwareModule
public class GrabberWheelsHardware implements GrabberWheels
{
	private SpeedController leftWheels, rightWheels;
	
	public GrabberWheelsHardware(int leftWheelsPort, int rightWheelsPort)
	{
		leftWheels = new Spark(leftWheelsPort);
		rightWheels = new Spark(rightWheelsPort);
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
}