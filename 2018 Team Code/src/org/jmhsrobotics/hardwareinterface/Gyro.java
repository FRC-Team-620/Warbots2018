package org.jmhsrobotics.hardwareinterface;

import org.jmhsrobotics.util.Angle;

import edu.wpi.first.wpilibj.PIDSource;

public interface Gyro
{
	public Angle getAngle();
	public PIDSource getAnglePIDSource();
}