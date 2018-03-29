package org.jmhsrobotics.modules.teleop;

import org.jmhsrobotics.core.modulesystem.ControlScheme;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.hardwareinterface.DriveController;

import edu.wpi.first.wpilibj.XboxController;

public class AutoTurnWithPOV extends ControlScheme
{
	private @Submodule DriveController drive;
	
	private XboxController xbox;
	
	public AutoTurnWithPOV(XboxController xbox)
	{
		this.xbox = xbox;
	}
	
	@Override
	protected void execute()
	{
		int pov = xbox.getPOV();
		if(pov != -1)
			drive.setRelativeTarget(Angle.fromDegrees(pov));
	}
}
