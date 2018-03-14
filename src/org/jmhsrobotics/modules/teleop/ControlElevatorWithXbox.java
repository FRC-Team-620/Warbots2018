package org.jmhsrobotics.modules.teleop;

import org.jmhsrobotics.core.modulesystem.ControlScheme;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.ElevatorController;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class ControlElevatorWithXbox extends ControlScheme
{
	private @Submodule ElevatorController elevator;

	private XboxController xbox;
	private Hand hand;
	
	public ControlElevatorWithXbox(XboxController xbox, Hand hand)
	{
		this.xbox = xbox;
		this.hand = hand;
	}
	
	@Override
	protected void execute()
	{
		double y = -deadZone(xbox.getY(hand), .2, .1);
		elevator.manualDrive(y);
		
		if(xbox.getYButtonPressed())
			elevator.setPneumatics(!elevator.isPneumaticsExtended());
		
		if(xbox.getBButtonPressed())
			elevator.goToRaw(7000, false);
	}
}
