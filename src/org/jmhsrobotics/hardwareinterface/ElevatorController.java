package org.jmhsrobotics.hardwareinterface;

import org.jmhsrobotics.core.util.RobotMath;

public interface ElevatorController
{
	public static enum Position
	{
		ground(0), exchangePortal(4.5), arcadeSwitch(23), scaleLow(52), scaleMedium(64), scaleHigh(76);
		
		static
		{
			RobotMath.linkNextAndPrevWithSelfReferencingCaps(values(), (p, t) -> p.next = t, (p, t) -> p.prev = t);
		}
		
		private Position prev, next;
		private double height;
		
		Position(double height)
		{
			this.height = height;
		}
		
		public double getHeight()
		{
			return height;
		}
		
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
	public void goToRaw(int linearHeight, boolean raisePneumatics);
	public void manualDrive(double speed);
	public void setPneumatics(boolean extended);
	public boolean isPneumaticsExtended();
	public void climbFullPower();
}