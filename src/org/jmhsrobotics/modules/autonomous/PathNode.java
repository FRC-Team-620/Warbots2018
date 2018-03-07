package org.jmhsrobotics.modules.autonomous;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;
import org.jmhsrobotics.hardwareinterface.TurnTableController;

public abstract class PathNode extends CommandModule
{
	private AffineTransform transform;
	
	@SuppressWarnings("hiding")
	void acceptTransform(AffineTransform transform)
	{
		this.transform = transform;
	}
	
	protected Point transform(Point p)
	{	
		if (transform == null)
			return p;
		
		Point2D source = new Point2D.Double(p.getX(), p.getY());
		Point2D output = new Point2D.Double();
		transform.transform(source, output);
		return new Point(output.getX(), output.getY());
	}
	
	protected Angle transform(Angle angle)
	{
		Point2D origin = transform.transform(new Point2D.Double(0, 0), null);
		Point2D b = transform.transform(new Point2D.Double(1, 0), null);
		Point2D p = transform.transform(new Point2D.Double(angle.cos(), angle.sin()), null);
		
		Angle base = Angle.fromRiseAndRun(b.getY() - origin.getY(), b.getX() - origin.getX());
		Angle newLine = Angle.fromRiseAndRun(p.getY() - origin.getY(), p.getX() - origin.getX());
		
		return newLine.minus(base);
	}
	
	protected TurnTableController.Position transform(TurnTableController.Position position)
	{
		if (transform.getScaleX() < 0)
			if(position == TurnTableController.Position.left)
				return TurnTableController.Position.right;
			else if(position == TurnTableController.Position.right)
				return TurnTableController.Position.left;
		
		return position;
	}
}
