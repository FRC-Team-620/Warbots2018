package org.jmhsrobotics.modules.autonomous;

import java.util.Optional;

import org.jmhsrobotics.core.modulesystem.Submodule;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class TestAutoSwitching extends AutoPlan
{
	private @Submodule CenterSwitchAutonomous strategyCenter;
	private @Submodule SidePreferentialSwitchAutonomous strategySameSide;
	private @Submodule SideAltSwitchAutonomous strategyAltSide;
	private @Submodule CrossAutoLineAutonomous strategyError;

	private Optional<AutonomousCommand> currentAuto;

	@Override
	public void onLink()
	{
		currentAuto = Optional.empty();
	}

	public void start()
	{
		try
		{
			FieldLayout fieldLayout = readFieldLayout();
			StartingPosition position = getStartingPosition();
			boolean onLeft = fieldLayout.isSwitchOnLeft();

			System.out.println("Switch On Left: " + fieldLayout.isSwitchOnLeft());
			System.out.println("Starting Position: " + position);
			
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
						auto = strategyAltSide.flipField();
					else
						auto = strategySameSide.flipField();
					break;
				default:
					auto = strategyError;
					break;
			}
			
			System.out.println("Selected auto: " + auto);
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
