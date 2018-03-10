package org.jmhsrobotics.hardwareinterface;

public interface GrabberController
{
	public static enum Position
	{
		contracted, middle, extended;
	}
	
	public void setLeftArm(Position position);
	
	public Position getLeftArmPosition();
	
	public void setRightArm(Position position);
	
	public Position getRightArmPosition();
	
	public void setRaised(boolean raised);
	
	public boolean isRaised();
	
	public boolean hasPrism();
	
	public void setWheels(double speed, double jank);
	
	public void intake();
	
	public void cancelAutomaticMovement();
}