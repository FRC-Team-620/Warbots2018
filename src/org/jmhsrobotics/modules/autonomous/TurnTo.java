package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.hardwareinterface.DriveController;

public class TurnTo extends CommandModule
{
	private @Submodule DriveController drive;
	
	private Angle target, range;
	
	public TurnTo(Angle target, Angle range)
	{
		this.target = target;
		this.range = range;
	}
	
	@Override
	protected void initialize()
	{
		drive.setTarget(target);
	}
	
	@Override
	protected boolean isFinished()
	{
		return drive.getDistanceToTargetAngle().compareTo(range) <= 0;
	}
}