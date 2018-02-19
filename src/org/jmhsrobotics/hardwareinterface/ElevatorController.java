package org.jmhsrobotics.hardwareinterface;

import org.jmhsrobotics.core.util.RobotMath;

public interface ElevatorController
{
	public static enum Position
	{
		ground, exchangePortal, arcadeSwitch, scaleLow, scaleMedium, scaleHigh;
		
		static
		{
			RobotMath.linkNextAndPrevWithSelfReferencingCaps(values(), (p, t) -> p.next = t, (p, t) -> p.prev = t);
		}
		
		private Position prev, next;
//		private double height;
//		
//		Position(double height)
//		{
//			this.height = height;
//		}
//		
//		public double getHeight()
//		{
//			return height;
//		}
		
		public Position getAdjacentAbove()
		{
			return next;
		}
		
		public Position getAdjacentBelow()
		{
			return prev;
		}
	}
	
	public void start();
	public void cancel();
	public void goTo(Position position);
	public Position getCurrentLifterPosition();
	public void goToRaw(double linearHeight, boolean raisePneumatics);
	public void driveManual(double linearSpeed, boolean raisePneumatics);
	public void climbFullPower();
}