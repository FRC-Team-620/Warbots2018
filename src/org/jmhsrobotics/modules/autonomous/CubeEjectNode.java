package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.hardwareinterface.DriveController;

public class CubeEjectNode implements PathNode
{
	private double x, y;
	private Angle direction;
	private boolean stop;
	
	public CubeEjectNode(double x, double y, Angle direction, boolean stop)
	{
		
	}

	@Override
	public void setTarget(DriveController drive, boolean reverse)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isFinished(DriveController drive, boolean reverse)
	{
		// TODO Auto-generated method stub
		return false;
	}
}