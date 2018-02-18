package org.jmhsrobotics.mockhardware;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.hardwareinterface.TurnTable;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class MockTurnTable implements Module, TurnTable
{
	private final static double LEFT_LIMIT_SWITCH = -.3, MIDDLE_LIMIT_SWITCH = 0, RIGHT_LIMIT_SWITCH = .3;
	private final static double MIDDLE_LIMIT_SWITCH_THRESHOLD_SIZE = 0.1;
	private final static double SPEED_MULTIPLIER = .3;
	private final static double POSITION_UPDATE_PERIOD = 0.02;
	
	private double position = 0;
	private double speed = 0;
	private Notifier updater;

	public MockTurnTable()
	{
		updater = new Notifier(() ->
		{
			synchronized (MockTurnTable.this)
			{
				if ((position > LEFT_LIMIT_SWITCH || speed > 0) && (position < RIGHT_LIMIT_SWITCH || speed < 0)) 
					position += SPEED_MULTIPLIER * POSITION_UPDATE_PERIOD * speed;
			}
		});
		updater.startPeriodic(POSITION_UPDATE_PERIOD);
	}

	@Override
	public synchronized void driveTurnTableMotor(double motorSpeed)
	{
		speed = motorSpeed;
	}

	@Override
	public synchronized boolean readMiddleLimitSwitch()
	{
		return Math.abs(position - MIDDLE_LIMIT_SWITCH) < MIDDLE_LIMIT_SWITCH_THRESHOLD_SIZE;
	}
	
	@Override
	public Command getTest()
	{
		return new InstantCommand()
		{
			@Override
			protected void initialize()
			{
				System.out.println("Testing mock turntable");
			}
		};
	}
}
