package org.jmhsrobotics.modules;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.jmhsrobotics.modulesystem.AutonomousCommand;
import org.jmhsrobotics.modulesystem.Module;
import org.jmhsrobotics.modulesystem.Submodule;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class AutoSwitcher implements Module
{
	private @Submodule AutonomousCommand[] autoCommands;
	private Map<String, AutonomousCommand> autoCommandMap;
	private Optional<AutonomousCommand> currentAuto;

	@Override
	public void onLink()
	{
		autoCommandMap = new HashMap<>();
		for (AutonomousCommand c : autoCommands)
			autoCommandMap.put(c.getID(), c);
		autoCommands = null;

		currentAuto = Optional.empty();
	}

	public void start()
	{
		String gameSpecificMessage = DriverStation.getInstance().getGameSpecificMessage();
		currentAuto = Optional.ofNullable(autoCommandMap.get(gameSpecificMessage));
		currentAuto.get().start();
	}

	public void cancel()
	{
		currentAuto.ifPresent(AutonomousCommand::cancel);
	}

	@Override
	public Command getTest()
	{
		return new InstantCommand()
		{
			@Override
			protected void initialize()
			{
				System.out.println("Loaded autonomous routines:" + autoCommandMap);
			}
		};
	}
}