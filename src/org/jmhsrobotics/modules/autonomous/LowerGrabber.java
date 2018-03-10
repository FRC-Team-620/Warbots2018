package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.GrabberController;

public class LowerGrabber extends PathNode
{
	@Submodule GrabberController grabber;
	
	@Override
	protected void initialize()
	{
		grabber.setRaised(false);
	}
	
	@Override
	protected boolean isFinished()
	{
		return true;
	}
}
