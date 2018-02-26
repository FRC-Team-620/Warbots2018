package org.jmhsrobotics.mockhardware;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.hardwareinterface.Tower;

import edu.wpi.first.wpilibj.command.Command;

public class MockTower implements Module, Tower
{
	private boolean extended = false;
	
	@Override
	public void raise()
	{
		System.out.println("raising tower");
		extended = true;
	}

	@Override
	public void lower()
	{
		System.out.println("lowering tower");
		extended = false;
	}

	@Override
	public void climb()
	{
		System.out.println("climbing");
	}

	@Override
	public boolean isExtended()
	{
		return extended;
	}

	@Override
	public Command getTest()
	{
		return null;
	}
}