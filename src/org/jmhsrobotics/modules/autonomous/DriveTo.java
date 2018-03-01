package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.DriveController;

public class DriveTo extends CommandModule
{
	private @Submodule DriveController drive;
	
	private double x, y, range;
	private boolean reverse;
	
	public DriveTo(double x, double y, double range, boolean reverse)
	{
		this.x = x;
		this.y = y;
		this.range = range;
		this.reverse = reverse;
	}
	
	public DriveTo(double x, double y, double range)
	{
		this(x, y, range, false);
	}
	
	@Override
	protected void initialize()
	{
		drive.setTarget(x, y, reverse);
	}

	@Override
	protected boolean isFinished()
	{
		return drive.getDistanceToTargetPoint() < range;
	}
}
