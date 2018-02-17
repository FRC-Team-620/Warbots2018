package org.jmhsrobotics.modules.drivecontrol;

import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;
import org.jmhsrobotics.hardwareinterface.DragWheelEncoders;
import org.jmhsrobotics.hardwareinterface.EncoderGroup.EncoderData;
import org.jmhsrobotics.hardwareinterface.Gyro;
import org.jmhsrobotics.hardwareinterface.WheelEncoders;

import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Localization extends CommandModule
{	
	private @Submodule Gyro gyro;
	private @Submodule WheelEncoders wheelEncoders;
	private @Submodule DragWheelEncoders dragWheelEncoders;

	private double x, y, speed;
	private Angle angle, rotationRate;
	private double totalDist;
	private long lastMeasureNanoTime;
	private PositionInterpolator interpolator;
	
//	private double leftEncoder, rightEncoder, averageEncoder, diffEncoder, backEncoder, sideEncoder;
	
	public Localization(PositionInterpolator interpolator)
	{
		this.interpolator = interpolator;
		SmartDashboard.putData("Localization", this);
	}
	
	@Override
	protected void initialize()
	{
		gyro.reset();
		wheelEncoders.reset();
		dragWheelEncoders.reset();
		
		lastMeasureNanoTime = System.nanoTime();
		
		totalDist = x = y = speed;
		angle = rotationRate = Angle.ZERO;
	}

	@Override
	protected void execute()
	{
		long currentNanoTime = System.nanoTime();
		double dt = (currentNanoTime - lastMeasureNanoTime) / 1E9;
		lastMeasureNanoTime = currentNanoTime;
		
//		leftEncoder = wheelEncoders.left().getDist();
//		rightEncoder = wheelEncoders.right().getDist();
//		averageEncoder = wheelEncoders.average().getDist();
//		diffEncoder = wheelEncoders.diff().getDist();
//		
//		backEncoder = dragWheelEncoders.forward().getDist();
//		sideEncoder = dragWheelEncoders.side().getDist();
		
		
		EncoderData forwardMovement = wheelEncoders.average();
		
		double distanceMoved = forwardMovement.getDist() - totalDist;
		totalDist += distanceMoved;
		
		double oldSpeed = speed;
		speed = forwardMovement.getRate();
		
		Angle oldAngle = angle;
		angle = gyro.getAngle();
		
		Angle oldRotationRate = rotationRate;
		rotationRate = gyro.getRotationPerSecond();
		
		double w0 = oldRotationRate.measureRadians();
		double wt = rotationRate.measureRadians();
		double theta0 = oldAngle.measureRadians();
		double thetat = angle.measureRadians();
		double v0 = oldSpeed;
		double vt = speed;
		double s = distanceMoved;
		Point relativePosition = interpolator.getRelativePosition(w0, wt, theta0, thetat, v0, vt, s, dt);
		
		x += relativePosition.getX();
		y += relativePosition.getY();
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
	
	public double getDistanceTo(Point target)
	{
		double dx = target.getX() - getX();
		double dy = target.getY() - getY();
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	public Angle getAngleTo(Point target)
	{
		double dx = target.getX() - getX();
		double dy = target.getY() - getY();
		return Angle.fromRiseAndRun(dx, dy);
	}
	
	public double getSpeedToward(Point target)
	{
		Angle toTarget = getAngleTo(target);
		
		return getSpeed() * toTarget.minus(getAngle()).cos();
	}
	
	public double getSpeed()
	{
		return speed;
	}
	
	public Angle getAngle()
	{
		if(angle == null)
			return Angle.ZERO;
		return angle;
	}
	
	public double getAngleDegrees()
	{
		return getAngle().measureDegreesUnsigned();
	}
	
	public Angle getRotationPerSecond()
	{
		if(rotationRate == null)
			return Angle.ZERO;
		return rotationRate;
	}
	
	public double getDegreesPerSecond()
	{
		return getRotationPerSecond().measureDegrees();
	}
	
	@Override
	public void initSendable(SendableBuilder builder)
	{
		builder.addDoubleProperty("x", this::getX, null);
		builder.addDoubleProperty("y", this::getY, null);
		builder.addDoubleProperty("angle", this::getAngleDegrees, null);
		builder.addDoubleProperty("speed", this::getSpeed, null);
		builder.addDoubleProperty("rotationRate", this::getDegreesPerSecond, null);
		builder.addDoubleProperty("total distance", this::getTotalDistanceDriven, null);
		
//		builder.addDoubleProperty("encoders/wheels/left encoder", () -> leftEncoder, null);
//		builder.addDoubleProperty("encoders/wheels/right encoder", () -> rightEncoder, null);
//		builder.addDoubleProperty("encoders/wheels/average encoder", () -> averageEncoder, null);
//		builder.addDoubleProperty("encoders/wheels/diff encoder", () -> diffEncoder, null);
//		builder.addDoubleProperty("encoders/drag/back encoder", () -> backEncoder, null);
//		builder.addDoubleProperty("encoders/drag/side encoder", () -> sideEncoder, null);
	}
}