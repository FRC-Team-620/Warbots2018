package org.jmhsrobotics.hardwareinterface;

public interface TurnTableMotorWithLimitSwitches
{
	public void driveTurnTableMotor(double speed);
	public boolean readMiddleEncoder();
}
