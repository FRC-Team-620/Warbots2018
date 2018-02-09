package org.jmhsrobotics.hardwareinterface;

import edu.wpi.first.wpilibj.Sendable;

public interface Barometer extends Sendable
{
	public double getBarometricPressure();
}
