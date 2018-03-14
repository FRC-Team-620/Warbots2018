package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.modulesystem.Module;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class AutoPlan implements Module
{
	public static enum StartingPosition
	{
		left, right, center
	}
	
	public static class FieldLayout
	{
		private boolean switchOnLeft;
		private boolean scaleOnLeft;
		private boolean farSwitchOnLeft;
		
		private FieldLayout(String message)
		{
			switchOnLeft = message.charAt(0) == 'L';
			scaleOnLeft = message.charAt(1) == 'L';
			farSwitchOnLeft = message.charAt(2) == 'L';
		}
		
		public boolean isSwitchOnLeft()
		{
			return switchOnLeft;
		}
		
		public boolean isScaleOnLeft()
		{
			return scaleOnLeft;
		}
		
		public boolean isFarSwitchOnLeft()
		{
			return farSwitchOnLeft;
		}
	}
	
	private SendableChooser<StartingPosition> startingPosition;
	
	public AutoPlan()
	{
		startingPosition = new SendableChooser<StartingPosition>();
		startingPosition.addDefault("Center", StartingPosition.center);
		startingPosition.addObject("Left", StartingPosition.left);
		startingPosition.addObject("Right", StartingPosition.right);
		SmartDashboard.putData("Starting Position", startingPosition);
	}
	
	public abstract void start();
	public abstract void cancel();
	
	protected StartingPosition getStartingPosition()
	{
		return startingPosition.getSelected();
	}
	
	protected FieldLayout readFieldLayout()
	{
		String gameSpecificMessage = DriverStation.getInstance().getGameSpecificMessage();
		return new FieldLayout(gameSpecificMessage);
	}
}
