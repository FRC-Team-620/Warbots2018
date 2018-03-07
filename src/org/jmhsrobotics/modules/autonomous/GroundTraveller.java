package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.ElevatorController;

public class GroundTraveller extends CommandModule
{
	private @Submodule ElevatorController elevator;
	
	@Override
	protected boolean isFinished()
	{
		// TODO Auto-generated method stub
		return false;
	}
}