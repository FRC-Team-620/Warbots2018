package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Angle;

public class AngleNode extends PathNode
{
	private Angle angle, range;
	
	public AngleNode(Angle location, Angle range)
	{
		this.angle = location;
		this.range = range;
	}
	
	Angle getAngle()
	{
		return angle;
	}
	
	Angle getRange()
	{
		return range;
	}
}
