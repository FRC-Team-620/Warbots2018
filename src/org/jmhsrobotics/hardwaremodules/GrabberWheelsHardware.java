package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.PerpetualCommand;
import org.jmhsrobotics.core.modulesystem.annotations.HardwareModule;
import org.jmhsrobotics.hardwareinterface.GrabberWheels;
import org.jmhsrobotics.modules.drivecontrol.DoubleDerivativeOutputSmoother;
import org.jmhsrobotics.modules.drivecontrol.OutputSmoother;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Command;

@HardwareModule
public class GrabberWheelsHardware extends CommandModule implements PerpetualCommand, GrabberWheels
{
	private SpeedController leftWheels, rightWheels;
	private OutputSmoother left, right;
	private DigitalInput cubeLimitSwitch;
	
	public GrabberWheelsHardware(int leftWheelsPort, int rightWheelsPort, int cubeLimitSwitchPort)
	{
		leftWheels = new Spark(leftWheelsPort);
		rightWheels = new Spark(rightWheelsPort);
		cubeLimitSwitch = new DigitalInput(cubeLimitSwitchPort);
		
		left = new DoubleDerivativeOutputSmoother(.5, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		right = left.clone();
	}
	
	@Override
	protected void execute()
	{
		left.update();
		right.update();
		
		double leftSpeed = left.get();
		double rightSpeed = right.get();
		
//		System.out.println("Left: " + leftSpeed + " Right: " + rightSpeed);
		
		leftWheels.set(leftSpeed);
		rightWheels.set(rightSpeed);
	}
	
	@Override
	public void set(double speed, double jank)
	{
		left.setTarget(GrabberWheels.getSideComponent(speed, jank));
		right.setTarget(GrabberWheels.getSideComponent(speed, -jank));
	}
	
	@Override
	public boolean hasPrism()
	{
		return !cubeLimitSwitch.get();
	}
	
	@Override
	public Command getTest()
	{
		return null;
	}

	@Override
	public void reset()
	{
		left.reset();
		right.reset();
	}

	@Override
	protected boolean isFinished()
	{
		return false;
	}
}