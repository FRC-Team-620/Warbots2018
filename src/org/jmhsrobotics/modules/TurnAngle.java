package org.jmhsrobotics.modules;

import java.util.Arrays;
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
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

@CommandFactoryModule
public class TurnAngle implements Module
{
	private @Submodule Gyro gyro;
	private @Submodule Drive drive;
	private @Submodule Optional<SubsystemManager> subsystems;

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

		SmartDashboard.putNumber("P", 0.02);
		SmartDashboard.putNumber("I", 0);
		SmartDashboard.putNumber("D", 0);
		SmartDashboard.putNumber("F", 0);
	}

	public Command newInstance(Angle angle)
	{
		return new Command()
		{
			private PIDController pid;
			private DummyPIDOutput output;

			//constructor
			{
				subsystems.ifPresent(s -> requires(s.getSubsystem("DriveTrain")));
				output = new DummyPIDOutput();
				pid = new PIDController(0, 0, 0, 0, gyro.getAnglePIDSource(), output);
				pid.setContinuous();
				pid.setPercentTolerance(.25);
				pid.setInputRange(0, 360);
				pid.setOutputRange(-1, 1);
			}

			@Override
			protected void initialize()
			{
				pid.setP(SmartDashboard.getNumber("P", 0.));
				pid.setI(SmartDashboard.getNumber("I", 0.));
				pid.setD(SmartDashboard.getNumber("D", 0.));
				pid.setF(SmartDashboard.getNumber("F", 0.));
				System.out.println(pid + "[P: " + pid.getP() + "  I: " + pid.getI() + "  D: " + pid.getD() + "  F: " + pid.getF() + "]");
				pid.setSetpoint(angle.plus(gyro.getAngle()).measureDegreesUnsigned());
				System.out.println("Target: " + pid.getSetpoint() + " Current: " + gyro.getAngle());
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
		};
	}

	@Override
	public Command getTest()
	{
		return newInstance(Angle.RIGHT);
	}
}
