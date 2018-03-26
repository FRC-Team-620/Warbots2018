package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.GrabberController;
import org.jmhsrobotics.hardwareinterface.GrabberController.Position;

public class CubeDrop extends PathNode
{
	private @Submodule GrabberController grabber;
	
	@Override
	protected void execute()
	{
		grabber.setLeftArm(Position.extended);
		grabber.setRightArm(Position.extended);
	}
	
	@Override
	protected boolean isFinished()
	{
		return timeSinceInitialized() > 1;
	}
}
