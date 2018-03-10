package org.jmhsrobotics.modules.autonomous;

import java.awt.geom.AffineTransform;

import org.jmhsrobotics.core.modulesystem.Sublinker;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;

import edu.wpi.first.wpilibj.command.CommandGroup;

class AutoStrategy
{
	private Sublinker linker;
	private CommandGroup commandGroup;
	private AffineTransform transform;
	
	public AutoStrategy(CommandGroup commandGroup, AffineTransform transform, Sublinker linker)
	{
		this.linker = linker;
		this.commandGroup = commandGroup;
		this.transform = transform;
	}
	
	private void process(PathNode m)
	{
		m.acceptTransform(transform);
		linker.link(m);
	}
	
	public AutoStrategy createFork()
	{
		CommandGroup fork = new CommandGroup();
		commandGroup.addSequential(fork);
		return new AutoStrategy(fork, transform, linker);
	}
	
	public void addSequential(PathNode m)
	{
		process(m);
		commandGroup.addSequential(m);
	}
	
	public void addSequential(Point point, double range, boolean reverse)
	{
		addSequential(new DriveTo(point, range, reverse));
	}
	
	public void addSequential(Point point, double range)
	{
		addSequential(point, range, false);
	}
	
	public void addSequential(Angle angle, Angle range)
	{
		addSequential(new TurnTo(angle, range));
	}
	
	public void addParallel(PathNode m)
	{
		process(m);
		commandGroup.addParallel(m);
	}
	
	public void addSequential(PathNode m, double timeout)
	{
		process(m);
		commandGroup.addSequential(m, timeout);
	}
	
	public void addParallel(PathNode m, double timeout)
	{
		process(m);
		commandGroup.addParallel(m, timeout);
	}
}