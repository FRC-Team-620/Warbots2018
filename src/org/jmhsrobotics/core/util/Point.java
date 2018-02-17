package org.jmhsrobotics.core.util;

public class Point
{
	private double x, y;
	
	public Point(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public Point plus(Point otherPoint)
	{
		return new Point(getX() + otherPoint.getX(), getY() + otherPoint.getY());
	}
	
	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}
}
