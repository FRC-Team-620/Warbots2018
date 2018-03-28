package org.jmhsrobotics.modules.teleop;

import org.jmhsrobotics.core.modulesystem.ControlScheme;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.ElevatorController;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class JunElevatorControl extends ControlScheme
{
	private @Submodule ElevatorController elevator;

	private XboxController xbox;
	private Hand hand;
	
	public JunElevatorControl(XboxController xbox, Hand hand)
	{
		this.xbox = xbox;
		this.hand = hand;
	}
	
	@Override
	protected void execute()
	{
		double y = -deadZone(xbox.getY(hand), .2, .1);
		elevator.manualDrive(y);
		
		if(xbox.getBumperPressed(Hand.kRight))
			elevator.setPneumatics(true);
		
		if(xbox.getXButtonPressed())
			elevator.setPneumatics(false);
		
		switch (xbox.getPOV())
		{
			case 0:
				elevator.goToRaw(11000);
				break;
				
			case 2:
			case 6:
				elevator.goToRaw(7500);
				break;
				
			case 4:
				elevator.goToRaw(100);
				break;
		}
	}
}
