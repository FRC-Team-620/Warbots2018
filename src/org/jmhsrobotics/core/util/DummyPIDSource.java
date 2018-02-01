package org.jmhsrobotics.core.util;

import java.util.function.DoubleSupplier;

import org.jmhsrobotics.core.modulesystem.Module;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;

public abstract class DummyPIDSource implements Module, PIDSource
{
	private PIDSourceType sourceType;
	
	public static DummyPIDSource fromDispAndRate(DoubleSupplier displacementSource, DoubleSupplier rateSource)
	{
		return new TwoSuppliers(displacementSource, rateSource);
	}
	
	public static DummyPIDSource fromDisp(DoubleSupplier displacementSource)
	{
		return new DispSupplier(displacementSource);
	}
	
	protected abstract double getDisplacement();
	protected abstract double getRate();
	
	public DummyPIDSource()
	{
		sourceType = PIDSourceType.kDisplacement;
	}
	
	@Override
	public void setPIDSourceType(PIDSourceType pidSource)
	{
		sourceType = pidSource;
	}

	@Override
	public PIDSourceType getPIDSourceType()
	{
		return sourceType;
	}

	@Override
	public double pidGet()
	{
		return sourceType == PIDSourceType.kDisplacement ? getDisplacement() : getRate();
	}

	@Override
	public Command getTest()
	{
		return new Command()
		{
			@Override
			protected void execute()
			{
				System.out.print("Measured ");
				if(timeSinceInitialized() <= 5)
				{
					setPIDSourceType(PIDSourceType.kDisplacement);
					System.out.print("Displacement");
				}
				else
				{
					setPIDSourceType(PIDSourceType.kRate);
					System.out.print("Rate");
				}
				System.out.println(": " + pidGet());
			}
			
			@Override
			protected boolean isFinished()
			{
				return timeSinceInitialized() > 10;
			}
		};
	}
}

class TwoSuppliers extends DummyPIDSource
{
	private DoubleSupplier disp, rate;
	
	public TwoSuppliers(DoubleSupplier displacementSource, DoubleSupplier rateSource)
	{
		disp = displacementSource;
		rate = rateSource;
	}
	
	@Override
	protected double getDisplacement()
	{
		return disp.getAsDouble();
	}

	@Override
	protected double getRate()
	{
		return rate.getAsDouble();
	}
}

class DispSupplier extends DummyPIDSource
{
	private DoubleSupplier disp;
	private double lastValue;
	private long lastTime;
	
	public DispSupplier(DoubleSupplier displacementSource)
	{
		disp = displacementSource;
		read();
	}
	
	private double read()
	{
		lastValue = disp.getAsDouble();
		lastTime = System.currentTimeMillis();
		return lastValue;
	}

	@Override
	protected double getDisplacement()
	{
		return read();
	}

	@Override
	protected double getRate()
	{
		double diff = -lastValue;
		long oldTime = lastTime;
		diff += read();
		return diff / (lastTime - oldTime);
	}
}