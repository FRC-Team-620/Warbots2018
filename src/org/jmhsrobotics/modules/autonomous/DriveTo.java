package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Point;
import org.jmhsrobotics.hardwareinterface.DriveController;

public class DriveTo extends PathNode
{
	private @Submodule DriveController drive;
	
	private Point target;
	private double range;
	private boolean reverse;
	
	public DriveTo(Point target, double range, boolean reverse)
	{
		this.target = target;
		this.range = range;
		this.reverse = reverse;
	}
	
	public DriveTo(Point target, double range)
	{
		this(target, range, false);
	}
	
	@Override
	protected void initialize()
	{
		drive.setTarget(transform(target), reverse);
	}

	@Override
	protected boolean isFinished()
	{
		return drive.getDistanceToTargetPoint() < range;
	}
}
