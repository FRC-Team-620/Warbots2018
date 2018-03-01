/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved. */
/* Open Source Software - may be modified and shared by FRC teams. The code */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project. */
/*----------------------------------------------------------------------------*/

package org.jmhsrobotics.robot;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.jmhsrobotics.core.modules.OperatorInterface;
import org.jmhsrobotics.core.modules.SubsystemManager;
import org.jmhsrobotics.core.modulesystem.ControlScheme;
import org.jmhsrobotics.core.modulesystem.ModuleManager;
import org.jmhsrobotics.core.modulesystem.PerpetualCommand;
import org.jmhsrobotics.core.util.HybridRobot;
import org.jmhsrobotics.hardwaremodules.NavXHardware;
import org.jmhsrobotics.hardwaremodules.TestbotDriveTrainHardware;
import org.jmhsrobotics.hardwaremodules.WheelEncodersHardware;
import org.jmhsrobotics.mockhardware.MockElevator;
import org.jmhsrobotics.mockhardware.MockGrabberPneumatics;
import org.jmhsrobotics.mockhardware.MockGrabberWheels;
import org.jmhsrobotics.mockhardware.MockTurnTable;
import org.jmhsrobotics.modules.GrabberControlCommand;
import org.jmhsrobotics.modules.MoveWithXbox;
import org.jmhsrobotics.modules.NormalizeDriveTrain;
import org.jmhsrobotics.modules.PersistantDataModule;
import org.jmhsrobotics.modules.TestLinearActuator;
import org.jmhsrobotics.modules.TurnTableControlCommand;
import org.jmhsrobotics.modules.autonomous.AutoSwitcher;
import org.jmhsrobotics.modules.autonomous.CLLAutonomous;
import org.jmhsrobotics.modules.drivecontrol.CorrectiveDrive;
import org.jmhsrobotics.modules.drivecontrol.LinearAccelRiemannInterpolator;
import org.jmhsrobotics.modules.drivecontrol.Localization;

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
	private AutoSwitcher autonomous;
//	private List<PerpetualCommand> baseLineControl;

	@Override
	public void robotInit()
	{
//		CameraServer.getInstance().startAutomaticCapture();
		long time = System.nanoTime();

		modules = new ModuleManager();
		
		subsystems = new SubsystemManager();
		modules.addModule(subsystems);
		subsystems.addEmptySubsystem("DriveTrain");
		subsystems.addEmptySubsystem("TurnTable");
		subsystems.addEmptySubsystem("Grabber");

		modules.addModule(new OperatorInterface());
		
		modules.addModule(new TestbotDriveTrainHardware(0, 2, 1, 3));
//		modules.addModule(new DriveTrainHardware(1, 2, 3, 4));
		modules.addModule(new WheelEncodersHardware(2, 3, true, 0, 1, false));
		modules.addModule(new NavXHardware(SPI.Port.kMXP));

		modules.addModule(new PersistantDataModule());
		
		modules.addModule(new NormalizeDriveTrain());
		
		modules.addModule(new Localization(new LinearAccelRiemannInterpolator(100)));
		modules.addModule(new CorrectiveDrive());
		
//		modules.addModule(new PneumaticCompressor(6));
		
//		modules.addModule(new GrabberPneumaticsHardware(7, 2, 0, 4, 3, 1));
//		modules.addModule(new GrabberWheelsHardware(1, 2));
		
//		modules.addModule(new TurnTableHardware(3, 4));
		
//		modules.addModule(new TowerHardware());
//		modules.addModule(new ElevatorControlCommand());
		
		modules.addModule(new MockGrabberPneumatics());
		modules.addModule(new MockGrabberWheels());
		modules.addModule(new MockTurnTable());

		modules.addModule(new MockElevator());
		
		modules.addModule(new GrabberControlCommand());

//		modules.addModule(new OldTurnTableControlCommand());
		modules.addModule(new TurnTableControlCommand());
		
		modules.addModule(new MoveWithXbox(0));
		modules.addModule(new TestLinearActuator());
//		modules.addModule(new DriveMechWithXbox(0));
		
		modules.addModule(new CLLAutonomous());
		modules.addModule(autonomous = new AutoSwitcher());
		
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
		modules.getModules(ControlScheme.class).forEach(Command::start);
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
		
		List<PerpetualCommand> baseLineControl = modules.getModules(PerpetualCommand.class).collect(Collectors.toList());
		Collections.reverse(baseLineControl);
		baseLineControl.stream().forEachOrdered(PerpetualCommand::reset);
		baseLineControl.stream().forEachOrdered(PerpetualCommand::start);
	}
}