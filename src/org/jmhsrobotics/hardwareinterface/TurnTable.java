package org.jmhsrobotics.hardwareinterface;

public interface TurnTable
{
	public static enum Position
	{
		farLeft, left, center, right, farRight
	}
	
	public void goTo(Position position);
}
