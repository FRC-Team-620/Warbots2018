package org.jmhsrobotics.modules.autonomous;

import java.util.Optional;

import org.jmhsrobotics.core.modulesystem.Submodule;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class TwoPrismAutoSwitcher extends AutoPlan
{
	private @Submodule CenterSwitchAutonomous strategyCenter;
	
	private @Submodule SidePreferentialScalePreferentialSwitchAutonomous strategySpp;
	private @Submodule SidePreferentialScaleAltSwitchAutonomous strategySpa;
	private @Submodule SideAltScalePreferentialSwitchAutonomous strategySap;
	private @Submodule SideAltScaleAltSwitchAutonomous strategySaa;
	
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
			boolean switchOnLeft = fieldLayout.isSwitchOnLeft();
			boolean scaleOnLeft = fieldLayout.isScaleOnLeft();

			AutonomousCommand auto;
			switch (position)
			{
				case center:
					if (switchOnLeft)
						auto = strategyCenter;
					else
						auto = strategyCenter.flipField();
					break;
				case left:
					if (scaleOnLeft)
						if (switchOnLeft)
							auto = strategySpp;
						else
							auto = strategySpa;
					else
						if (switchOnLeft)
							auto = strategySap;
						else
							auto = strategySaa;
					break;
				case right:
					if (scaleOnLeft)
						if (switchOnLeft)
							auto = strategySaa.flipField();
						else
							auto = strategySap.flipField();
					else
						if (switchOnLeft)
							auto = strategySpa.flipField();
						else
							auto = strategySpp.flipField();
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
			strategyError.start();
			currentAuto = Optional.of(strategyError);
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