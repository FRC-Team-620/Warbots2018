package org.jmhsrobotics.hardwareinterface;

import org.jmhsrobotics.core.util.RobotMath;

public interface TurnTableController
{
	public static enum Position
	{
		left, center, right;
		
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
	
	public void manualDrive(double speed);
	
	public Position getCurrentPosition();
	
	public boolean onTarget();
	
	public Position getTargetPosition();
}
