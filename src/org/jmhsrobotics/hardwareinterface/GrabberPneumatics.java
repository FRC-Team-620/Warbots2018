package org.jmhsrobotics.hardwareinterface;

public interface GrabberPneumatics
{
	public void setLateralLeftPistonExtended(boolean val);
	public void setLateralRightEncoderExtended(boolean val);
	public void setVerticalLeftPistonExtended(boolean val);
	public void setVerticalRightEncoderExtended(boolean val);
}
