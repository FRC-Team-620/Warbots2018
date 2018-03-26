package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.PerpetualCommand;

import edu.wpi.first.wpilibj.command.Command;

public class SpikeCompressor extends CommandModule implements PerpetualCommand
{

	@Override
	public void reset()
	{
	}

	@Override
	public Command getTest()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isFinished()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
