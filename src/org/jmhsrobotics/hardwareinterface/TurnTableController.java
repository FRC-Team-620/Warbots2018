package org.jmhsrobotics.hardwareinterface;

import org.jmhsrobotics.core.util.RobotMath;

public interface TurnTableController
{
	public static enum Position
	{
		right, center, left;
		
		static
		{
			RobotMath.linkNextAndPrevWithSelfReferencingCaps(values(), (p, t) -> p.next = t, (p, t) -> p.prev = t);
		}
		
		private Position prev, next;
		
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
