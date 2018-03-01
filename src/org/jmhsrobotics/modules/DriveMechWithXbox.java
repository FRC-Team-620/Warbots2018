package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.modulesystem.ControlScheme;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.RobotMath;
import org.jmhsrobotics.hardwareinterface.ElevatorController;
import org.jmhsrobotics.hardwareinterface.GrabberController;
import org.jmhsrobotics.hardwareinterface.TurnTableController;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class DriveMechWithXbox extends ControlScheme
{
	private final static double[] triggerArmThresholds = {0.2, 0.8};
	
	private @Submodule GrabberController grabber;
	private @Submodule ElevatorController elevator;
	private @Submodule TurnTableController table;
	
	private int xboxNum;
	private boolean raisedElevatorPneumatics;
	
	public DriveMechWithXbox(int xboxNumber)
	{
		xboxNum = xboxNumber;
	}
	
	@Override
	protected void execute()
	{
		XboxController xbox = getOI().getXboxControllers().get(xboxNum);
		
		if(xbox.getXButtonPressed())
			table.goTo(table.getCurrentPosition().getLeftAdjacent());
		
		if(xbox.getBButtonPressed())
			table.goTo(table.getCurrentPosition().getRightAdjacent());
		
		double rightStickY = -xbox.getY();
		rightStickY = RobotMath.xKinkedMap(rightStickY, -1, 1, 0, -.2, .2, -1, 1);
		
//		grabber.spinLeftWheels(rightStickY);
//		grabber.spinRightWheels(rightStickY);
//		
//		if(xbox.getYButtonPressed())
//			elevator.goTo(elevator.getCurrentLifterPosition().getAdjacentAbove());
//		if(xbox.getAButtonPressed())
//			elevator.goTo(elevator.getCurrentLifterPosition().getAdjacentBelow());
		
		double wheelSpeed = 0, elevatorSpeed = 0;
		if(xbox.getYButton())
			wheelSpeed = rightStickY;
		else
			elevatorSpeed = rightStickY;
		
		if(xbox.getAButtonPressed())
			raisedElevatorPneumatics = !raisedElevatorPneumatics;
		
		elevator.driveManual(elevatorSpeed, raisedElevatorPneumatics);
		
		grabber.spinLeftWheels(wheelSpeed);
		grabber.spinRightWheels(wheelSpeed);
		
//		
		
		grabber.setLeftArm(getGrabberArmPosition(xbox.getTriggerAxis(Hand.kLeft)));
		grabber.setRightArm(getGrabberArmPosition(xbox.getTriggerAxis(Hand.kRight)));
		
		if(xbox.getBumperPressed(Hand.kRight))
			grabber.setRaised(!grabber.isRaised());
	}
	
	private GrabberController.Position getGrabberArmPosition(double axis)
	{
		int posNum = 0;
		while(posNum < triggerArmThresholds.length && axis > triggerArmThresholds[posNum])
			++posNum;
		
		return GrabberController.Position.values()[posNum];
	}
}