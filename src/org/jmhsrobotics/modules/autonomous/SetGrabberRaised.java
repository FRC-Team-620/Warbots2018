package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.GrabberController;

public class SetGrabberRaised extends PathNode
{
	@Submodule GrabberController grabber;
	
	private boolean raised;
	
	public SetGrabberRaised(boolean raised)
	{
		this.raised = raised;
	}
	
	@Override
	protected void initialize()
	{
		grabber.setRaised(raised);
	}
	
	@Override
	protected boolean isFinished()
	{
		return true;
	}
}
