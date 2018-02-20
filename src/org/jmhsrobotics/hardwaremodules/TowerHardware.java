package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.modulesystem.annotations.HardwareModule;
import org.jmhsrobotics.hardwareinterface.Pneumatics;
import org.jmhsrobotics.hardwareinterface.Tower;

import edu.wpi.first.wpilibj.Solenoid;

@HardwareModule
public class TowerHardware implements Tower
{
	private @Submodule Pneumatics pneumatics;
	
	private Solenoid drivingSolenoid;
	private Solenoid climbingSolenoid;
	
	public TowerHardware(int drivingSolenoidPort, int climbingSolenoidPort)
	{
		drivingSolenoid = new Solenoid(pneumatics.getPort(), drivingSolenoidPort);
		drivingSolenoid.set(false);
		climbingSolenoid = new Solenoid(pneumatics.getPort(), climbingSolenoidPort);
		climbingSolenoid.set(false);
	}
	
	@Override
	public void raise()
	{
		drivingSolenoid.set(true);
	}

	@Override
	public void lower()
	{
		drivingSolenoid.set(false);
	}

	@Override
	public void climb()
	{
		lower();
		climbingSolenoid.set(true);
	}

	@Override
	public boolean isExtended()
	{
		return drivingSolenoid.get();
	}
}
