package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Point;
import org.jmhsrobotics.hardwareinterface.DriveController;
import org.jmhsrobotics.hardwareinterface.GrabberController;

public class PickUpCube extends PathNode
{
	private @Submodule DriveController drive;
	private @Submodule GrabberController grabber;
	
	private Point cubeLocation;
	
	public PickUpCube(Point location)
	{
		cubeLocation = location;
	}
	
	@Override
	protected void initialize()
	{
		drive.setTarget(cubeLocation);
	}
	
	@Override
	protected boolean isFinished()
	{
		return false;
	}
}
