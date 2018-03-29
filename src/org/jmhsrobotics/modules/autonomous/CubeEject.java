package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.GrabberController;

public class CubeEject extends PathNode
{
	private @Submodule GrabberController grabber;
	
	@Override
	protected void initialize()
	{
		grabber.extake();
	}
	
	@Override
	protected boolean isFinished()
	{
		return !grabber.isExtaking();
	}
}
