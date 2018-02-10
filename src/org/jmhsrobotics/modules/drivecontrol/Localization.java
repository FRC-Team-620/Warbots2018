package org.jmhsrobotics.modules.drivecontrol;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.PlainSendable;
import org.jmhsrobotics.hardwareinterface.DragWheelEncoders;
import org.jmhsrobotics.hardwareinterface.Gyro;
import org.jmhsrobotics.hardwareinterface.WheelEncoders;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Localization extends PlainSendable implements Module
{	
	private @Submodule Gyro gyro;
	private @Submodule WheelEncoders wheelEncoders;
	private @Submodule DragWheelEncoders dragWheelEncoders;

	private Notifier updater;
	
	private double x, y, dx, dy, o, w, t;
	
	public Localization()
	{
		updater = new Notifier(this::updateSensors);
		t = System.currentTimeMillis() / 1000.;
		SmartDashboard.putData("Localization", this);
	}
	
	@SuppressWarnings("unused") //a lot of this data will be used later to develope more sophisticated localization
	private void updateSensors()
	{
		double oldT = t;
		t = System.currentTimeMillis();
		double dt = t - oldT;
		
		double oldW = w;
		w = gyro.getRotationRate();
		double oldO = o;
		o = gyro.getAngle().measureRadians();
		
		double speed = wheelEncoders.average().getRate();
		System.out.println(wheelEncoders.left().getDist());
		double oldDx = dx;
		dx = Math.sin(o) * speed;
		double oldDy = dy;
		dy = Math.cos(o) * speed;
		
		x += dx;
		y += dy;
	}
	
	public void enable()
	{
		updater.startPeriodic(.002);
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
	
	public double getPos(Angle axis)
	{
		return x * Math.sin(axis.measureRadians()) + y * Math.cos(axis.measureRadians());
	}
	
	public double getForwardPos()
	{
		return getPos(getAngle());
	}
	
	public double getDX()
	{
		return dx;
	}
	
	public double getDY()
	{
		return dy;
	}
	
	public double getSpeed(Angle axis)
	{
		return dx * Math.sin(axis.measureRadians()) + dy * Math.cos(axis.measureRadians());
	}
	
	public double getForwardSpeed()
	{
		return getSpeed(getAngle());
	}
	
	public Angle getAngle()
	{
		return Angle.fromRadians(o);
	}
	
	public double getAngleDegreesUnsigned()
	{
		return getAngle().measureDegreesUnsigned();
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

	@Override
	public void initSendable(SendableBuilder builder)
	{
		builder.addDoubleProperty("x", this::getX, null);
		builder.addDoubleProperty("y", this::getY, null);
		builder.addDoubleProperty("o", this::getAngleDegreesUnsigned, null);
		builder.addDoubleProperty("dx", this::getDX, null);
		builder.addDoubleProperty("dy", this::getDX, null);
		builder.addDoubleProperty("w", this::getRotationRate, null);
		builder.addDoubleProperty("t", () -> t, null);
	}
}