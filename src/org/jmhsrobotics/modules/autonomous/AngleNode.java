package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.hardwareinterface.DriveController;

public class AngleNode implements PathNode
{
	private Angle angle, range;
	
	public AngleNode(Angle location, Angle range)
	{
		this.angle = location;
		this.range = range;
	}

	@Override
	public void setTarget(DriveController drive, boolean reverse)
	{
		drive.setTarget(angle);
	}

	@Override
	public boolean isFinished(DriveController drive, boolean reverse)
	{
		return drive.getDistanceToTargetAngle().compareTo(range) <= 0;
	}
}