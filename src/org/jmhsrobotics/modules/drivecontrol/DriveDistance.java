package org.jmhsrobotics.modules.drivecontrol;

import org.jmhsrobotics.core.modules.SubsystemManager;
import org.jmhsrobotics.core.modulesystem.DriveController;
import org.jmhsrobotics.core.util.DummyPIDOutput;
import org.jmhsrobotics.core.util.PIDCalculator;
import org.jmhsrobotics.core.util.PIDSensor;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

class DriveDistance extends Command
{
	private DriveController drive;

	private double targetDistance;

	private PIDCalculator controller;
	private DummyPIDOutput output;

	public DriveDistance(DriveController drive, Localization localization, double dist)
	{
		this.drive = drive;
		this.targetDistance = dist;

		output = new DummyPIDOutput();
		SmartDashboard.putData("Output", output);

		PIDSensor input = PIDSensor.fromDispAndRate(localization::getTotalDistanceDriven, localization::getSpeed);
		
		controller = new PIDCalculator(0.02, 0, 0.04, input, output);
		controller.setOutputRange(-1, 1);
		SmartDashboard.putData("PID Calculator", controller);
	}

	public DriveDistance(DriveController drive, Localization localization, double dist, SubsystemManager subsystems)
	{
		this(drive, localization, dist);
		requires(subsystems.getSubsystem("DriveTrain"));
	}

	@Override
	protected void initialize()
	{
		controller.setRelativeSetpoint(targetDistance);
		controller.enable();
	}

	@Override
	protected void execute()
	{
		drive.drive(output.get(), 0);
	}

	@Override
	protected boolean isFinished()
	{
		return controller.getAbsoluteError() < 10;
	}

	@Override
	protected void end()
	{
		controller.disable();
	}
}
