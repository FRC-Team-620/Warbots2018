package org.jmhsrobotics.core.modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.modulesystem.annotations.HardwareModule;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

@HardwareModule
public class OperatorInterface  implements Module
{
	private final static DriverStation ds = DriverStation.getInstance();

	private volatile List<XboxController> xboxControllers;
	private volatile List<Joystick> joysticks;

	private Notifier joystickUpdater;
	
	public OperatorInterface()
	{
		updateJoysticks();
		
		joystickUpdater = new Notifier(this::updateJoysticks);
	}
	
	private void updateJoysticks()
	{
		List<XboxController> localXboxControllers = new ArrayList<>();
		List<Joystick> localJoysticks = new ArrayList<>();
		
		for (int i = 0; i < DriverStation.kJoystickPorts; ++i)
		{
			if (ds.getJoystickType(i) == -1) break;

			if (ds.getJoystickIsXbox(i))
				localXboxControllers.add(new XboxController(i));
			else localJoysticks.add(new Joystick(i));
		}
		
		this.xboxControllers = Collections.unmodifiableList(localXboxControllers);
		this.joysticks = Collections.unmodifiableList(localJoysticks);
		
		
		if(!localJoysticks.isEmpty() || !localXboxControllers.isEmpty())
			if(joystickUpdater != null)
				joystickUpdater.stop();
	}

	public void enableJoystickRefresh()
	{
		joystickUpdater.startPeriodic(0.25);
	}
	
	public void disableJoystickRefresh()
	{
		joystickUpdater.stop();
	}
	
	public List<XboxController> getXboxControllers()
	{
		return xboxControllers;
	}

	public List<Joystick> getJoysticks()
	{
		return joysticks;
	}

	public Stream<GenericHID> controllers()
	{
		return Stream.concat(xboxControllers.stream(), joysticks.stream());
	}
	
	public Joystick getMainDriverJoystick()
	{
		if(joysticks.size() == 0)
			throw new NoSuchElementException("No joysticks detected on driver station");
		return joysticks.get(Math.min(1, joysticks.size() - 1));
	}

	@Override
	public Command getTest()
	{
		System.out.println("manufacturing test");
		return new InstantCommand()
		{
			@Override
			protected void initialize()
			{
				System.out.println("initializing test");
				System.out.println("Found " + joysticks.size() + " joysticks and " + xboxControllers.size() + " controllers.");
				System.out.println();
				controllers().forEach(c -> 
					System.out.println("Name:" + c.getName() + " Axises:" + c.getAxisCount() + " Buttons:" + c.getButtonCount()));
			}
			
			@Override
			protected void execute()
			{
				System.out.println("running test");
			}
			
			@Override
			protected void end()
			{
				System.out.println("ended test");
			}
		};
	}
}