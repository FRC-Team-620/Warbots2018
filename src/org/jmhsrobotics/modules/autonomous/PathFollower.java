package org.jmhsrobotics.modules.autonomous;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.DriveController;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;

public class PathFollower extends CommandModule
{
	private @Submodule DriveController drive;

	private double range;
	private double finalRange;
	private Angle finalAngle;
	private Angle finalAngleRange;
	private List<Point> points;
	private int currentPoint;
	private boolean onLastPoint;

	public PathFollower(List<Point> points, double range, double finalRange, Angle finalAngle, Angle finalAngleRange)
	{
		this.points = points;
		this.range = range;
		this.finalRange = finalRange;
		this.finalAngle = finalAngle;
		this.finalAngleRange = finalAngleRange;
	}

	public PathFollower(double range, double finalRange, Angle finalAngle, Angle finalAngleRange, Point... points)
	{
		this(Arrays.stream(points).collect(Collectors.toCollection(LinkedList::new)), range, finalRange, finalAngle, finalAngleRange);
	}

	@Override
	protected void initialize()
	{
		System.out.println("Starting path follower");
		currentPoint = 0;
	}

	@Override
	protected void execute()
	{
		if(onLastPoint)
			drive.setTarget(finalAngle);
		else
		{
			drive.setTarget(points.get(currentPoint));
			
			if (drive.getDistanceToTargetPoint() < range)
			{
				++currentPoint;
				
				if(currentPoint == points.size() - 1)
					range = finalRange;
				else if(currentPoint == points.size())
				{
					drive.setTarget(finalAngle);
					onLastPoint = true;
				}
			}
		}
	}

	@Override
	protected boolean isFinished()
	{
		return onLastPoint && drive.getDistanceToTargetAngle().compareTo(finalAngleRange) <= 0;
	}
	
	@Override
	protected void end()
	{
		drive.removeTarget();
	}
}
