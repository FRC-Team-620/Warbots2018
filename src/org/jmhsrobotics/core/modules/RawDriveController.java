package org.jmhsrobotics.core.modules;

import org.jmhsrobotics.core.modulesystem.DriveController;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;

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
	public void setTarget(Point point)
	{
	}
	
	@Override
	public void setRelativeTarget(Point point)
	{
	}
	
	@Override
	public void setTarget(Angle angle)
	{
	}
	
	@Override
	public void setRelativeTarget(Angle angle)
	{
	}
	
	@Override
	public void removeTarget()
	{
	}
	
	@Override
	public double getDistanceToTargetPoint()
	{
		return 0;
	}
	
	@Override
	public Angle getDistanceToTargetAngle()
	{
		return Angle.ZERO;
	}
	
	@Override
	protected void execute()
	{
		driveRaw(speed, turn);
	}
}