package org.jmhsrobotics.modules.teleop;

import org.jmhsrobotics.core.modulesystem.ControlScheme;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.DriveController;
import org.jmhsrobotics.hardwareinterface.ElevatorController;
import org.jmhsrobotics.modules.drivecontrol.DoubleDerivativeOutputSmoother;
import org.jmhsrobotics.modules.drivecontrol.OutputSmoother;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class MoveAndClimbWithXbox extends ControlScheme
{
	private final static OutputSmoother FULL_SPEED = new DoubleDerivativeOutputSmoother(1, .05, .05);
	private final static OutputSmoother FULL_TURN = new DoubleDerivativeOutputSmoother(1, .08, Double.POSITIVE_INFINITY);
	
	private final static OutputSmoother SLOW_SPEED = new DoubleDerivativeOutputSmoother(.5, .05, .05);
	private final static OutputSmoother SLOW_TURN = new DoubleDerivativeOutputSmoother(.5, .08, Double.POSITIVE_INFINITY);
	
	private final static OutputSmoother ELEVATOR_SPEED = new DoubleDerivativeOutputSmoother(.7, .02, .02);
	private final static OutputSmoother ELEVATOR_TURN = new DoubleDerivativeOutputSmoother(.7, .06, Double.POSITIVE_INFINITY);
	
	private final static OutputSmoother SLOW_ELEVATOR_SPEED = new DoubleDerivativeOutputSmoother(.3, .03, .05);
	private final static OutputSmoother SLOW_ELEVATOR_TURN = new DoubleDerivativeOutputSmoother(.3, .08, Double.POSITIVE_INFINITY);
	
	private @Submodule DriveController drive;
	private @Submodule ElevatorController elevator;
	
	private XboxController xbox;
	private Hand side;
	
	public MoveAndClimbWithXbox(XboxController xbox, Hand side)
	{
		this.xbox = xbox;
		this.side = side;
	}
	
	@Override
	protected void execute()
	{
		double x = deadZone(xbox.getX(side), .2, .1);
		double y = -deadZone(xbox.getY(side), .2, .1);
		
		if(elevator.isPneumaticsExtended())
		{
			if(xbox.getBumper(Hand.kLeft))
			{
				drive.setSpeedOutputSmoother(SLOW_ELEVATOR_SPEED);
				drive.setTurnOutputSmoother(SLOW_ELEVATOR_TURN);
			}
			else
			{
				drive.setSpeedOutputSmoother(ELEVATOR_SPEED);
				drive.setTurnOutputSmoother(ELEVATOR_TURN);
			}
		}
		else if(xbox.getBumper(Hand.kLeft))
		{
			drive.setSpeedOutputSmoother(SLOW_SPEED);
			drive.setTurnOutputSmoother(SLOW_TURN);
		}
		else
		{
			drive.setSpeedOutputSmoother(FULL_SPEED);
			drive.setTurnOutputSmoother(FULL_TURN);
		}

		drive.drive(y, x);
		
		if(xbox.getStartButtonPressed())
			elevator.climbFullPower();
		
		if(xbox.getBackButtonPressed())
		{
			drive.getSpeedOutputSmoother().reset();
			drive.getTurnOutputSmoother().reset();
		}
	}
}
