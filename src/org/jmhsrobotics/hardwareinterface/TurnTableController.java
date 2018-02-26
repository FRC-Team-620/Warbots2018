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
	
	public void calibrate();
	
	public boolean isCalibrated();
	
	public void goTo(Position position);
	
	public void goToPartial(Position position, double amount);
	
	public Position getCurrentPosition();
	
	public Position getTargetPosition();
}
