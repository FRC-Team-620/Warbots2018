package org.jmhsrobotics.modules;

import java.util.Optional;

import org.jmhsrobotics.core.modules.SubsystemManager;
import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.modulesystem.annotations.CommandFactoryModule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.DummyPIDOutput;
import org.jmhsrobotics.hardwareinterface.Drive;
import org.jmhsrobotics.hardwareinterface.Gyro;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

@CommandFactoryModule
public class TurnAngle implements Module
{
	private @Submodule Gyro gyro;
	private @Submodule Drive drive;
	private @Submodule Optional<SubsystemManager> subsystems;

	private PIDController pid;
	private DummyPIDOutput output;
	
	@Override
	public void onLink()
	{
		int[] turnTests = { 90, -90, 180, 45 };
		Command[] turnTestCommands = new Command[turnTests.length];
		for (int i = 0; i < turnTests.length; ++i)
		{
			int ang = turnTests[i];
			turnTestCommands[i] = newInstance(Angle.fromDegrees(ang));
			SmartDashboard.putData("Turn " + ang, turnTestCommands[i]);
		}
		
		output = new DummyPIDOutput();
		SmartDashboard.putData("Turn Output", output);
		
		pid = new PIDController(0.02, 0, 0.04, gyro.getAnglePIDSource(), output);
		pid.setAbsoluteTolerance(1);
		pid.setInputRange(0, 360);
		pid.setOutputRange(-1, 1);
		pid.setContinuous();
		SmartDashboard.putData("Turn PID", pid);
	}

	public Command newInstance(Angle angle)
	{
		return new TurnAngleCommand(angle);
	}

	class TurnAngleCommand extends Command
	{
		private Angle turnAngle;
		
		//constructor
		public TurnAngleCommand(Angle angle)
		{
			turnAngle = angle;
			subsystems.ifPresent(s -> requires(s.getSubsystem("DriveTrain")));
		}

		@Override
		protected void initialize()
		{
			gyro.setPIDSourceType(PIDSourceType.kDisplacement);
			pid.setSetpoint(turnAngle.plus(gyro.getAngle()).measureDegreesUnsigned());
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

	@Override
	public Command getTest()
	{
		return newInstance(Angle.RIGHT);
	}
}