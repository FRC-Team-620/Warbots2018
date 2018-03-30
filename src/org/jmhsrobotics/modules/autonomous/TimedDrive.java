package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.hardwareinterface.DriveController;

public class TimedDrive extends PathNode
{
	private @Submodule DriveController drive;
	
	private double time;
	
	public TimedDrive(double time)
	{
		this.time = time;
	}
	
	@Override
	protected void initialize()
	{
		System.out.println("Starting Timed Drive");
		// TODO Auto-generated method stub
		super.initialize();
	}
	
	@Override
	protected void execute()
	{
		drive.drive(.7, Angle.ZERO);
	}

	@Override
	protected boolean isFinished()
	{
		return timeSinceInitialized() > time;
	}
	
	@Override
	protected void end()
	{
		drive.drive(0, 0);
	}
}