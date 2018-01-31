package org.jmhsrobotics.modules;

import java.util.Optional;

import org.jmhsrobotics.core.modules.SubsystemManager;
import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.modulesystem.annotations.CommandFactoryModule;
import org.jmhsrobotics.core.util.DummyPIDOutput;
import org.jmhsrobotics.hardwareinterface.Drive;
import org.jmhsrobotics.hardwareinterface.Gyro;
import org.jmhsrobotics.hardwareinterface.WheelEncodersInterface;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

@CommandFactoryModule
public class DriveDistance implements Module
{
	private @Submodule Gyro gyro;
	private @Submodule Drive drive;
	private @Submodule WheelEncodersInterface encoders;
	private @Submodule Optional<SubsystemManager> subsystems;
	
	private PIDController dirPid, distPid;
	private DummyPIDOutput dirOutput, distOutput;
	
	@Override
	public void onLink()
	{
		int[] distTests = { 200, 500, 1000, 2000 };
		Command[] distTestCommands = new Command[distTests.length];
		for (int i = 0; i < distTests.length; ++i)
		{
			int dist = distTests[i];
			distTestCommands[i] = newInstance(dist);
			SmartDashboard.putData("Drive " + dist, distTestCommands[i]);
		}
		
		dirOutput = new DummyPIDOutput();
		SmartDashboard.putData("Dir Output", dirOutput);
		distOutput = new DummyPIDOutput();
		SmartDashboard.putData("Dist Output", distOutput);
		
		dirPid = new PIDController(0.01, 0, 0.01, gyro.getAnglePIDSource(), dirOutput);
		dirPid.setAbsoluteTolerance(10);
		dirPid.setInputRange(0, 360);
		dirPid.setOutputRange(-1, 1);
		dirPid.setContinuous();
		SmartDashboard.putData("Dir PID", dirPid);
		
		distPid = new PIDController(0.02, 0, 0.04, encoders.average().getPIDSource(), distOutput);
		distPid.setAbsoluteTolerance(0.5);
		distPid.setOutputRange(-1, 1);
		SmartDashboard.putData("Dist PID", distPid);
	}
	
	public Command newInstance(double dist)
	{
		return new DriveDistanceCommand(dist);
	}
	
	class DriveDistanceCommand extends Command
	{
		private double targetDist;
		
		public DriveDistanceCommand(double dist)
		{
			targetDist = dist;
			subsystems.ifPresent(s -> requires(s.getSubsystem("DriveTrain")));
		}
		
		@Override
		protected void initialize()
		{
			encoders.setPIDSourceType(PIDSourceType.kDisplacement);
			gyro.setPIDSourceType(PIDSourceType.kDisplacement);
			
			dirPid.setSetpoint(gyro.getAngle().measureDegreesUnsigned());
			dirPid.enable();
			distPid.setSetpoint(targetDist + encoders.average().getDist());
			distPid.enable();
		}
		
		@Override
		protected void execute()
		{
			drive.drive(distOutput.get(), dirOutput.get());
		}
		
		@Override
		protected boolean isFinished()
		{
			return distPid.onTarget();
		}
		
		@Override
		protected void end()
		{
			dirPid.disable();
			distPid.disable();
		}
	}
	
	@Override
	public Command getTest()
	{
		return newInstance(500);
	}
}
