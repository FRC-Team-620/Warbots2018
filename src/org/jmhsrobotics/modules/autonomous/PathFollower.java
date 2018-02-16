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
	private List<Point> points;
	private int currentPoint;
	private boolean onLastPoint;

	public PathFollower(List<Point> points, double range, Angle finalAngle, double finalRange)
	{
		this.points = points;
		this.range = range;
		this.finalAngle = finalAngle;
		this.finalRange = finalRange;
	}

	public PathFollower(double range, Angle finalAngle, double finalRange, Point... points)
	{
		this(Arrays.stream(points).collect(Collectors.toCollection(LinkedList::new)), range, finalAngle, finalRange);
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
		drive.setTarget(points.get(currentPoint));
		
		if (currentPoint < points.size() - 1 && drive.getDistanceToTarget() < range)
		{
			++currentPoint;
			
			if(currentPoint == points.size() - 1)
				onLastPoint = true;
		}
	}

	@Override
	protected boolean isFinished()
	{
		return onLastPoint && drive.getDistanceToTarget() < finalRange;
	}
	
	@Override
	protected void end()
	{
		drive.removeTarget();
	}
}
