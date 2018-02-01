package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.modulesystem.Module;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class Localization implements Module
{	
	@Override
	public Command getTest()
	{
		return new InstantCommand()
		{
			@Override
			protected void initialize()
			{
				System.out.println("No code to test localization yet");
			}
		};
	}
}