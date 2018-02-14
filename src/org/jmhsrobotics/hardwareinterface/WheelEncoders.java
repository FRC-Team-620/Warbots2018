package org.jmhsrobotics.hardwareinterface;

public interface WheelEncoders extends EncoderGroup
{
	public EncoderData left();

	public EncoderData right();

	public EncoderData average();

	public EncoderData diff();
}