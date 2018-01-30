package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.core.modulesystem.SensorModule;
import org.jmhsrobotics.hardwareinterface.WheelEncodersInterface;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class WheelEncoders extends SensorModule implements WheelEncodersInterface
{
	private final static int LEFT = 0, RIGHT = 1;

	private Encoder left;
	private Encoder right;

	public WheelEncoders()
	{
		super(2);
		left = new Encoder(2, 3);
		right = new Encoder(0, 1, true);
		setPIDSourceType(PIDSourceType.kDisplacement);
	}

	@Override
	protected void readSensors(double[] dataArray)
	{
		System.out.println("left encoder: " + (dataArray[LEFT] = left.pidGet()));
		System.out.println("right encoder: " + (dataArray[RIGHT] = right.pidGet()));
	}
	
	public PIDSource getLeftPIDSource()
	{
		return getPIDSource(LEFT);
	}
	
	public PIDSource getRightPIDSource()
	{
		return getPIDSource(RIGHT);
	}
	
	public double getLeft()
	{
		return get(LEFT);
	}
	
	public double getRight()
	{
		return get(RIGHT);
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

}