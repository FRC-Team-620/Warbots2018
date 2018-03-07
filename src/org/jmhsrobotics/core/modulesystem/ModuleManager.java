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

import org.jmhsrobotics.modules.autonomous.AutonomousCommand;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class ModuleManager implements Sublinker
{
	private List<Module> moduleList;

	public ModuleManager()
	{
		moduleList = new ArrayList<>();
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
	
	@Override
	public void link(Module module)
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

		module.onLink(this);
	}

	public void addModule(Module module)
	{
		link(module);
		moduleList.add(0, module);
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
		addModules(otherModuleManager.moduleList);
	}

	@SuppressWarnings("unchecked")
	public <T> Stream<T> getModules(Class<T> type)
	{
		return (Stream<T>) moduleList.stream().filter(o -> type.isAssignableFrom(o.getClass()));
	}

	public <T> Optional<T> getModule(Class<T> type)
	{
		return getModules(type).findFirst();
	}

	@Override
	public String toString()
	{
		return "Module Manager: " + moduleList;
	}
}