package org.jmhsrobotics.hardwareinterface;

public interface Grabber
{
	public static enum Position
	{
		contracted, middle, extended
	}
	
	public void setLeftArm(Position position);
	
	public void setRightArm(Position position);
	
	public void spinLeftWheels(double speed);
	
	public void spinRightWheels(double speed);
}