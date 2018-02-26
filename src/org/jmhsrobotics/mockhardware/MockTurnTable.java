package org.jmhsrobotics.mockhardware;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.util.RobotMath;
import org.jmhsrobotics.hardwareinterface.TurnTable;

import edu.wpi.first.wpilibj.command.Command;

public class MockTurnTable implements Module, TurnTable
{
	private double tableSpeed;
	private double position;
	
	@Override
	public void drive(double speed)
	{
		position += 0.06 * RobotMath.curve(tableSpeed, 2);
		position = RobotMath.constrain(position, -1, 1);
		
		if(speed != tableSpeed)
			System.out.println("Driving Turn Table at " + speed);
		
		tableSpeed = speed;
	}

	@Override
	public double getSpeed()
	{
		return tableSpeed;
	}

	@Override
	public boolean isInCenter()
	{
		return Math.abs(position) < .1;
	}

	@Override
	public Command getTest()
	{
		return null;
	}
}