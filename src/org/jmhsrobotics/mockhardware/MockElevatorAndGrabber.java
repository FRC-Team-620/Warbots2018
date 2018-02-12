package org.jmhsrobotics.mockhardware;

import org.jmhsrobotics.hardwareinterface.Grabber;
import org.jmhsrobotics.hardwareinterface.HybridLifter;
import org.jmhsrobotics.hardwareinterface.TurnTable;

public class MockElevatorAndGrabber implements Grabber, TurnTable, HybridLifter
{	
	@Override
	public void goTo(HybridLifter.Position position)
	{
		System.out.println("Moving lifter to " + position);
	}

	@Override
	public void goToRaw(double linearHeight, boolean raisePneumatics)
	{
		System.out.println("Moving linear actuator to " + linearHeight + " and " + (raisePneumatics ? "raising" : "lowering") + " pneumatics.");
	}

	@Override
	public void driveManual(double linearSpeed, boolean raisePneumatics)
	{
		System.out.println("Driving linear actuator at " + linearSpeed + " and " + (raisePneumatics ? "raising" : "lowering") + " pneumatics.");
	}

	@Override
	public void climbFullPower()
	{
		System.out.println("Lowering penumatic lift with both pistons");
	}

	@Override
	public void goTo(TurnTable.Position position)
	{
		System.out.println("Turning turntable to " + position);
	}

	@Override
	public void setLeftArm(Grabber.Position position)
	{
		System.out.println("Moving grabber left arm to " + position);
	}

	@Override
	public void setRightArm(Grabber.Position position)
	{
		System.out.println("Moving grabber right arm to " + position);
	}

	@Override
	public void spinLeftWheels(double speed)
	{
		System.out.println("Spinning left wheels at " + speed);
	}

	@Override
	public void spinRightWheels(double speed)
	{
		System.out.println("Spinning right wheels at " + speed);
	}

}
