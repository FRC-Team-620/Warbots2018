package org.jmhsrobotics.hardwareinterface;

public interface WheelEncoders extends GenericEncoderInterface
{
	public EncoderData left();

	public EncoderData right();

	public EncoderData average();

	public EncoderData diff();
}