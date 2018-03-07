package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.modulesystem.ControlScheme;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.RobotMath;
import org.jmhsrobotics.hardwareinterface.DriveController;
import org.jmhsrobotics.hardwareinterface.ElevatorController;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class MoveWithXbox extends ControlScheme
{
	private final static double CONTROLLED_DRIVING_SPEED_COEFFICIENT = 0.6;
	private final static double CONTROLLED_TURN_PERIOD = 3;
	private final static double RAISED_TOWER_DRIVING_SPEEED_COEFFICIENT = 0.5;
	private final static double RAISED_TOWER_TURN_PERIOD = 2;
	private final static double RAISED_TOWER_CONTROLLED_DRIVING_SPEED_COEFFICIENT = 0.4;
	private final static double RAISED_TOWER_CONTROLLED_DRIVING_TURN_PERIOD = 2;
	
	private @Submodule DriveController drive;
	private @Submodule ElevatorController elevator;
	
	private XboxController xbox;
	
	public MoveWithXbox(XboxController xbox)
	{
		this.xbox = xbox;
	}
	
	@Override
	protected void execute()
	{
		int pov = xbox.getPOV();
		if(pov != -1)
			drive.setRelativeTarget(Angle.fromTurns((double) pov / 8));
		
		double x = xbox.getX(Hand.kLeft);
		double y = -xbox.getY(Hand.kLeft);
		
		x = RobotMath.xKinkedMap(x, -1, 1, 0, -.2, .2, -1, 1);
		y = RobotMath.xKinkedMap(y, -1, 1, 0, -.2, .2, -1, 1);

		double speedCoeff = 1, period = Double.NaN;
		if(elevator.isPneumaticsExtended())
		{
			if(xbox.getBumper(Hand.kLeft))
			{
				speedCoeff = RAISED_TOWER_CONTROLLED_DRIVING_SPEED_COEFFICIENT;
				period = RAISED_TOWER_CONTROLLED_DRIVING_TURN_PERIOD;
			}
			else
			{
				speedCoeff = RAISED_TOWER_DRIVING_SPEEED_COEFFICIENT;
				period = RAISED_TOWER_TURN_PERIOD;
			}
		}
		else if(xbox.getBumper(Hand.kLeft))
		{
			speedCoeff = CONTROLLED_DRIVING_SPEED_COEFFICIENT;
			period = CONTROLLED_TURN_PERIOD;
		}
		
		if(period != period)
			drive.drive(y * speedCoeff, x);
		else
			drive.drive(y * speedCoeff, Angle.fromTurns(x / (50 * period)));
		
//		if(xbox.getAButtonPressed())
//			elevator.setPneumatics(!elevator.isPneumaticsExtended());
		
		if(xbox.getStartButtonPressed())
			elevator.climbFullPower();
	}
}
