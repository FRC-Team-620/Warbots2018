package org.jmhsrobotics.modules.testcommands;

import org.jmhsrobotics.core.modules.SubsystemManager;
import org.jmhsrobotics.hardwareinterface.DriveController;

import edu.wpi.first.wpilibj.command.Command;

public class DriftTest extends Command
{
	private DriveController drive;
	
	public DriftTest(DriveController drive)
	{
		this.drive = drive;
	}
	
	public DriftTest(DriveController drive, SubsystemManager subsystems)
	{
		this(drive);
		requires(subsystems.getSubsystem("DriveTrain"));
	}
	
	@Override
	protected void execute()
	{
		if(timeSinceInitialized() < 2)
			drive.drive(1, 0);
		else
			drive.drive(0, 1);
	}
	
	@Override
	protected boolean isFinished()
	{
		return timeSinceInitialized() > 3;
	}
}
