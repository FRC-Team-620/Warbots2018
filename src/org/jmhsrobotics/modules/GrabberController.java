package org.jmhsrobotics.modules;

import org.jmhsrobotics.hardwareinterface.Grabber;

public class GrabberController implements Grabber
{
	private Position currentLeftPosition, currentRightPosition;
	
	@Override
	public void setLeftArm(Position position)
	{
		if(currentLeftPosition == position)
			return;
		
		currentLeftPosition = position;
	}

	@Override
	public void setRightArm(Position position)
	{
		if(currentRightPosition == position)
			return;
		
		currentRightPosition = position;
	}

	@Override
	public void spinLeftWheels(double speed)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void spinRightWheels(double speed)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Position getLeftArmPosition()
	{
		return currentLeftPosition;
	}

	@Override
	public Position getRightArmPosition()
	{
		return currentRightPosition;
	}

}
