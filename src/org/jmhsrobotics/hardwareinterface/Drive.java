package org.jmhsrobotics.hardwareinterface;

import edu.wpi.first.wpilibj.command.Command;

public interface Drive
{
	public void drive(double speed, double turn);

	public static Command makeTest(Drive d)
	{
		return new Command()
		{
			@Override
			protected void execute()
			{
				if (timeSinceInitialized() < 3)
					d.drive(0.7, 0);
				else d.drive(0, 0.7);
			}

			@Override
			protected boolean isFinished()
			{
				return timeSinceInitialized() > 5;
			}
		};
	}
}