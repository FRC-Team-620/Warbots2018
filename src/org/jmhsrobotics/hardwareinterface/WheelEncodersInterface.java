package org.jmhsrobotics.hardwareinterface;

import java.util.function.DoubleBinaryOperator;
import java.util.function.ToDoubleBiFunction;

import org.jmhsrobotics.hardwareinterface.WheelEncodersInterface.EncoderData;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Sendable;

public interface WheelEncodersInterface extends Sendable
{
	public EncoderData left();
	
	public EncoderData right();
	
	public EncoderData average();
	
	public EncoderData diff();
	
	public class EncoderData
	{
		private double dist, rate, raw;
		
		public EncoderData(double dist, double rate, double raw)
		{
			this.dist = dist;
			this.rate = rate;
			this.raw = raw;
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
	
	public static EncoderData read(Encoder e)
	{
		return new EncoderData(e.getDistance(), e.getRate(), e.getRaw());
	}
	
	public static EncoderData combine(EncoderData e1, EncoderData e2, DoubleBinaryOperator combiner)
	{
		return new EncoderData(
				combiner.applyAsDouble(e1.getDist(), e2.getDist()),
				combiner.applyAsDouble(e1.getRate(), e2.getRate()),
				combiner.applyAsDouble(e1.getRaw(), e2.getRate()));
	}
}