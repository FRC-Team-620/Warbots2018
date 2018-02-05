package org.jmhsrobotics.modules.drivecontrol;

import org.jmhsrobotics.core.modules.SubsystemManager;
import org.jmhsrobotics.core.modulesystem.DriveController;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.DummyPIDOutput;
import org.jmhsrobotics.core.util.SensorSource;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

class TurnAngle extends Command
{
	private DriveController drive;
	private Localization localization;

	private Angle targetAngle;
	
	private PIDController pid;
	private DummyPIDOutput output;

	public TurnAngle(DriveController drive, Localization localization, Angle angle)
	{
		this.drive = drive;
		this.localization = localization;
		this.targetAngle = angle;
		
		output = new DummyPIDOutput();
		SmartDashboard.putData("Output", output);

		SensorSource source = SensorSource.fromDispAndRate(localization::getAngleDegreesUnsigned, localization::getRotationRate);

		pid = new PIDController(0.02, 0, 0.03, source, output);
		pid.setAbsoluteTolerance(0.5);
		pid.setInputRange(0, 360);
		pid.setOutputRange(-1, 1);
		pid.setContinuous();
		SmartDashboard.putData("PID", pid);
	}

	public TurnAngle(DriveController drive, Localization localization, Angle angle, SubsystemManager subsystems)
	{
		this(drive, localization, angle);
		requires(subsystems.getSubsystem("DriveTrain"));
	}

	@Override
	protected void initialize()
	{
		targetAngle = targetAngle.plus(localization.getAngle());
		pid.setSetpoint(targetAngle.measureDegreesUnsigned());
		pid.enable();
	}

	@Override
	protected void execute()
	{
		drive.drive(0, output.get());
	}

	@Override
	protected boolean isFinished()
	{
		return pid.onTarget();
	}

	@Override
	protected void end()
	{
		pid.disable();
	}
}
