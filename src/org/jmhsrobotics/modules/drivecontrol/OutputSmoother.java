package org.jmhsrobotics.modules.drivecontrol;

public interface OutputSmoother
{
	public void setTarget(double target);
	public double getTarget();
	public void reset();
	public void update();
	public double get();
	public void copyRelevantData(OutputSmoother oldOutputSmoother);
}
