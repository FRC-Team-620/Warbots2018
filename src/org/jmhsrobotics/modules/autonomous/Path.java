package org.jmhsrobotics.modules.autonomous;

import java.util.Arrays;
import java.util.List;

import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.hardwareinterface.DriveController;

public class Path extends CommandModule
{
	private DriveController drive;

	private List<PathNode> nodes;
	private boolean reverse;

	private int currentTarget;

	public Path(DriveController drive, List<PathNode> nodes, boolean reverse)
	{
		this.drive = drive;
		this.nodes = nodes;
		this.reverse = reverse;
	}

	public Path(DriveController drive, boolean reverse, PathNode... points)
	{
		this(drive, Arrays.asList(points), reverse);
	}
	
	public void addNode(PathNode node)
	{
		nodes.add(node);
	}

	@Override
	protected void initialize()
	{
		System.out.println("Starting path follower");
		currentTarget = 0;
	}

	@Override
	protected void execute()
	{
		PathNode target = nodes.get(currentTarget);
		target.setTarget(drive, reverse);
		if(target.isFinished(drive, reverse))
		{
			target.end();
			++currentTarget;
		}
	}
	
	@Override
	protected boolean isFinished()
	{
		return currentTarget >= nodes.size();
	}

	@Override
	protected void end()
	{
		drive.removeTarget();
	}
}
