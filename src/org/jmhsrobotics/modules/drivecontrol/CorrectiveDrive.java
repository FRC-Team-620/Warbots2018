package org.jmhsrobotics.modules.drivecontrol;

import org.jmhsrobotics.core.modulesystem.DriveController;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Angle;

import edu.wpi.first.wpilibj.command.Command;

public class CorrectiveDrive extends DriveController
{
	private @Submodule Localization localization;
	
	private boolean enabled;
	
	@Override
	public void onLink()
	{
	}
	
	@Override
	public void enable()
	{
		if(enabled)
			return;
		
		localization.enable();
		enabled = true;
	}
	
	@Override
	public void disable()
	{
		if(!enabled)
			return;
		
		localization.disable();
		enabled = false;
	}
	
	@Override
	public void drive(double speed, double turn)
	{	
		if(!enabled)
			throw new RuntimeException("Attempted to drive without enabling corrective drive");
		
		driveRaw(speed, turn);
	}
	
	@Override
	public Command getDriveCommand(double dist)
	{
		return new DriveDistance(this, localization, dist);
	}
	
	@Override
	public Command getTurnCommand(Angle angle)
	{
		return new TurnAngle(this, localization, angle);
	}
}