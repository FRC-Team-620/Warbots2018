package org.jmhsrobotics.hardwareinterface;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public interface WheelEncodersInterface
{
	public EncoderData left();
	
	public EncoderData right();
	
	public EncoderData average();
	
	public EncoderData diff();
	
	public void setPIDSourceType(PIDSourceType type);

	public PIDSourceType getPIDSourceType();
	
	public class EncoderData
	{
		private double pid, dist, rate, raw;
		private PIDSource pidSource;
		
		public EncoderData(PIDSource pidSource, double pid, double dist, double rate, double raw)
		{
			this.pid = pid;
			this.dist = dist;
			this.rate = rate;
			this.raw = raw;
			this.pidSource = pidSource;
		}
		
		public PIDSource getPIDSource()
		{
			return pidSource;
		}
		
		public double getPidValue()
		{
			return pid;
		}
		
		public double getDist()
		{
			return dist;
		}
		
		public double getRate()
		{
			return rate;
		}
		
		public double getRaw()
		{
			return raw;
		}
	}
}