package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.modulesystem.ControlScheme;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.ElevatorController;
import org.jmhsrobotics.hardwareinterface.GrabberController;
import org.jmhsrobotics.hardwareinterface.TurnTableController;

import edu.wpi.first.wpilibj.XboxController;

public class DriveMechWithXbox extends ControlScheme
{
	private final static double[] triggerArmThresholds = {0.25, 0.75};
	
	private @Submodule GrabberController grabber;
	private @Submodule ElevatorController elevator;
	private @Submodule TurnTableController table;
	
	private int xboxNum;
	
	public DriveMechWithXbox(int xboxNumber)
	{
		xboxNum = xboxNumber;
	}
	
	@Override
	protected void execute()
	{
		XboxController xbox = getOI().getXboxControllers().get(xboxNum);
	}
}