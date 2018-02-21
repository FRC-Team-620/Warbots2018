package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Point;

public class PositionNode extends PathNode
{
	private Point location;
	private double range;
	
	public PositionNode(double x, double y, double range)
	{
		location = new Point(x, y);
		this.range = range;
	}
	
	Point getLocation()
	{
		return location;
	}
	
	double getRange()
	{
		return range;
	}
}
