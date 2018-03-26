package org.jmhsrobotics.modules.teleop;

import org.jmhsrobotics.core.modulesystem.ControlScheme;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.ElevatorController;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class SeanElevatorControl extends ControlScheme
{
	private @Submodule ElevatorController elevator;

	private XboxController xbox;
	private Hand hand;
	
	public SeanElevatorControl(XboxController xbox, Hand hand)
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
			elevator.setPneumatics(true);
		
		if(xbox.getStartButtonPressed())
			elevator.setPneumatics(false);
		
		if(xbox.getBButtonPressed())
			elevator.goToRaw(7500, elevator.isPneumaticsExtended());
		
		if(xbox.getXButtonPressed())
			elevator.goToRaw(100, elevator.isPneumaticsExtended());
		
		if(xbox.getPOV() == 0)
			elevator.goToRaw(11000, elevator.isPneumaticsExtended());
	}
}
