package org.jmhsrobotics.modules.autonomous;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.jmhsrobotics.core.modulesystem.AutonomousCommand;
import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.modulesystem.Submodule;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoSwitcher implements Module
{
	public static enum StartingPosition
	{
		left, right, center
	}
	
	private SendableChooser<StartingPosition> startingPosition;
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
		
		startingPosition = new SendableChooser<StartingPosition>();
		startingPosition.addDefault("Center", StartingPosition.center);
		startingPosition.addObject("Left", StartingPosition.left);
		startingPosition.addObject("Right", StartingPosition.right);
		SmartDashboard.putData("Starting Position", startingPosition);
	}

	public void start()
	{
		String gameSpecificMessage = DriverStation.getInstance().getGameSpecificMessage();
		String position = startingPosition.getSelected().toString().toUpperCase();
		String id = position.substring(0, 1) + gameSpecificMessage.substring(0, 2);
		System.out.println(startingPosition.getSelected());
		System.out.println("Starting auto command by id " + id);
		currentAuto = Optional.ofNullable(autoCommandMap.get(id));
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