package org.jmhsrobotics.hardwareinterface;

public interface DragWheelEncoders extends GenericEncoderInterface
{
	public EncoderData forward();
	
	public EncoderData side();
	
	public double getSideEncoderCorrection(double radiansTurned);
}