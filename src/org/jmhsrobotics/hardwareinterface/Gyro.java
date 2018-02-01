package org.jmhsrobotics.hardwareinterface;

import org.jmhsrobotics.core.util.Angle;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Sendable;

public interface Gyro extends Sendable
{
	public Angle getAngle();
	public double getRotationRate();
	public PIDSource getAnglePIDSource();
	public void setPIDSourceType(PIDSourceType type);
	public PIDSourceType getPIDSourceType();
}