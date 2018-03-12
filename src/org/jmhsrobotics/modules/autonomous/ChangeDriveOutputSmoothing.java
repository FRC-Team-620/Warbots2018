package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.DriveController;
import org.jmhsrobotics.modules.drivecontrol.OutputSmoother;

public class ChangeDriveOutputSmoothing extends PathNode
{
	private @Submodule DriveController drive;

	private OutputSmoother speed;
	private OutputSmoother turn;
	
	public ChangeDriveOutputSmoothing(OutputSmoother speedSmoother, OutputSmoother turnSmoother)
	{
		speed = speedSmoother;
		turn = turnSmoother;
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