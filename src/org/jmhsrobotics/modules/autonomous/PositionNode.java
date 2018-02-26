package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Point;
import org.jmhsrobotics.hardwareinterface.DriveController;

public class PositionNode implements PathNode
{
	private Point location;
	private double range;
	
	public PositionNode(double x, double y, double range)
	{
		location = new Point(x, y);
		this.range = range;
	}
	
	@Override
	public void setTarget(DriveController drive, boolean reverse)
	{
		drive.setTarget(location, reverse);
	}
	
	@Override
	public boolean isFinished(DriveController drive, boolean reverse)
	{
		return drive.getDistanceToTargetPoint() < range;
	}
}
