package org.jmhsrobotics.hardwareinterface;

public interface GrabberPneumatics
{
	public void setLeftWristExtended(boolean val);
	public void setRightWristExtended(boolean val);
	public void setLeftArmExtended(boolean val);
	public void setRightArmExtended(boolean val);
	public void setRaised(boolean val);
}
