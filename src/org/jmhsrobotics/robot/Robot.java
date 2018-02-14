/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved. */
/* Open Source Software - may be modified and shared by FRC teams. The code */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project. */
/*----------------------------------------------------------------------------*/

package org.jmhsrobotics.robot;

import org.jmhsrobotics.core.modules.OperatorInterface;
import org.jmhsrobotics.core.modules.SubsystemManager;
import org.jmhsrobotics.core.modulesystem.ControlSchemeModule;
import org.jmhsrobotics.core.modulesystem.DriveController;
import org.jmhsrobotics.core.modulesystem.ModuleManager;
import org.jmhsrobotics.core.util.HybridRobot;
import org.jmhsrobotics.mockhardware.MockElevatorAndGrabber;
import org.jmhsrobotics.mockhardware.MockGrabberPneumatics;
import org.jmhsrobotics.mockhardware.MockGrabberWheels;
import org.jmhsrobotics.mockhardware.MockTurnTable;
import org.jmhsrobotics.modules.DriveClawMechWithTwoJoysticks;
import org.jmhsrobotics.modules.GrabberController;
import org.jmhsrobotics.modules.TurnTableController;
import org.jmhsrobotics.modules.autonomous.AutoSwitcher;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends HybridRobot
{
	private ModuleManager modules;
	private SubsystemManager subsystems;
	private AutoSwitcher autonomous;

	@Override
	public void robotInit()
	{
		long time = System.nanoTime();

		modules = new ModuleManager();

		subsystems = new SubsystemManager();
		modules.addModule(subsystems);
//		subsystems.addEmptySubsystem("DriveTrain");
		subsystems.addEmptySubsystem("TurnTable");
		subsystems.addEmptySubsystem("Grabber");

		modules.addModule(new OperatorInterface());
		
//		modules.addModule(new DriveTrainHardware(0, 2, 1, 3));
//		modules.addModule(new NavXHardware(Port.kMXP));
//		modules.addModule(new WheelEncodersHardware(2, 3, true, 0, 1, false));
//		modules.addModule(new DragEncodersHardware(20, 21, false, 22, 23, false));
//		
//		modules.addModule(new CalibrateDriveTrain());
//
//		modules.addModule(new Localization());
//		modules.addModule(new CorrectiveDrive());
//		modules.addModule(new RawDriveController());
		
//		CommandModule dbControl = new DashboardControl();
//		modules.addModule(dbControl);
//		subsystems.getSubsystem("DriveTrain").setDefaultCommand(dbControl);
		
//		modules.addModule(new DriveWithJoystick());
		
		modules.addModule(new MockElevatorAndGrabber());
		
		modules.addModule(new MockGrabberPneumatics());
		modules.addModule(new MockGrabberWheels());
		
		modules.addModule(new MockTurnTable());
		
		modules.addModule(new GrabberController());
		
		TurnTableController turnTableController = new TurnTableController();
		modules.addModule(turnTableController);
		subsystems.getSubsystem("TurnTable").setDefaultCommand(turnTableController);
		
		modules.addModule(new DriveClawMechWithTwoJoysticks());
		
//		modules.addModule(modules.getAllModuleTests());
		autonomous = new AutoSwitcher();
		modules.addModule(autonomous);

		System.out.println("Built and linked all modules in " + (System.nanoTime() - time) / 1E9 + " seconds.");
	}

	@Override
	public void autonomousInit()
	{
		activate();
		autonomous.start();
	}

	@Override
	public void autonomousPeriodic()
	{
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit()
	{
		activate();
		modules.getModules(ControlSchemeModule.class).forEach(Command::start);
	}

	@Override
	public void teleopPeriodic()
	{
		Scheduler.getInstance().run();
	}

	@Override
	public void disabledInit()
	{
		Scheduler.getInstance().removeAll();
		modules.getModule(OperatorInterface.class).ifPresent(OperatorInterface::enableJoystickRefresh);
		modules.getModules(DriveController.class).forEach(DriveController::disable);
	}
	
	private void activate()
	{
		Scheduler.getInstance().removeAll();
		modules.getModule(OperatorInterface.class).ifPresent(OperatorInterface::disableJoystickRefresh);
		modules.getModules(DriveController.class).forEach(DriveController::enable);
	}
}