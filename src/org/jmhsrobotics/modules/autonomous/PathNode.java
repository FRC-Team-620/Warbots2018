package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.hardwareinterface.DriveController;

public interface PathNode
{
	public void setTarget(DriveController drive, boolean reverse);
	public boolean isFinished(DriveController drive, boolean reverse);
	
	public default void end()
	{
		
	}
}
