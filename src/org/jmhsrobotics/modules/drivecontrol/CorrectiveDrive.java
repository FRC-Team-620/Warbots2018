package org.jmhsrobotics.modules.drivecontrol;

import java.util.Optional;

import org.jmhsrobotics.core.modules.SubsystemManager;
import org.jmhsrobotics.core.modulesystem.DriveController;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.DummyPIDOutput;
import org.jmhsrobotics.core.util.PIDCalculator;
import org.jmhsrobotics.core.util.PIDSensor;
import org.jmhsrobotics.core.util.Point;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CorrectiveDrive extends DriveController
{
	private @Submodule Optional<SubsystemManager> subsystems;
	private @Submodule Localization localization;
	
	private Optional<Point> target;
	private double speed, turn;
	
	private PIDSensor angleSensor;
	private DummyPIDOutput angleOutput;
	private PIDCalculator angleController;
	
	private PIDSensor distanceSensor;
	private DummyPIDOutput distanceOutput;
	private PIDController distanceController;
	
	@Override
	public void onLink()
	{
		subsystems.ifPresent(sm -> requires(sm.getSubsystem("DriveTrain")));
		
		angleSensor = PIDSensor.fromDispAndRate(localization::getAngleDegrees, localization::getRotationRate);
		angleOutput = new DummyPIDOutput();
		angleController = new PIDCalculator(0.03, 0, 0.5, angleSensor, angleOutput);
		angleController.setInputRange(0, 360);
		angleController.setContinuous();
		angleController.setOutputRange(-.5, .5);
		
		angleController.setName("Angle Controller PID");
		SmartDashboard.putData(angleController);
	}
	
	@Override
	@SuppressWarnings("hiding")
	public void drive(double speed, double turn)
	{	
		if(speed != 0 || turn != 0)
			target = Optional.empty();
		
		this.speed = speed;
		this.turn = turn;
	}
	
	@Override
	public void setTarget(double x, double y)
	{
		target = Optional.of(new Point(x, y));
	}
	
	@Override
	protected void initialize()
	{
		target = Optional.empty();
		localization.start();
	}
	
	@Override
	protected void execute()
	{
		if(target.isPresent())
		{
			Point targetPoint = target.get();
			System.out.println("Pointing at " + targetPoint);
			double xOffset = targetPoint.getX() - localization.getX();
			double yOffset = targetPoint.getY() - localization.getY();
			double targetAngle = Angle.fromRiseAndRun(xOffset, yOffset).measureDegreesUnsigned();
			System.out.println(targetAngle);
			angleController.setSetpoint(targetAngle);
			angleController.update();
			driveRaw(0, angleOutput.get());
		}
		else
			driveRaw(speed, turn);
	}
	
	@Override
	protected void end()
	{
		localization.cancel();
	}
}