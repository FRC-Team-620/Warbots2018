package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.modules.DriveStraightDistance;
import org.jmhsrobotics.modulesystem.AutonomousCommand;
import org.jmhsrobotics.modulesystem.Submodule;

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