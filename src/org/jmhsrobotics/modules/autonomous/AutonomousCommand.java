package org.jmhsrobotics.modules.autonomous;

import java.awt.geom.AffineTransform;

import org.jmhsrobotics.core.modulesystem.CommandGroupModule;
import org.jmhsrobotics.core.modulesystem.Sublinker;
import org.jmhsrobotics.core.util.Point;
import org.jmhsrobotics.modules.drivecontrol.ConstraintOutputSmoother;

public abstract class AutonomousCommand extends CommandGroupModule
{
	private AffineTransform transform;
	private boolean flippedField = false;
	
	public AutonomousCommand()
	{
		transform = new AffineTransform();
	}
	
	public AutonomousCommand flipField()
	{
		flippedField = true;
		
		transform.scale(-1, 1);
		return this;
	}
	
	@Override
	public final void onLink(Sublinker linker)
	{
		AutoStrategy path = new AutoStrategy(this, transform, linker);
		System.out.println("Calling loadPath");
		path.addSequential(new ConfigureDriveSpeed(new ConstraintOutputSmoother(1), new ConstraintOutputSmoother(1)));
		loadPath(path);
		path.addSequential(new ConfigureDriveSpeed(null, null));
	}
	
	@Override
	protected void initialize()
	{
		try
		{
			Thread.sleep(10);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.initialize();
	}
	
	protected abstract void loadPath(AutoStrategy strategy);
	
	public final static Point getLeftOrigin()
	{
		return new Point(96.262, 304);
	}
	
	public final static Point getCenterOrigin()
	{
		return new Point(0, 0);
	}
	
	@Override
	public String toString()
	{
		return super.toString() + (flippedField ? " Flipped" : " Not Flipped");
	}
}