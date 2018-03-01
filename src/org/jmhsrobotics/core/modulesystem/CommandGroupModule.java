package org.jmhsrobotics.core.modulesystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public abstract class CommandGroupModule extends CommandGroup implements Module
{
	@Override
	public Command getTest()
	{
		return this;
	}
	
	protected class LinkedComponentHelper
	{
		private Sublinker linker;
		
		public LinkedComponentHelper(Sublinker linker)
		{
			this.linker = linker;
		}
		
		public <T extends Command & Module> void addSequential(T m)
		{
			linker.link(m);
			CommandGroupModule.this.addSequential(m);
		}
		
		public <T extends Command & Module> void addParallel(T m)
		{
			linker.link(m);
			CommandGroupModule.this.addParallel(m);
		}
		
		public <T extends Command & Module> void addSequential(T m, double timeout)
		{
			linker.link(m);
			CommandGroupModule.this.addSequential(m, timeout);
		}
		
		public <T extends Command & Module> void addParallel(T m, double timeout)
		{
			linker.link(m);
			CommandGroupModule.this.addParallel(m, timeout);
		}
	}
}