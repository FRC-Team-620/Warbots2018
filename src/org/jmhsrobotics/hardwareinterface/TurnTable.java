package org.jmhsrobotics.hardwareinterface;

import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.RobotMath;

public interface TurnTable
{
	public static enum Position
	{
		left(Angle.INVERSE_RIGHT), center(Angle.ZERO), right(Angle.RIGHT);
		
		static
		{
			RobotMath.linkNextAndPrevWithSelfReferencingCaps(values(), (p, t) -> p.next = t, (p, t) -> p.prev = t);
		}
		
		private Angle location;
		private Position prev, next;
		
		Position(Angle location)
		{
			this.location = location;
		}
		
		public Angle getPosition()
		{
			return location;
		}
		
		public Position getRightAdjacent()
		{
			return next;
		}
		
		public Position getLeftAdjacent()
		{
			return prev;
		}

	}
	
	public void goTo(Position position);
	
	public Position getCurrentTurnTablePosition();
}
