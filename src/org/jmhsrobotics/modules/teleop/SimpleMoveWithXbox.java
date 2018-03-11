package org.jmhsrobotics.modules.teleop;

import org.jmhsrobotics.core.modulesystem.ControlScheme;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.DriveController;
import org.jmhsrobotics.modules.drivecontrol.DoubleDerivativeOutputSmoother;
import org.jmhsrobotics.modules.drivecontrol.OutputSmoother;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class SimpleMoveWithXbox extends ControlScheme
{
	private @Submodule DriveController drive;
	
	private final static OutputSmoother CONTROLLED_SPEED_SMOOTHER = new DoubleDerivativeOutputSmoother(.3, .05, .05);
	private final static OutputSmoother CONTROLLED_TURN_SMOOTHER = new DoubleDerivativeOutputSmoother(.3, .08, .1);
	
	private XboxController xbox;
	
	private boolean limitStuff;
	
	public SimpleMoveWithXbox(XboxController xbox)
	{
		this.xbox = xbox;
	}
	
	@Override
	protected void initialize()
	{
		limitStuff = true;
	}
	
	@Override
	protected void execute()
	{
		if (xbox.getAButtonPressed())
			if(limitStuff)
			{
				limitStuff = false;
				drive.setSpeedOutputSmoother(null);
				drive.setTurnOutputSmoother(null);
			}
			else
			{
				limitStuff = true;
				drive.setSpeedOutputSmoother(CONTROLLED_SPEED_SMOOTHER);
				drive.setTurnOutputSmoother(CONTROLLED_TURN_SMOOTHER);
			}

		double x = deadZone(xbox.getX(Hand.kLeft), .3, .1);
		double y = -deadZone(xbox.getY(Hand.kLeft), .2, .1);
		drive.drive(y, x);
	}
}
