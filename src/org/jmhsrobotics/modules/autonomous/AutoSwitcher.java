package org.jmhsrobotics.modules.autonomous;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.modulesystem.Submodule;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoSwitcher implements Module
{
	private @Submodule CenterSwitchAutonomous strategyCenter;
	private @Submodule SideAltSwitchAutonomous strategySameSide;
	private @Submodule SidePreferentialSwitchAutonomous strategyAltSide;
	private @Submodule CrossAutoLineAutonomous strategyError;

	private @Submodule AutonomousCommand[] allAutos;

	public static enum StartingPosition
	{
		left, right, center
	}

	private SendableChooser<StartingPosition> startingPosition;
	private Optional<AutonomousCommand> currentAuto;

	@Override
	public void onLink()
	{
		currentAuto = Optional.empty();

		startingPosition = new SendableChooser<StartingPosition>();
		startingPosition.addObject("Center", StartingPosition.center);
		startingPosition.addDefault("Left", StartingPosition.left);
		startingPosition.addObject("Right", StartingPosition.right);
		SmartDashboard.putData("Starting Position", startingPosition);
	}

	public void start()
	{
		try
		{
			String gameSpecificMessage = DriverStation.getInstance().getGameSpecificMessage();
			
			try
			{
				Pattern pattern = Pattern.compile("test-(.+)");
				Matcher matcher = pattern.matcher(gameSpecificMessage);
				
				if(matcher.matches())
				{
					String name = matcher.group(1);
					System.out.println("Testing autonomous " + name);
					
					for(AutonomousCommand auto : allAutos)
						if(auto.getClass().getSimpleName().equals(name))
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
			
			boolean onLeft = gameSpecificMessage.charAt(0) == 'L';

			StartingPosition position = startingPosition.getSelected();

			System.out.println(startingPosition.getSelected());

			AutonomousCommand auto;
			switch (position)
			{
				case center:
					if (onLeft)
						auto = strategyCenter;
					else
						auto = strategyCenter.flipField();
					break;
				case left:
					if (onLeft)
						auto = strategySameSide;
					else
						auto = strategyAltSide;
					break;
				case right:
					if (onLeft)
						auto = strategySameSide.flipField();
					else
						auto = strategyAltSide.flipField();
					break;
				default:
					auto = strategyError;
					break;
			}
			currentAuto = Optional.of(auto);
			auto.start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			try
			{
				strategyError.start();
				currentAuto = Optional.of(strategyError);
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
				allAutos[0].start();
				currentAuto = Optional.of(allAutos[0]);
			}
		}
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
			{}
		};
	}
}