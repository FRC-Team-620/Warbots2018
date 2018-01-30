package org.jmhsrobotics.hardwaremodules;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToDoubleFunction;

import org.jmhsrobotics.core.modulesystem.SensorModule;
import org.jmhsrobotics.hardwareinterface.WheelEncodersInterface;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;

public class WheelEncoders extends SensorModule implements WheelEncodersInterface
{
	private final static int LEFT = 0, RIGHT = 1, AVERAGE = 2, DIFF = 3;
	private final static int PID = 0 << 2, DIST = 1 << 2, RATE = 2 << 2, RAW = 3 << 2;

	private final Map<Integer, ToDoubleFunction<Encoder>> operations;
	
	private Encoder left;
	private Encoder right;

	public WheelEncoders()
	{
		super(16);
		left = new Encoder(2, 3);
		right = new Encoder(0, 1, true);
		setPIDSourceType(PIDSourceType.kDisplacement);
		
		operations = new HashMap<>();
		operations.put(PID, Encoder::pidGet);
		operations.put(DIST, Encoder::getDistance);
		operations.put(RATE, Encoder::getRate);
		operations.put(RAW, Encoder::getRaw);
	}

	@Override
	protected void readSensors(double[] dataArray)
	{
		for(int i : operations.keySet())
		{
			double leftValue = operations.get(i).applyAsDouble(left);
			double rightValue = operations.get(i).applyAsDouble(right);
			
			dataArray[LEFT | i] = leftValue;
			dataArray[RIGHT | i] = rightValue;
			dataArray[AVERAGE | i] = (leftValue + rightValue) / 2;
			dataArray[DIFF | i] = leftValue - rightValue;
		}
	}
	
	@Override
	public void setPIDSourceType(PIDSourceType type)
	{
		left.setPIDSourceType(type);
		right.setPIDSourceType(type);
	}

	@Override
	public PIDSourceType getPIDSourceType()
	{
		return left.getPIDSourceType();
	}

	private EncoderData read(int mask)
	{
		return new EncoderData(getPIDSource(mask | PID), get(mask | PID), get(mask | DIST), get(mask | RATE), get(mask | RAW));
	}
	
	@Override
	public EncoderData left()
	{
		return read(LEFT);
	}

	@Override
	public EncoderData right()
	{
		return read(RIGHT);
	}

	@Override
	public EncoderData average()
	{
		return read(AVERAGE);
	}

	@Override
	public EncoderData diff()
	{
		return read(DIFF);
	}
}