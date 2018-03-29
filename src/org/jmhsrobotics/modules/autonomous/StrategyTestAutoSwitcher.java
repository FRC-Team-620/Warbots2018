package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.modulesystem.Sublinker;

import edu.wpi.first.wpilibj.command.Command;

public class StrategyTestAutoSwitcher extends AutoPlan
{
	private AutonomousCommand thingToTest;
	
	public StrategyTestAutoSwitcher(AutonomousCommand thingToTest)
	{
		this.thingToTest = thingToTest;
	}
	
	@Override
	public void onLink(Sublinker linker)
	{
		linker.link(thingToTest);
	}
	
	@Override
	public Command getTest()
	{
		return null;
	}

	@Override
	public void start()
	{
		thingToTest.start();
	}

	@Override
	public void cancel()
	{
		thingToTest.cancel();
	}
}
