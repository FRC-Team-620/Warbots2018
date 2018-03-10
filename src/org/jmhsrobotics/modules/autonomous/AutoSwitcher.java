package org.jmhsrobotics.modules.autonomous;

import java.util.Optional;

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
		String gameSpecificMessage = DriverStation.getInstance().getGameSpecificMessage();
		boolean onLeft = gameSpecificMessage.charAt(0) == 'L';
		
		StartingPosition position = startingPosition.getSelected();
		
		
		System.out.println(startingPosition.getSelected());
		
		AutonomousCommand auto;
		switch(position)
		{
			case center:
				if(onLeft)
					auto = strategyCenter;
				else
					auto = strategyCenter.flipField();
				break;
			case left:
				if(onLeft)
					auto = strategySameSide;
				else
					auto = strategyAltSide;
				break;
			case right:
				if(onLeft)
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
			}
		};
	}
}