package org.jmhsrobotics.core.modulesystem;

import java.io.PrintStream;

import org.jmhsrobotics.core.util.PlainSendable;

import edu.wpi.first.wpilibj.command.Command;

public abstract class SensorModule extends PlainSendable implements Module
{
	public static final double DEFAULT_REFRESH_RATE = 0.02;

	private long lastTimeMeasured;
	private double refreshRate;

	public SensorModule()
	{
		refreshRate = DEFAULT_REFRESH_RATE;
		lastTimeMeasured = 0;
	}

	public abstract void updateData();

	public abstract void printData(PrintStream out);

	protected SensorModule updateIfNeeded()
	{
		long time = System.currentTimeMillis();
		if (time - lastTimeMeasured >= refreshRate * 1000)
		{
			updateData();
			lastTimeMeasured = time;
		}
		
		return this;
	}

	public void setRefreshRate(double rate)
	{
		refreshRate = rate;
	}

	public double getRefreshRate()
	{
		return refreshRate;
	}

	@Override
	public Command getTest()
	{
		return new Command()
		{
			@Override
			protected void execute()
			{
				printData(System.out);
			}

			@Override
			protected boolean isFinished()
			{
				return timeSinceInitialized() > 10;
			}
		};
	}
}