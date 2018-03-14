package org.jmhsrobotics.modules.autonomous;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jmhsrobotics.core.modulesystem.Submodule;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

public class TestingAutoSwitcher extends AutoPlan
{
	private @Submodule AutonomousCommand[] allAutos;
	
	private Optional<AutonomousCommand> currentAuto;
	
	@Override
	public void onLink()
	{
		currentAuto = Optional.empty();
	}
	
	@Override
	public void start()
	{
		try
		{
			String gameSpecificMessage = DriverStation.getInstance().getGameSpecificMessage();
			
			Pattern pattern = Pattern.compile("test-(.+)");
			Matcher matcher = pattern.matcher(gameSpecificMessage);

			if (matcher.matches())
			{
				String name = matcher.group(1);
				System.out.println("Testing autonomous " + name);

				for (AutonomousCommand auto : allAutos)
					if (auto.getClass().getSimpleName().equals(name))
					{
						currentAuto = Optional.of(auto);
						auto.start();
						return;
					}

				System.out.println("Failed to find requested test auto: " + name);
				return;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void cancel()
	{
		currentAuto.ifPresent(AutonomousCommand::cancel);
	}

	@Override
	public Command getTest()
	{
		return null;
	}
}
