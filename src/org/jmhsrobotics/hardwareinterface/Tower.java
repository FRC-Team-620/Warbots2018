package org.jmhsrobotics.hardwareinterface;

public interface Tower
{
	public void raise();
	public void lower();
	public void climb();
	public boolean isExtended();
}
