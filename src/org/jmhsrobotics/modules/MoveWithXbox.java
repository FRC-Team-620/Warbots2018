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
	private final static double REDUCED_SPEED_COEFFICIENT = 0.7;
	private final static double REDUCED_TURN_COEFFICIENT = 0.8;
	
	private @Submodule DriveController drive;
	
	private int xboxNum;
	
	public MoveWithXbox(int xboxNumber)
	{
		xboxNum = xboxNumber;
	}
	
	@Override
	protected void execute()
	{
		XboxController xbox = getOI().getXboxControllers().get(xboxNum);
		
		int pov = xbox.getPOV();
		if(pov != -1)
			drive.setRelativeTarget(Angle.fromTurns((double) pov / 8));
		
		double x = xbox.getX(Hand.kLeft);
		double y = -xbox.getY(Hand.kLeft);
		
		x = RobotMath.xKinkedMap(x, -1, 1, 0, -.2, .2, -1, 1);
		y = RobotMath.xKinkedMap(y, -1, 1, 0, -.2, .2, -1, 1);
		
		if(xbox.getBumper(Hand.kLeft))
		{
			x *= REDUCED_SPEED_COEFFICIENT;
			y *= REDUCED_TURN_COEFFICIENT;
		}
		
		drive.drive(y, x);
	}
}
