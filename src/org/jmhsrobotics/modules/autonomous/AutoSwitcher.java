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
	private @Submodule CenterSameSideAutonomous strategyCpp;
	private @Submodule CenterDifferentSideAutonomous strategyCpn;
	private @Submodule SideAltSwitchPreferentialScaleAutonomous strategySnp;
	private @Submodule SidePreferentialSwitchScaleAutonomous strategySpp;
	private @Submodule SidePreferentialSwitchAltScaleAutonomous strategySpn;
	private @Submodule SideAltSwitchScaleAutonomous strategySnn;
	private @Submodule CrossAutoLine strategyProblem;
	
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
		boolean switchOnLeft = gameSpecificMessage.charAt(0) == 'L';
		boolean scaleOnLeft = gameSpecificMessage.charAt(1) == 'L';
		
		StartingPosition position = startingPosition.getSelected();
		
		
		System.out.println(startingPosition.getSelected());
		
		AutonomousCommand auto;
		switch(position)
		{
			case center:
				if(switchOnLeft)
					if(scaleOnLeft)
						auto = strategyCpp;
					else
						auto = strategyCpn;
				else
					if(scaleOnLeft)
						auto = strategyCpn.flipField();
					else
						auto = strategyCpp.flipField();
				break;
			case left:
				if(switchOnLeft)
					if(scaleOnLeft)
						auto = strategySpp;
					else
						auto = strategySpn;
				else
					if(scaleOnLeft)
						auto = strategySnp;
					else
						auto = strategySnn;
				break;
			case right:
				if(switchOnLeft)
					if(scaleOnLeft)
						auto = strategySnn.flipField();
					else
						auto = strategySnp.flipField();
				else
					if(scaleOnLeft)
						auto = strategySpn.flipField();
					else
						auto = strategySpp.flipField();
				break;
			default:
				auto = strategyProblem;
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