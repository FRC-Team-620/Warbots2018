package org.jmhsrobotics.modules.drivecontrol;

import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.hardwareinterface.DragWheelEncoders;
import org.jmhsrobotics.hardwareinterface.Gyro;
import org.jmhsrobotics.hardwareinterface.WheelEncoders;

import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Localization extends CommandModule
{	
	private @Submodule Gyro gyro;
	private @Submodule WheelEncoders wheelEncoders;
	private @Submodule DragWheelEncoders dragWheelEncoders;

	private double x, y, speed, angle, rotationRate;
	
	private double totalDist;
	
	private double leftEncoder, rightEncoder, averageEncoder, diffEncoder, backEncoder, sideEncoder;
	
	public Localization()
	{
		SmartDashboard.putData("Localization", this);
	}
	
	@Override
	protected void initialize()
	{
		gyro.reset();
		wheelEncoders.reset();
		dragWheelEncoders.reset();
		
		totalDist = x = y = speed = angle = rotationRate = 0;
	}

	@Override
	protected void execute()
	{
		System.out.println("Starting sensor update");
		
		leftEncoder = wheelEncoders.left().getDist();
		rightEncoder = wheelEncoders.right().getDist();
		averageEncoder = wheelEncoders.average().getDist();
		diffEncoder = wheelEncoders.diff().getDist();
		
		backEncoder = dragWheelEncoders.forward().getDist();
		sideEncoder = dragWheelEncoders.side().getDist();
		
		
		double distanceMoved = wheelEncoders.average().getDist() - totalDist;
//		double distanceMoved = dragWheelEncoders.forward().getDist() - totalDist;
		totalDist += distanceMoved;
		
//		speed = dragWheelEncoders.forward().getRate();
		speed = wheelEncoders.average().getRate();
		
		x += Math.sin(Angle.fromDegrees(angle).measureRadians()) * distanceMoved;
		y += Math.cos(Angle.fromDegrees(angle).measureRadians()) * distanceMoved;
		
		angle = gyro.getAngle().measureDegreesUnsigned();
		rotationRate = gyro.getRotationRate();
		
		System.out.println("Ending sensor update");
	}
	
	@Override
	protected boolean isFinished()
	{
		return false;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public double getTotalDistanceDriven()
	{
		return totalDist;
	}
	
	public double getSpeed()
	{
		return speed;
	}
	
	public Angle getAngle()
	{
		return Angle.fromDegrees(angle);
	}
	
	public double getAngleDegrees()
	{
		return getAngle().measureDegreesUnsigned();
	}
	
	public double getRotationRate()
	{
		return rotationRate;
	}
	
	@Override
	public void initSendable(SendableBuilder builder)
	{
		builder.addDoubleProperty("x", this::getX, null);
		builder.addDoubleProperty("y", this::getY, null);
		builder.addDoubleProperty("angle", this::getAngleDegrees, null);
		builder.addDoubleProperty("speed", this::getSpeed, null);
		builder.addDoubleProperty("rotationRate", this::getRotationRate, null);
		builder.addDoubleProperty("total distance", this::getTotalDistanceDriven, null);
		
		builder.addDoubleProperty("encoders/wheels/left encoder", () -> leftEncoder, null);
		builder.addDoubleProperty("encoders/wheels/right encoder", () -> rightEncoder, null);
		builder.addDoubleProperty("encoders/wheels/average encoder", () -> averageEncoder, null);
		builder.addDoubleProperty("encoders/wheels/diff encoder", () -> diffEncoder, null);
		builder.addDoubleProperty("encoders/drag/back encoder", () -> backEncoder, null);
		builder.addDoubleProperty("encoders/drag/side encoder", () -> sideEncoder, null);
	}
}