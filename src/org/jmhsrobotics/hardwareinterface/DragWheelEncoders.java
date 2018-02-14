package org.jmhsrobotics.hardwareinterface;

public interface DragWheelEncoders extends EncoderGroup
{
	public EncoderData forward();
	
	public EncoderData side();
	
	public double getSideEncoderCorrection(double radiansTurned);
}