package org.jmhsrobotics.modules.autonomous;

import java.util.Optional;

import org.jmhsrobotics.core.modulesystem.Submodule;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class DistrictAutoPlan extends AutoPlan
{
	private @Submodule CenterSwitchAutonomous strategyCenter;
	private @Submodule SidePreferentialScaleAutonomous strategySameSide;
	private @Submodule SideAltScaleAutonomous strategyAltSide;
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

			AutonomousCommand auto;
			switch (position)
			{
				case center:
					if (fieldLayout.isSwitchOnLeft())
						auto = strategyCenter;
					else
						auto = strategyCenter.flipField();
					break;
				case left:
					if (fieldLayout.isScaleOnLeft())
						auto = strategySameSide;
					else
						auto = strategyAltSide;
					break;
				case right:
					if (fieldLayout.isScaleOnLeft())
						auto = strategyAltSide.flipField();
					else
						auto = strategySameSide.flipField();
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
