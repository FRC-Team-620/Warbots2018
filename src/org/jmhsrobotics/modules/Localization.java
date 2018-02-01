package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.hardwareinterface.Gyro;
import org.jmhsrobotics.hardwareinterface.WheelEncodersInterface;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class Localization implements Module
{	
	private @Submodule Gyro gyro;
	private @Submodule WheelEncodersInterface wheelEncoders;

	private Notifier updater;
	
	private double x, y, dx, dy, t, w;
	
	public Localization()
	{
		updater = new Notifier(this::updateSensors);
	}
	
	private void updateSensors()
	{
		
	}
	
	public void enable()
	{
		updater.startPeriodic(10);
	}
	
	public void disable()
	{
		updater.stop();
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public double getDX()
	{
		return dx;
	}
	
	public double getDY()
	{
		return dy;
	}
	
	public Angle getAngle()
	{
		return Angle.fromDegrees(t);
	}
	
	public double getRotationRate()
	{
		return w;
	}
	
	@Override
	public Command getTest()
	{
		return new InstantCommand()
		{
			@Override
			protected void initialize()
			{
				System.out.println("No code to test localization yet");
			}
		};
	}
}