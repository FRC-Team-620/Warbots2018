package org.jmhsrobotics.modules;

import java.util.Optional;

import org.jmhsrobotics.core.modules.SubsystemManager;
import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.DriveController;
import org.jmhsrobotics.core.modulesystem.Submodule;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DashboardControl extends CommandModule
{
	private @Submodule DriveController drive;
	private @Submodule Optional<SubsystemManager> subsystems;

	@Override
	public void onLink()
	{
		subsystems.ifPresent(s -> requires(s.getSubsystem("DriveTrain")));
		SmartDashboard.putNumber("Speed Val", 0.);
		SmartDashboard.putNumber("Turn Val", 0.);
		SmartDashboard.putData("Move Controlled", this);
	}

	@Override
	protected void execute()
	{
		double speed = SmartDashboard.getNumber("Speed Val", 0.);
		double turn = SmartDashboard.getNumber("Turn Val", 0.);
		drive.drive(speed, turn);
	}

	@Override
	protected boolean isFinished()
	{
		return false;
	}

	@Override
	public Command getTest()
	{
		return new Command()
		{
			private Command c = DashboardControl.super.getTest();
			
			@Override
			protected void initialize()
			{
				c.start();
			}
			
			@Override
			protected boolean isFinished()
			{
				return !c.isRunning();
			}
		};
	}
}
