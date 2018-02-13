package org.jmhsrobotics.hardwareinterface;

import org.jmhsrobotics.core.util.RobotMath;

public interface HybridLifter
{
	public static enum Position
	{
		ground, exchangePortal, arcadeSwitch, scaleLow, scaleMedium, scaleHigh;
		
		static
		{
			RobotMath.linkNextAndPrevWithSelfReferencingCaps(values(), (p, t) -> p.next = t, (p, t) -> p.prev = t);
		}
		
		private Position prev, next;
		
		public Position getAdjacentAbove()
		{
			return next;
		}
		
		public Position getAdjacentBelow()
		{
			return prev;
		}
	}
	
	public void goTo(Position position);
	public Position getCurrentLifterPosition();
	public void goToRaw(double linearHeight, boolean raisePneumatics);
	public void driveManual(double linearSpeed, boolean raisePneumatics);
	public void climbFullPower();
}