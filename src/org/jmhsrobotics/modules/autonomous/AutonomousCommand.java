package org.jmhsrobotics.modules.autonomous;

import java.awt.geom.AffineTransform;

import org.jmhsrobotics.core.modulesystem.CommandGroupModule;
import org.jmhsrobotics.core.modulesystem.Sublinker;

public abstract class AutonomousCommand extends CommandGroupModule
{
	private AffineTransform transform;
	
	public AutonomousCommand()
	{
		transform = new AffineTransform();
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
		System.out.println("Calling loadPath");
		loadPath(path);
	}
	
	protected abstract void loadPath(AutoStrategy strategy);
}