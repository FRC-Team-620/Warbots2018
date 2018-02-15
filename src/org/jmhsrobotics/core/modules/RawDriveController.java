package org.jmhsrobotics.core.modules;

import org.jmhsrobotics.core.modulesystem.DriveController;

public class RawDriveController extends DriveController
{
	private double speed, turn;
	
	@Override
	@SuppressWarnings("hiding")
	public void drive(double speed, double turn)
	{
		this.speed = speed;
		this.turn = turn;
	}
	
	@Override
	public void setTarget(double x, double y)
	{
	}
	
	@Override
	protected void execute()
	{
		driveRaw(speed, turn);
	}
}