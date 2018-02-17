package org.jmhsrobotics.modules.drivecontrol;

import java.util.Optional;

import org.jmhsrobotics.core.modules.SubsystemManager;
import org.jmhsrobotics.core.modulesystem.DriveController;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.PIDCalculator;
import org.jmhsrobotics.core.util.PIDSensor;
import org.jmhsrobotics.core.util.Point;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CorrectiveDrive extends DriveController
{
	private @Submodule Optional<SubsystemManager> subsystems;
	private @Submodule Localization localization;
	
	private Optional<Point> targetPoint;
	private Optional<Angle> targetAngle;
	
	private PIDSensor angleSensor;
	private PIDCalculator angleController;
	
	private PIDSensor distanceSensor;
	private PIDCalculator distanceController;
	
	private double speed, turn;
	
	@Override
	public void onLink()
	{
		subsystems.ifPresent(sm -> requires(sm.getSubsystem("DriveTrain")));
		
		angleSensor = PIDSensor.fromDispAndRate(localization::getAngleDegrees, localization::getDegreesPerSecond);
		angleController = new PIDCalculator(0.02, 0, 7, angleSensor, o -> turn = o);
		angleController.setInputRange(0, 360);
		angleController.setContinuous();
		angleController.setOutputRange(-1, 1);
		
		distanceSensor = PIDSensor.fromDispAndRate(this::getDistanceToTargetPoint, this::getSpeedTowardTarget);
		distanceController = new PIDCalculator(0.005, 0, 5, distanceSensor, o -> speed = -o);
		distanceController.setOutputRange(-1, 1);
		distanceController.setSetpoint(0);
		
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
	public void setTarget(Point point)
	{
		System.out.println("Setting target to: " + point);
		targetPoint = Optional.of(point);
	}
	
	public void setRelativeTarget(Point point)
	{
		setTarget(localization.getX() + point.getX(), localization.getY() + point.getY());
	}
	
	@Override
	public void setTarget(Angle angle)
	{
		System.out.println("Setting target to angle: " + angle);
		targetPoint = Optional.empty();
		targetAngle = Optional.of(angle);
	}
	
	public void setRelativeTarget(Angle angle)
	{
		setTarget(localization.getAngle().plus(angle));
	}
	
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
	protected void initialize()
	{
		System.out.println("Initializing corrective drive");
		targetPoint = Optional.empty();
		targetAngle = Optional.empty();
		localization.start();
	}
	
	@Override
	protected void execute()
	{
		if(targetPoint.isPresent())
		{
			distanceController.update();
			targetAngle = Optional.of(localization.getAngleTo(targetPoint.get()));
		}
		
		if(targetAngle.isPresent())
		{
			angleController.setSetpoint(targetAngle.get().measureDegreesUnsigned());
			angleController.update();
		}
		
		driveRaw(speed, turn);
	}
	
	@Override
	protected void end()
	{
		localization.cancel();
	}
}