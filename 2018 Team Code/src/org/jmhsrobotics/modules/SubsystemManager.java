package org.jmhsrobotics.modules;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jmhsrobotics.modulesystem.Module;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SubsystemManager implements Module
{
	private Map<String, Subsystem> subsystems;
	
	public SubsystemManager()
	{
		subsystems = new HashMap<>();
	}

	public void addSubsystem(String name, Subsystem system)
	{
		if (subsystems.containsKey(name)) throw new RuntimeException("Duplicate Subsystem Name");
		SmartDashboard.putData(system);
		subsystems.put(name, system);
	}

	public void addEmptySubsystem(String name)
	{
		subsystems.put(name, new EmptySubsystem());
	}

	public void deleteSubsystem(String name)
	{
		subsystems.remove(name);
	}

	public Subsystem getSubsystem(String name)
	{
		return subsystems.get(name);
	}

	public Set<String> getSubsystems()
	{
		return subsystems.keySet();
	}

	@Override
	public Command getTest()
	{
		return new InstantCommand()
		{
			@Override
			protected void initialize()
			{
				System.out.println(subsystems);
			}
		};
	}
}

class EmptySubsystem extends Subsystem
{
	@Override
	protected void initDefaultCommand()
	{}
}