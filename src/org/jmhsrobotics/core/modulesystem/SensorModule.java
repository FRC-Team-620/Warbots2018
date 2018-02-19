package org.jmhsrobotics.core.modulesystem;

import java.io.PrintStream;

import org.jmhsrobotics.core.util.PlainSendable;
import org.jmhsrobotics.hardwareinterface.Sensor;

import edu.wpi.first.wpilibj.command.Command;

public abstract class SensorModule extends PlainSendable implements Sensor, Module
{
	public static final double DEFAULT_CACHE_PERIOD = 0.02;
	
	private DummySendableBuilder relevantData;
	
	private long lastTimeMeasured;
	private double cachePeriod;

	public SensorModule()
	{
		cachePeriod = DEFAULT_CACHE_PERIOD;
		lastTimeMeasured = 0;
	}

	public abstract void updateData();

	public void printData(PrintStream out)
	{
		if(relevantData == null)
		{
			relevantData = new DummySendableBuilder();
			initSendable(relevantData);
		}
		
		out.println("-- " + relevantData.getType() + " --");
		relevantData.entries().forEach(e -> out.println("\t" + e.getName() + " = " + e.getValue()));
	}

	protected SensorModule updateIfNeeded()
	{
		long time = System.currentTimeMillis();
		if (time - lastTimeMeasured >= cachePeriod * 1000)
		{
			updateData();
			lastTimeMeasured = time;
		}
		
		return this;
	}

	public void setCachePeriod(double rate)
	{
		cachePeriod = rate;
	}

	public double getCachePeriod()
	{
		return cachePeriod;
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