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
import org.jmhsrobotics.core.modulesystem.ModuleManager;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.HybridRobot;
import org.jmhsrobotics.core.util.Point;
import org.jmhsrobotics.hardwaremodules.DragEncodersHardware;
import org.jmhsrobotics.hardwaremodules.DriveTrainHardware;
import org.jmhsrobotics.hardwaremodules.NavXHardware;
import org.jmhsrobotics.hardwaremodules.WheelEncodersHardware;
import org.jmhsrobotics.modules.CalibrateDriveTrain;
import org.jmhsrobotics.modules.DriveWithJoystick;
import org.jmhsrobotics.modules.autonomous.PathFollower;
import org.jmhsrobotics.modules.drivecontrol.CorrectiveDrive;
import org.jmhsrobotics.modules.drivecontrol.Localization;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.SPI;
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
	private Command autonomous;

	@Override
	public void robotInit()
	{
		CameraServer.getInstance().startAutomaticCapture();
		
		
		long time = System.nanoTime();

		modules = new ModuleManager();

		subsystems = new SubsystemManager();
		modules.addModule(subsystems);
		subsystems.addEmptySubsystem("DriveTrain");
//		subsystems.addEmptySubsystem("TurnTable");
//		subsystems.addEmptySubsystem("Grabber");

		modules.addModule(new OperatorInterface());
		
		modules.addModule(new DriveTrainHardware(0, 2, 1, 3));
		modules.addModule(new NavXHardware(SPI.Port.kMXP));
		modules.addModule(new WheelEncodersHardware(2, 3, true, 0, 1, false));
		modules.addModule(new DragEncodersHardware(20, 21, false, 22, 23, false));
		
		modules.addModule(new CalibrateDriveTrain());

		modules.addModule(new Localization());
		
		CorrectiveDrive driveController = new CorrectiveDrive();
		modules.addModule(driveController);
		subsystems.getSubsystem("DriveTrain").setDefaultCommand(driveController);
//		modules.addModule(new RawDriveController());
		
//		CommandModule dbControl = new DashboardControl();
//		modules.addModule(dbControl);
//		subsystems.getSubsystem("DriveTrain").setDefaultCommand(dbControl);
		
		modules.addModule(new DriveWithJoystick());

//		modules.addModule(new MockGrabberPneumatics());
//		modules.addModule(new MockGrabberWheels());
//		
//		modules.addModule(new MockTurnTable());
//		
//		GrabberControlCommand grabberController = new GrabberControlCommand();
//		modules.addModule(new GrabberControlCommand());
//		subsystems.getSubsystem("Grabber").setDefaultCommand(grabberController);
//		
//		TurnTableControlCommand turnTableController = new TurnTableControlCommand();
//		modules.addModule(turnTableController);
//		subsystems.getSubsystem("TurnTable").setDefaultCommand(turnTableController);
		
//		modules.addModule(new DriveClawMechWithTwoJoysticks());
		
//		modules.addModule(modules.getAllModuleTests());
//		autonomous = new AutoSwitcher();
//		modules.addModule(autonomous);

		PathFollower auto = new PathFollower(24, Angle.ZERO, 6,
				new Point(0, 84),
				new Point(84, 84));
		modules.addModule(auto);
		autonomous = auto;
		
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
	}
	
	private void activate()
	{
		Scheduler.getInstance().removeAll();
		modules.getModule(OperatorInterface.class).ifPresent(OperatorInterface::disableJoystickRefresh);
	}
}