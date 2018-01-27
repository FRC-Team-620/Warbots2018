package org.jmhsrobotics.core.modulesystem;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class ModuleManager
{
	private List<Module> modules;

	public ModuleManager()
	{
		modules = new ArrayList<>();
	}

	public static void linkModules(Stream<Module> modules)
	{
		ModuleManager m = new ModuleManager();
		m.addModules(modules);
	}

	public static void linkModules(Collection<Module> modules)
	{
		ModuleManager m = new ModuleManager();
		m.addModules(modules);
	}

	public static void linkModules(Module modules)
	{
		ModuleManager m = new ModuleManager();
		m.addModules(modules);
	}

	public void addModule(Module module)
	{
		for (Class<?> c = module.getClass(); c != null; c = c.getSuperclass())
			Arrays.stream(c.getDeclaredFields()).filter(f -> f.isAnnotationPresent(Submodule.class)).parallel().forEach(field ->
			{
				try
				{
					field.setAccessible(true);

					Class<?> type = field.getType();
					if (type.isArray())
					{
						Class<?> subtype = type.getComponentType();
						field.set(module, getModules(subtype).toArray(size -> (Object[]) Array.newInstance(subtype, size)));
					}
					else if (Optional.class.isAssignableFrom(type))
					{
						ParameterizedType fullType = (ParameterizedType) field.getGenericType();
						Class<?> subtype = (Class<?>) fullType.getActualTypeArguments()[0];
						field.set(module, getModule(subtype));
					}
					else field.set(module, getModule(type).orElseThrow(() -> new MissingSubmoduleException(type, this, module)));
				}
				catch (ReflectiveOperationException e)
				{
					e.printStackTrace();
				}
			});

		module.onLink();
		modules.add(0, module);
	}

	public void addModules(Stream<Module> modules)
	{
		modules.forEach(this::addModule);
	}

	public void addModules(Module... modules)
	{
		addModules(Arrays.stream(modules));
	}

	public void addModules(Collection<Module> modules)
	{
		addModules(modules.stream());
	}

	public void addModules(ModuleManager otherModuleManager)
	{
		addModules(otherModuleManager.modules);
	}

	@SuppressWarnings("unchecked")
	public <T> Stream<T> getModules(Class<T> type)
	{
		return (Stream<T>) modules.stream().filter(o -> type.isAssignableFrom(o.getClass()));
	}

	public <T> Optional<T> getModule(Class<T> type)
	{
		return getModules(type).findFirst();
	}

	public AutonomousCommand getModuleTests(Predicate<? super Module> filter, String name)
	{
		Stream<Module> toTest = modules.stream().filter(filter);
		AutonomousCommand group = new AutonomousCommand()
		{
			@Override
			public String getID()
			{
				return "test-" + name;
			}
		};
		toTest.forEach(module ->
		{
			group.addSequential(new InstantCommand()
			{
				@Override
				protected void initialize()
				{
					System.out.println("\n\n*** Starting test for Module " + module.getClass().getName() + " ***");
				}
			});

			group.addSequential(module.getTest());
		});

		return group;
	}

	public AutonomousCommand getAllModuleTests()
	{
		return getModuleTests(m -> true, "all");
	}

	public AutonomousCommand getModuleTestsByAnnotation(Class<? extends Annotation> annotation)
	{
		return getModuleTests(m -> m.getClass().isAnnotationPresent(annotation), annotation.getName());
	}

	@Override
	public String toString()
	{
		return "Module Manager: " + modules;
	}
}