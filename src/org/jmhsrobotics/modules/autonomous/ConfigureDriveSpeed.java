package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.DriveController;
import org.jmhsrobotics.modules.drivecontrol.OutputSmoother;

public class ConfigureDriveSpeed extends PathNode
{
	private @Submodule DriveController drive;
	
	private OutputSmoother speed, turn;
	
	public ConfigureDriveSpeed(OutputSmoother speed, OutputSmoother turn)
	{
		this.speed = speed;
		this.turn = turn;
	}
	
	@Override
	protected void initialize()
	{
		drive.setSpeedOutputSmoother(speed);
		drive.setTurnOutputSmoother(turn);
	}
	
	@Override
	protected boolean isFinished()
	{
		return true;
	}
}
