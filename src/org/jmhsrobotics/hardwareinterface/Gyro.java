package org.jmhsrobotics.hardwareinterface;

import org.jmhsrobotics.core.util.Angle;

public interface Gyro extends Sensor
{
	public Angle getAngle();
	public Angle getRotationPerSecond();
}