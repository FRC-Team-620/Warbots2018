package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.hardwareinterface.GrabberWheels;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Command;

public class SingleSparkGrabberWheelsHardware implements Module, GrabberWheels
{
	private SpeedController controller;
	private DigitalInput button;
	
	public SingleSparkGrabberWheelsHardware(int motorPort, int buttonPort)
	{
		controller = new Spark(motorPort);
		button = new DigitalInput(buttonPort);
	}
	
	@Override
	public void set(double speed, double jank)
	{
		controller.set(speed);
	}

	@Override
	public boolean hasPrism()
	{
		return button.get();
	}

	@Override
	public Command getTest()
	{
		return null;
	}
}