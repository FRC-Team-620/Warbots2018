package org.jmhsrobotics.modules;

import java.util.Optional;

import org.jmhsrobotics.core.modules.SubsystemManager;
import org.jmhsrobotics.core.modulesystem.ControlSchemeModule;
import org.jmhsrobotics.core.modulesystem.Submodule;

public class DriveBallMech extends ControlSchemeModule
{
	private @Submodule Optional<SubsystemManager> subsystems;
	
	@Override
	public void onLink()
	{
		subsystems.ifPresent(sm -> requires(sm.getSubsystem("Grabber")));
	}
	
	@Override
	protected void execute()
	{
		
	}
}
