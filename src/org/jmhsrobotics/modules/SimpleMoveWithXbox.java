package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.modulesystem.ControlScheme;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.DriveController;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

public class SimpleMoveWithXbox extends ControlScheme
{
	private @Submodule DriveController drive;
	
	private XboxController xbox;
	
	private OutputSmoother speedSmoother;
	private OutputSmoother turnSmoother;
	
	private boolean limitStuff;
	
	public SimpleMoveWithXbox(XboxController xbox)
	{
		this.xbox = xbox;
		speedSmoother = new OutputSmoother();
		turnSmoother = new OutputSmoother();
	}
	
	@Override
	protected void initialize()
	{
		speedSmoother.reset();
	}
	
	@Override
	protected void execute()
	{
		if (xbox.getAButtonPressed())
			limitStuff ^= true;
		
		if (limitStuff)
		{
			speedSmoother.setLimits(1, 0.05, 0.05);
			turnSmoother.setLimits(1, 0.08, Double.POSITIVE_INFINITY);
		}
		else
		{
			speedSmoother.setLimits(1, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
			turnSmoother.setLimits(1, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		}
		
		double x = deadZone(xbox.getX(Hand.kLeft), .3, .1);
		turnSmoother.setTarget(x);
		
		double y = -deadZone(xbox.getY(Hand.kLeft), .2, .1);
		speedSmoother.setTarget(y);
		
		drive.drive(speedSmoother.update(), turnSmoother.update());
	}
}
