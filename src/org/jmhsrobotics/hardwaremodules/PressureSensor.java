package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.util.PlainSendable;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PressureSensor extends PlainSendable implements Module
{
	private final static double vcc = 4.5;
	
	private AnalogInput pressureSensor;
	
	public PressureSensor(int port)
	{
		pressureSensor = new AnalogInput(port);
		setName("Pressure Sensor");
		SmartDashboard.putData(this);
	}

	public double getCurrent()
	{
		return pressureSensor.getVoltage();
	}
	
	public double getPressure()
	{
		return 250 * getCurrent() / vcc - 25;
	}
	
	@Override
	public void initSendable(SendableBuilder builder)
	{
		builder.addDoubleProperty("current", this::getCurrent, null);
		builder.addDoubleProperty("pressure", this::getPressure, null);
	}

	@Override
	public Command getTest()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
