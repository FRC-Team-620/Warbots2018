package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.modulesystem.AutonomousCommand;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.modules.DriveStraightDistance;

public class DriveStraightAutonomous extends AutonomousCommand
{
	private @Submodule DriveStraightDistance dsDistFactory;
	
	@Override
	public void onLink()
	{
		addSequential(dsDistFactory.newInstance(5));
	}

	@Override
	public String getID()
	{
		return "stuff";
	}
}