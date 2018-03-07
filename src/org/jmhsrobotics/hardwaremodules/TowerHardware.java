package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.modulesystem.annotations.HardwareModule;
import org.jmhsrobotics.hardwareinterface.Tower;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Command;

@HardwareModule
public class TowerHardware implements Module, Tower
{
	private Solenoid drivingSolenoid;
	private Solenoid climbingSolenoid;
	
	public TowerHardware(int canId, int drivingSolenoidPort, int climbingSolenoidPort)
	{
		drivingSolenoid = new Solenoid(canId, drivingSolenoidPort);
		drivingSolenoid.set(false);
		climbingSolenoid = new Solenoid(canId, climbingSolenoidPort);
		climbingSolenoid.set(false);
	}
	
	@Override
	public void raise()
	{
		System.out.println("raising");
		climbingSolenoid.set(false);
		drivingSolenoid.set(true);
	}

	@Override
	public void lower()
	{
		System.out.println("lowering");
		drivingSolenoid.set(false);
	}

	@Override
	public void climb()
	{
		lower();
		System.out.println("climbing");
		climbingSolenoid.set(true);
	}

	@Override
	public boolean isExtended()
	{
		return drivingSolenoid.get();
	}

	@Override
	public Command getTest()
	{
		return null;
	}
}
