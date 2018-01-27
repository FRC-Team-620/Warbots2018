package org.jmhsrobotics.core.util;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.IterativeRobotBase;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tInstances;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tResourceType;
import edu.wpi.first.wpilibj.hal.HAL;

/**
 * HybridRobot implements the IterativeRobotBase robot program framework.
 *
 * <p>The HybridRobot class is intended to be subclassed by a user creating a robot program.
 *
 * <p>periodic() functions from the base class are called each time a new packet is received from
 * the driver station (like {@link IterativeRobot}) during the operator controlled phase, and they are called 
 * on an interval by a {@link Notifier} instance during every other phase (like {@link TimedRobot}).
 * 
 * @see {@link IterativeRobot}, {@link TimedRobot}
 * 
 * @author FRC Team 620, the James Madison High School Warbots, jmhsrobotics.org
 */
public class HybridRobot extends IterativeRobotBase
{
	private double period = TimedRobot.DEFAULT_PERIOD;
	private boolean looping = false;
	
	private Notifier loop = new Notifier(() ->
	{
		if(!isOperatorControl())
			loopFunc();
	});

	public HybridRobot()
	{
		HAL.report(tResourceType.kResourceType_Framework, tInstances.kFramework_Simple);
	}
	
	@Override
	public void startCompetition()
	{
		robotInit();
		HAL.observeUserProgramStarting();
		
		looping = true;
		loop.startPeriodic(period);

		while(true)
		{
			m_ds.waitForData();
			if(isOperatorControl())
				loopFunc();
		}
	}

	public void setPeriod(double period)
	{
		this.period = period;
		if(looping)
			loop.startPeriodic(period);
	}
}