package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.DriveController;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.WheelEncodersInterface;

public class CalibrateDriveTrain extends CommandModule
{
	private @Submodule DriveController driveTrain;
	private @Submodule WheelEncodersInterface encoders;
	
	@Override
	protected void initialize()
	{
	}
	
	@Override
	protected void execute()
	{
		encoders.left().getRate(); //get speed of left pid
		encoders.average().getDist(); //get average dist of both encoders
		driveTrain.drive(0.5, 0.5); //drive the bot
	}
	
	@Override
	protected boolean isFinished()
	{
		return false;
	}
}
