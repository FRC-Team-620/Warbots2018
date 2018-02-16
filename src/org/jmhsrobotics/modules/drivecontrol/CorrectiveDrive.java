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
	
	private Optional<Point> target;
	private double speed, turn;
	
	private PIDSensor angleSensor;
	private PIDCalculator angleController;
	
	private PIDSensor distanceSensor;
	private PIDCalculator distanceController;
	
	@Override
	public void onLink()
	{
		subsystems.ifPresent(sm -> requires(sm.getSubsystem("DriveTrain")));
		
		angleSensor = PIDSensor.fromDispAndRate(localization::getAngleDegrees, localization::getRotationRate);
		angleController = new PIDCalculator(0.03, 0, 1, angleSensor, o -> turn = o);
		angleController.setInputRange(0, 360);
		angleController.setContinuous();
		angleController.setOutputRange(-.5, .5);
		
		distanceSensor = PIDSensor.fromDispAndRate(this::getDistanceToTarget, () -> localization.getSpeedToward(target.get()));
		distanceController = new PIDCalculator(0.3, 0, 1, distanceSensor, o -> speed = -o);
		distanceController.setOutputRange(-.7, .7);
		distanceController.setSetpoint(0);
		
		angleController.setName("Angle Controller PID");
		SmartDashboard.putData(angleController);
	}
	
	@Override
	@SuppressWarnings("hiding")
	public void drive(double speed, double turn)
	{	
		System.out.println("Manually driving");
		
		if((speed != 0 || turn != 0) && target.isPresent())
		{
			System.out.println("Switching to manual drive");
			target = Optional.empty();
		}
		
		this.speed = speed;
		this.turn = turn;
	}
	
	@Override
	public void setTarget(Point point)
	{
		System.out.println("Setting target to: " + point);
		target = Optional.of(point);
	}
	
	public void removeTarget()
	{
		target = Optional.empty();
		turn = speed = 0;
	}
	
	@Override
	public double getDistanceToTarget()
	{
		return localization.getDistanceTo(target.get());
	}
	
	@Override
	protected void initialize()
	{
		System.out.println("Initializing corrective drive");
		target = Optional.empty();
		localization.start();
	}
	
	@Override
	protected void execute()
	{
		if(target.isPresent())
		{
			Point targetPoint = target.get();
			
			Angle targetAngle = localization.getAngleTo(targetPoint);
			
			angleController.setSetpoint(targetAngle.measureDegreesUnsigned());
			angleController.update();
			
			distanceController.update();
		}
			
		driveRaw(speed, turn);
	}
	
	@Override
	protected void end()
	{
		localization.cancel();
	}
}