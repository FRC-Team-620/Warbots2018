package org.jmhsrobotics.calibration;

import java.util.Date;

import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.TurnTableController.Position;
import org.jmhsrobotics.hardwaremodules.TurnTableHardware;
import org.jmhsrobotics.modules.PersistantDataModule;

public class CalibrateTurnTable extends CommandModule
{
	private @Submodule PersistantDataModule fileHandler;
	private @Submodule TurnTableHardware turnTable;

	@Override
	protected void execute()
	{
		if (timeSinceInitialized() < 6)
			turnTable.drive(.4);
		else turnTable.drive(-.4);
	}

	@Override
	protected boolean isFinished()
	{
		return !turnTable.readMiddleLimitSwitch();
	}

	@Override
	protected void end()
	{
		turnTable.drive(0);
		
		String[] data = new String[2];
		data[0] = fileHandler.getDateFormat().format(new Date());
		data[1] = Position.center.toString();

		try
		{
			fileHandler.write(fileHandler.getDataFile("turntable"), data);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
