package org.jmhsrobotics.modules.autonomous;

import java.awt.geom.AffineTransform;

import org.jmhsrobotics.core.modulesystem.CommandGroupModule;
import org.jmhsrobotics.core.modulesystem.Sublinker;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;
import org.jmhsrobotics.modules.TurnTableControlCommand;

import edu.wpi.first.wpilibj.command.InstantCommand;

public abstract class AutonomousCommand extends CommandGroupModule
{
	private AffineTransform transform;
	private @Submodule TurnTableControlCommand table;
	
	public AutonomousCommand()
	{
		transform = new AffineTransform();
		
		addSequential(new InstantCommand()
		{
			@Override
			protected void initialize()
			{
				table.calibrate();
			}
		});
	}
	
	public AutonomousCommand flipField()
	{
		transform.scale(-1, 1);
		return this;
	}
	
	protected class PathLinker
	{
		private Sublinker linker;
		
		public PathLinker(Sublinker linker)
		{
			this.linker = linker;
		}
		
		private void process(PathNode m)
		{
			m.acceptTransform(transform);
			linker.link(m);
		}
		
		public void addSequential(PathNode m)
		{
			process(m);
			AutonomousCommand.this.addSequential(m);
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
			AutonomousCommand.this.addParallel(m);
		}
		
		public void addSequential(PathNode m, double timeout)
		{
			process(m);
			AutonomousCommand.this.addSequential(m, timeout);
		}
		
		public void addParallel(PathNode m, double timeout)
		{
			process(m);
			AutonomousCommand.this.addParallel(m, timeout);
		}
	}
}
