package org.jmhsrobotics.hardwareinterface;

public interface GrabberController
{
	public static enum Position
	{
		contracted, extended, raised;
	}
	
	public void setLeftArm(Position position);
	
	public Position getLeftArmPosition();
	
	public void setRightArm(Position position);
	
	public Position getRightArmPosition();
	
	public void spinLeftWheels(double speed);
	
	public void spinRightWheels(double speed);
}