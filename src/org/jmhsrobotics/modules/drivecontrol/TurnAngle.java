package org.jmhsrobotics.modules.drivecontrol;

import org.jmhsrobotics.core.modules.SubsystemManager;
import org.jmhsrobotics.core.modulesystem.DriveController;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.DummyPIDOutput;
import org.jmhsrobotics.core.util.PIDCalculator;
import org.jmhsrobotics.core.util.PIDSensor;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

class TurnAngle extends Command
{
	private DriveController drive;

	private Angle targetAngle;
	
	private PIDCalculator controller;
	private DummyPIDOutput output;

	public TurnAngle(DriveController drive, Localization localization, Angle angle)
	{
		this.drive = drive;
		this.targetAngle = angle;
		
		output = new DummyPIDOutput();
		SmartDashboard.putData("Output", output);

		PIDSensor input = PIDSensor.fromDispAndRate(localization::getAngleDegrees, localization::getRotationRate);

		controller = new PIDCalculator(0.02, 0, 0.03, input, output);
		controller.setInputRange(0, 360);
		controller.setOutputRange(-1, 1);
		controller.setContinuous();
		SmartDashboard.putData("PID", controller);
	}

	public TurnAngle(DriveController drive, Localization localization, Angle angle, SubsystemManager subsystems)
	{
		this(drive, localization, angle);
		requires(subsystems.getSubsystem("DriveTrain"));
	}

	@Override
	protected void initialize()
	{
		controller.setRelativeSetpoint(targetAngle.measureDegreesUnsigned());
		controller.enable();
	}

	@Override
	protected void execute()
	{
		drive.drive(0, output.get());
	}

	@Override
	protected boolean isFinished()
	{
		return controller.getAbsoluteError() < 1;
	}

	@Override
	protected void end()
	{
		controller.disable();
	}
}
