package org.jmhsrobotics.hardwareinterface;

public interface GrabberPneumatics
{
	public void setLeftWristContracted(boolean val);
	public void setRightWristContracted(boolean val);
	public void setLeftArmContracted(boolean val);
	public void setRightArmContracted(boolean val);
	public void setRaised(boolean val);
}
