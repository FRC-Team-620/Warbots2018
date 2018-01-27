package org.jmhsrobotics.modulesystem;

import edu.wpi.first.wpilibj.command.Command;

public abstract class WrapperModule<T> implements Module
{
	private final T o;

	public WrapperModule(T o)
	{
		this.o = o;
	}

	public T get()
	{
		return o;
	}

	@Override
	public Command getTest()
	{
		return new Command()
		{
			@Override
			protected void initialize()
			{
				System.out.println(get());
			}
			
			@Override
			protected boolean isFinished()
			{
				return true;
			}
		};
	}
}
