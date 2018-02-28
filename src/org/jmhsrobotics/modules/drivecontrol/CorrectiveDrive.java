package org.jmhsrobotics.modules.drivecontrol;

import java.util.Optional;

import org.jmhsrobotics.core.modules.SubsystemManager;
import org.jmhsrobotics.core.modulesystem.PerpetualCommand;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.PIDCalculator;
import org.jmhsrobotics.core.util.PIDSensor;
import org.jmhsrobotics.core.util.Point;
import org.jmhsrobotics.hardwareinterface.DriveController;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CorrectiveDrive extends DriveController implements PerpetualCommand
{
	private @Submodule Optional<SubsystemManager> subsystems;
	private @Submodule Localization localization;
	
	private Optional<Point> targetPoint;
	private Optional<Angle> targetAngle;
	
	private PIDSensor angleSensor;
	private PIDCalculator angleController;
	
	private PIDSensor distanceSensor;
	private PIDCalculator distanceController;
	
	private boolean targetReverse;
	
	private double speed, turn;
	
	@Override
	public void onLink()
	{
		subsystems.ifPresent(sm -> requires(sm.getSubsystem("DriveTrain")));
		
		angleSensor = PIDSensor.fromDispAndRate(localization::getAngleDegrees, localization::getDegreesPerSecond);
		angleController = new PIDCalculator(0.035, 0, 10, angleSensor, o -> turn = o);
		angleController.setInputRange(0, 360);
		angleController.setContinuous();
		angleController.setOutputRange(-1, 1);
		
		distanceSensor = PIDSensor.fromDispAndRate(() -> -getDistanceToTargetPoint(), this::getSpeedTowardTarget);
		distanceController = new PIDCalculator(0.03, 0, 0, distanceSensor, o -> speed = o);
		distanceController.setOutputRange(-1, 1);
		distanceController.setSetpoint(0);
		
		distanceController.setName("Distance Controller");
		SmartDashboard.putData(distanceController);
		
		angleController.setName("Angle Controller PID");
		SmartDashboard.putData(angleController);
	}
	
	@Override
	@SuppressWarnings("hiding")
	public void drive(double speed, double turn)
	{	
		if((speed != 0 || turn != 0) && (targetPoint.isPresent() || targetAngle.isPresent()))
		{
			System.out.println("Switching to manual drive");
			removeTarget();
		}
		
		this.speed = speed;
		this.turn = turn;
	}
	
	@Override
	public void setTarget(Point point, boolean reverse)
	{
		if(point == null)
			throw new NullPointerException();
		
		System.out.println("Setting target to: " + point + (reverse ? " in reverse" : ""));
		targetReverse = reverse;
		targetPoint = Optional.of(point);
	}
	
	@Override
	public void setRelativeTarget(Point point, boolean reverse)
	{
		setTarget(localization.getX() + point.getX(), localization.getY() + point.getY(), reverse);
	}
	
	@Override
	public void setTarget(Angle angle)
	{
		if(angle == null)
			throw new NullPointerException();
		
		if(targetAngle.equals(Optional.of(angle)))
			return;
		
		System.out.println("Setting target to angle: " + angle);
		targetPoint = Optional.empty();
		speed = 0;
		targetAngle = Optional.of(angle);
	}
	
	@Override
	public void setRelativeTarget(Angle angle)
	{
		setTarget(localization.getAngle().plus(angle));
	}
	
	@Override
	public void removeTarget()
	{
		targetAngle = Optional.empty();
		targetPoint = Optional.empty();
		turn = speed = 0;
	}
	
	@Override
	public double getDistanceToTargetPoint()
	{
		return targetPoint.map(localization::getDistanceTo)
				.orElseThrow(() -> new RuntimeException("No target point set"));
	}
	
	@Override
	public Angle getDistanceToTargetAngle()
	{
		return targetAngle.map(localization.getAngle()::minus).map(Angle::absoluteValue)
				.orElseThrow(() -> new RuntimeException("No target angle set"));
	}
	
	private double getSpeedTowardTarget()
	{
		return targetPoint.map(localization::getSpeedToward)
				.orElseThrow(() -> new RuntimeException("No target point set"));
	}
	
	@Override
	public void reset()
	{
		System.out.println("Resetting corrective drive");
		targetPoint = Optional.empty();
		targetAngle = Optional.empty();
	}
	
	@Override
	protected void execute()
	{
		if(targetPoint.isPresent())
		{
			distanceController.update();
			targetAngle = Optional.of(localization.getAngleTo(targetPoint.get()));
			
			if(targetReverse)
			{
				speed = - speed;
				targetAngle = targetAngle.map(angle -> angle.plus(Angle.REVERSE));
			}
		}
		
		if(targetAngle.isPresent())
		{
			angleController.setSetpoint(targetAngle.get().measureDegreesUnsigned());
			angleController.update();
		}
		
		driveRaw(speed, turn);
	}
}