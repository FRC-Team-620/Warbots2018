package org.jmhsrobotics.modules;

import java.util.Optional;

import org.jmhsrobotics.core.modules.SubsystemManager;
import org.jmhsrobotics.core.modulesystem.ControlSchemeModule;
import org.jmhsrobotics.core.modulesystem.DriveController;
import org.jmhsrobotics.core.modulesystem.Submodule;

import edu.wpi.first.wpilibj.XboxController;

public class DriveWithXbox extends ControlSchemeModule
{
	private @Submodule Optional<SubsystemManager> subsystems;
	private @Submodule DriveController drive;
	
	@Override
	public void onLink()
	{
		subsystems.ifPresent(s -> requires(s.getSubsystem("DriveTrain")));
	}
	
	@Override
	public void execute()
	{
		XboxController xbox = getOI().getXboxControllers().get(0);
	}
}
