package org.jmhsrobotics.core.util;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class PIDCalculator implements Sendable
{
	String name, system;
	
	private SensorSource source;
	private double sourceMin = Double.NEGATIVE_INFINITY, sourceMax = Double.POSITIVE_INFINITY;
	private boolean continuous;

	private PIDOutput output;
	private double outputMin = Double.NEGATIVE_INFINITY, outputMax = Double.POSITIVE_INFINITY;

	private double target;
	
	private double lastValueRead;
	private double lastP;
	private double lastD;
	private double lastI;
	private double lastF;

	private double p, i, d, f;
	private double accumulator;
	
	private Notifier autoUpdater;
	private double period = .02;
	private boolean enabled;
	
	public PIDCalculator(double p, double i, double d, double f, SensorSource source, PIDOutput output)
	{
		this.p = p;
		this.i = i;
		this.d = d;
		this.f = f;
		this.source = source;
		this.output = output;
		
		autoUpdater = new Notifier(this::update);
	}
	
	public PIDCalculator(double p, double i, double d, SensorSource source, PIDOutput output)
	{
		this(p, i, d, 0, source, output);
	}
	
	public PIDCalculator(SensorSource source, PIDOutput output)
	{
		this(0, 0, 0, source, output);
	}
	
	public void update()
	{
		lastValueRead = source.getDisplacement();
		double error = findError(lastValueRead);
		error *= p;
		double rate = -d * p * source.getRate();
		accumulator += i * error;
		double feed_forward = f * target;
		
		lastP = error;
		lastI = accumulator;
		lastD = rate;
		lastF = feed_forward;
		
		output.pidWrite(clamp(error + rate + accumulator + feed_forward, outputMin, outputMax));
	}
	
	private double findError(double n)
	{
		if (sourceMin >= sourceMax) 
			throw new IllegalArgumentException("Min greater than max");

		if(continuous)
		{
			n = (n - sourceMin) % (sourceMax - sourceMin) + sourceMin;
			if(n < sourceMin)
				n += (sourceMax - sourceMin);
		}
		else
			n = clamp(n, sourceMin, sourceMax);
		
		double error = target - n;
		if (error == 0) 
			return 0;

		double range = sourceMax - sourceMin;

		if (continuous)
		{
			if (range == Double.POSITIVE_INFINITY) 
				throw new IllegalArgumentException("Unlimited range for continuous source");

			if (error > range / 2)
				error -= range;
			else if (error < -range / 2) 
				error += range;
		}

		return error;
	}

	private double clamp(double n, double min, double max)
	{
		if (n < min)
			return min;
		else if (n > max) return max;

		return n;
	}
	
	public void setUpdatePeriod(double period)
	{
		this.period = period;
		if(enabled)
		{
			autoUpdater.stop();
			autoUpdater.startPeriodic(period);
		}
	}
	
	public double getUpdatePeriod()
	{
		return period;
	}
	
	public void enable()
	{
		if(enabled)
			return;
		
		enabled = true;
		autoUpdater.startPeriodic(period);
	}
	
	public void disable()
	{
		if(!enabled)
			return;
		
		enabled = false;
		autoUpdater.stop();
	}
	
	public void setEnabled(boolean enabled)
	{
		if(enabled)
			enable();
		else
			disable();
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public void setP(double p)
	{
		this.p = p;
	}
	
	public double getP()
	{
		return p;
	}
	
	public void setI(double i)
	{
		this.i = i;
	}
	
	public double getI()
	{
		return i;
	}
	
	public void setD(double d)
	{
		this.d = d;
	}
	
	public double getD()
	{
		return d;
	}
	
	public void setF(double f)
	{
		this.f = f;
	}
	
	public double getF()
	{
		return f;
	}
	
	public void setContinuous(boolean continuous)
	{
		this.continuous = continuous;
	}
	
	public void setContinuous()
	{
		setContinuous(true);
	}
	
	public boolean isContinuous()
	{
		return continuous;
	}
	
	public void setOutputRange(double min, double max)
	{
		outputMin = min;
		outputMax = max;
	}
	
	public double getOutputMin()
	{
		return outputMin;
	}
	
	public double getOutputMax()
	{
		return outputMax;
	}
	
	public void setInputRange(double min, double max)
	{
		sourceMin = min;
		sourceMax = max;
	}
	
	public double getInputMin()
	{
		return sourceMin;
	}
	
	public double getInputMax()
	{
		return sourceMax;
	}
	
	public void setSetpoint(double setpoint)
	{
		target = setpoint;
		if(continuous)
		{
			target = (target - sourceMin) % (sourceMax - sourceMin) + sourceMin;
			if(target < sourceMin)
				target += (sourceMax - sourceMin);
		}
		else
			target = clamp(target, sourceMin, sourceMax);
	}
	
	public void setRelativeSetpoint(double relativeSetpoint)
	{
		setSetpoint(lastValueRead + relativeSetpoint);
	}
	
	public double getSetpoint()
	{
		return target;
	}
	
	public void resetAccumulator()
	{
		accumulator = 0;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String getSubsystem()
	{
		return system;
	}

	@Override
	public void setSubsystem(String subsystem)
	{
		system = subsystem;
	}
	
	@Override
	public void initSendable(SendableBuilder builder)
	{
		builder.setSmartDashboardType("PIDController");
		
		builder.addDoubleProperty("p", this::getP, this::setP);
		builder.addDoubleProperty("i", this::getI, this::setI);
		builder.addDoubleProperty("d", this::getD, this::setD);
		builder.addDoubleProperty("f", this::getF, this::setF);
		builder.addDoubleProperty("setpoint", this::getSetpoint, this::setSetpoint);
		builder.addBooleanProperty("enabled", this::isEnabled, this::setEnabled);
		
		builder.addDoubleProperty("last value read", () -> lastValueRead, null);
		builder.addDoubleProperty("P Component", () -> lastP, null);
		builder.addDoubleProperty("D Component", () -> lastD, null);
		builder.addDoubleProperty("I Component", () -> lastI, null);
		builder.addBooleanProperty("accumulated error", () -> accumulator != 0, b -> resetAccumulator());
		builder.addDoubleProperty("F Component", () -> lastF, null);
	}
}