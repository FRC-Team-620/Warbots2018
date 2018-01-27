package org.jmhsrobotics.hardwareinterface;

import edu.wpi.first.wpilibj.PIDSource;

public interface Barometer
{
	public double getBarometricPressure();
	public PIDSource getBarometricPressurePIDSource();
}
