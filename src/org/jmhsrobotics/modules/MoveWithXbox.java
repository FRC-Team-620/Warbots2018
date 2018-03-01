package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.modulesystem.ControlScheme;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.RobotMath;
import org.jmhsrobotics.hardwareinterface.DriveController;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class MoveWithXbox extends ControlScheme
{
	private final static double REDUCED_SPEED_COEFFICIENT = 0.5;
	private final static double CONTROLLED_TURN_PERIOD = 4;
	
	private @Submodule DriveController drive;
	
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

		if(xbox.getBumper(Hand.kLeft))
			drive.drive(y * REDUCED_SPEED_COEFFICIENT, Angle.fromTurns(x / (50 * CONTROLLED_TURN_PERIOD)));
		else 
			drive.drive(y, x);
	}
}
