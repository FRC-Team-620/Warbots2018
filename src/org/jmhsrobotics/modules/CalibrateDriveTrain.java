package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.hardwareinterface.Drive;
import org.jmhsrobotics.hardwareinterface.WheelEncodersInterface;

public class CalibrateDriveTrain extends CommandModule
{
	private @Submodule Drive driveTrain;
	private @Submodule WheelEncodersInterface encoders;
	
	@Override
	protected void initialize()
	{
	}
	
	static double rightOut = 0.5;
	static double leftOut = 0.5;
	static double turn = 0;
	@Override
	protected void execute() //Driving Straight
	{
		double leftSpeed = encoders.left().getRate(); //get speed of left pid
		double rightSpeed = encoders.right().getRate(); //Get speed of right pid
		double diffSpeed = encoders.diff().getRate(); //Get the difference in speed for the pids
		if(200 < diffSpeed) {
			if(rightSpeed < leftSpeed) {
				
			}else if(leftSpeed < rightSpeed) {
				
			}else {
				rightOut = 0; //If the code somehow breaks, stop the bot
				leftOut = 0;
			}
		}
		driveTrain.drive(0.5, turn);
	}
	
	@Override
	protected boolean isFinished()
	{
		return false;
	}
}
