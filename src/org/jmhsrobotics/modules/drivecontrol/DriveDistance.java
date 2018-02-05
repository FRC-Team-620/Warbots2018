package org.jmhsrobotics.modules.drivecontrol;

import org.jmhsrobotics.core.modules.SubsystemManager;
import org.jmhsrobotics.core.modulesystem.DriveController;
import org.jmhsrobotics.core.util.DummyPIDOutput;
import org.jmhsrobotics.core.util.SensorSource;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

class DriveDistance extends Command
{
	private DriveController drive;
//	private Localization localization;

	//	private Angle lockAngle;
	private double targetDistance;

	private PIDController /*dirPid,*/ distPid;
	private DummyPIDOutput /*dirOutput,*/ distOutput;

	public DriveDistance(DriveController drive, Localization localization, double dist)
	{
		this.drive = drive;
//		this.localization = localization;
		this.targetDistance = dist;

		//		dirOutput = new DummyPIDOutput();
		//		SmartDashboard.putData("Dir Output", dirOutput);
		distOutput = new DummyPIDOutput();
		SmartDashboard.putData("Dist Output", distOutput);

		//		DummyPIDSource dirSource = DummyPIDSource.fromDispAndRate(localization::getAngleDegreesUnsigned, localization::getRotationRate);
		SensorSource distSource = SensorSource.fromDispAndRate(() -> localization.getPos(drive.getLockAngle().get()), () -> localization.getSpeed(drive.getLockAngle().get()));

		//		dirPid = new PIDController(0.01, 0, 0.01, dirSource, dirOutput);
		//		dirPid.setAbsoluteTolerance(0.5);
		//		dirPid.setInputRange(0, 360);
		//		dirPid.setOutputRange(-1, 1);
		//		dirPid.setContinuous();
		//		SmartDashboard.putData("Dir PID", dirPid);

		distPid = new PIDController(0.02, 0, 0.04, distSource, distOutput);
		distPid.setAbsoluteTolerance(10);
		distPid.setOutputRange(-1, 1);
		SmartDashboard.putData("Dist PID", distPid);
	}

	public DriveDistance(DriveController drive, Localization localization, double dist, SubsystemManager subsystems)
	{
		this(drive, localization, dist);
		requires(subsystems.getSubsystem("DriveTrain"));
	}

	@Override
	protected void initialize()
	{
//		lockAngle = localization.getAngle();
//		targetDistance += localization.getPos(lockAngle);
//		dirPid.setSetpoint(lockAngle.measureDegreesUnsigned());
		distPid.setSetpoint(targetDistance);
//		dirPid.enable();
		distPid.enable();
	}

	@Override
	protected void execute()
	{
		drive.drive(distOutput.get(), /*dirOutput.get()*/ 0);
	}

	@Override
	protected boolean isFinished()
	{
		return distPid.onTarget();
	}

	@Override
	protected void end()
	{
//		dirPid.disable();
		distPid.disable();
	}
}
