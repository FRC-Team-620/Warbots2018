package org.jmhsrobotics.calibration;

import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwaremodules.TurnTableHardware;
import org.jmhsrobotics.modules.PersistantDataModule;

public class CalibrateTurnTable extends CommandModule
{
	private @Submodule PersistantDataModule fileHandler;
	private @Submodule TurnTableHardware turnTable;

	protected void initialize()
	{
	}
	
	@Override
	protected void execute()
	{
	}
	
	@Override
	protected boolean isFinished()
	{
		return false;
	}
}
