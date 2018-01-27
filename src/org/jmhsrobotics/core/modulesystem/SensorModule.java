package org.jmhsrobotics.core.modulesystem;

import java.util.Arrays;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;

public abstract class SensorModule implements Module
{
	private double[] data;
	private PIDSource[] dataSources;
	private long lastRead;
	private int refreshRate;

	public SensorModule(int numberOfDataPoints)
	{
		lastRead = -1;
		refreshRate = 20;
		data = new double[numberOfDataPoints];
		dataSources = new PIDSource[numberOfDataPoints];
		for (int i = 0; i < numberOfDataPoints; ++i)
			dataSources[i] = new DataPIDSource(i);
	}
	
	private class DataPIDSource implements PIDSource
	{
		private int dataIndex;
		
		public DataPIDSource(int dataPoint)
		{
			dataIndex = dataPoint;
		}

		@Override
		public void setPIDSourceType(PIDSourceType pidSource)
		{
			SensorModule.this.setPIDSourceType(pidSource);
		}

		@Override
		public PIDSourceType getPIDSourceType()
		{
			return SensorModule.this.getPIDSourceType();
		}

		@Override
		public double pidGet()
		{
			return SensorModule.this.get(dataIndex);
		}
	}

	protected abstract void readSensors(double[] dataArray);

	public abstract void setPIDSourceType(PIDSourceType type);

	public abstract PIDSourceType getPIDSourceType();

	protected final int getNumOfDataPoints()
	{
		return data.length;
	}

	protected double get(int dataPoint)
	{
		long t = System.currentTimeMillis();
		if (t - lastRead >= refreshRate)
		{
			readSensors(data);
			lastRead = t;
		}

		return data[dataPoint];
	}

	protected final PIDSource getPIDSource(int dataPoint)
	{
		return dataSources[dataPoint];
	}

	protected final void invalidateCache()
	{
		lastRead = -1;
	}

	public SensorModule setRefreshRate(int millisBetweenReads)
	{
		refreshRate = millisBetweenReads;
		return this;
	}

	public int getRefreshRate()
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
				get(0);
				System.out.println("Measured Data: " + Arrays.toString(data));
			}
			
			@Override
			protected boolean isFinished()
			{
				return timeSinceInitialized() > 10;
			}
		};
	}
}