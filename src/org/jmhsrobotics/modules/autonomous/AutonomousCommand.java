package org.jmhsrobotics.modules.autonomous;

import java.awt.geom.AffineTransform;

import org.jmhsrobotics.core.modulesystem.CommandGroupModule;
import org.jmhsrobotics.core.modulesystem.Sublinker;
import org.jmhsrobotics.core.modulesystem.Submodule;
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
	
	@Override
	public final void onLink(Sublinker linker)
	{
		AutoStrategy path = new AutoStrategy(this, transform, linker);
		loadPath(path);
	}
	
	protected abstract void loadPath(AutoStrategy strategy);
}