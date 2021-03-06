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

import org.jmhsrobotics.core.modules.SubsystemManager;
import org.jmhsrobotics.core.modulesystem.ControlScheme;
import org.jmhsrobotics.core.modulesystem.ModuleManager;
import org.jmhsrobotics.core.modulesystem.PerpetualCommand;
import org.jmhsrobotics.core.util.HybridRobot;
import org.jmhsrobotics.hardwareinterface.GrabberController;
import org.jmhsrobotics.hardwaremodules.GrabberPneumaticsHardware;
import org.jmhsrobotics.hardwaremodules.GrabberWheelsHardware;
import org.jmhsrobotics.hardwaremodules.NavXHardware;
import org.jmhsrobotics.hardwaremodules.PWMDriveTrainHardware;
import org.jmhsrobotics.hardwaremodules.PneumaticCompressor;
import org.jmhsrobotics.hardwaremodules.PressureSensor;
import org.jmhsrobotics.hardwaremodules.TowerHardware;
import org.jmhsrobotics.hardwaremodules.TravellerHardware;
import org.jmhsrobotics.hardwaremodules.WheelEncodersHardware;
import org.jmhsrobotics.modules.ElevatorControlCommand;
import org.jmhsrobotics.modules.GrabberControlCommand;
import org.jmhsrobotics.modules.NormalizeDriveTrain;
import org.jmhsrobotics.modules.PersistantDataModule;
import org.jmhsrobotics.modules.autonomous.AutoPlan;
import org.jmhsrobotics.modules.autonomous.CenterSwitchAutonomous;
import org.jmhsrobotics.modules.autonomous.CrossAutoLineAutonomous;
import org.jmhsrobotics.modules.autonomous.ScaleAutoSwitcher;
import org.jmhsrobotics.modules.autonomous.SideAltScaleAutonomous;
import org.jmhsrobotics.modules.autonomous.SideAltSwitchAutonomous;
import org.jmhsrobotics.modules.autonomous.SidePreferentialScaleAutonomous;
import org.jmhsrobotics.modules.autonomous.SidePreferentialSwitchAutonomous;
import org.jmhsrobotics.modules.drivecontrol.CorrectiveDrive;
import org.jmhsrobotics.modules.drivecontrol.LinearAccelRiemannInterpolator;
import org.jmhsrobotics.modules.drivecontrol.Localization;
import org.jmhsrobotics.modules.teleop.AutoTurnWithPOV;
import org.jmhsrobotics.modules.teleop.ControlGrabberWithXbox;
import org.jmhsrobotics.modules.teleop.JunElevatorControl;
import org.jmhsrobotics.modules.teleop.MoveAndClimbWithXbox;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.XboxController;
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
	private AutoPlan autonomous;
//	private CommandGroupModule auto;

	@Override
	public void robotInit()
	{
		CameraServer.getInstance().startAutomaticCapture();
		long time = System.nanoTime();

		modules = new ModuleManager();
		
		subsystems = new SubsystemManager();
		modules.addModule(subsystems);
		subsystems.addEmptySubsystem("DriveTrain");
		subsystems.addEmptySubsystem("TurnTable");
		subsystems.addEmptySubsystem("Grabber");

//		modules.addModule(new MockDrive());
		modules.addModule(new PWMDriveTrainHardware(4, 7, 5, 1)); //TODO Real bot
//		modules.addModule(new PWMDriveTrainHardware(1, 3, 2, 0)); //Test bot
//		modules.addModule(new DriveTrainHardware(1, 2, 3, 4));
		modules.addModule(new WheelEncodersHardware(2, 3, true, 0, 1, false)); //TODO Real bot
//		modules.addModule(new WheelEncodersHardware(0, 1, true, 2, 3, false)); //Test bot
		
		modules.addModule(new PressureSensor(0));
		
		modules.addModule(new NavXHardware(SPI.Port.kMXP));

		modules.addModule(new PersistantDataModule());
		
		modules.addModule(new NormalizeDriveTrain());
		
		modules.addModule(new Localization(new LinearAccelRiemannInterpolator(100)));
		modules.addModule(new CorrectiveDrive());
		
		modules.addModule(new PneumaticCompressor(6));
		
//		modules.addModule(new MockGrabberPneumatics());
//		modules.addModule(new MockGrabberWheels());
//		modules.addModule(new NoRaiseGrabberPneumaticsHardware(6,2,3,0,1)); //Test bot
//		modules.addModule(new SingleSparkGrabberWheelsHardware(4, 4)); //Test bot
		modules.addModule(new GrabberPneumaticsHardware(7, 0, 4, 3, 2, 1)); //TODO Real bot
		modules.addModule(new GrabberWheelsHardware(6, 2, 5)); //TODO Real bot
		
		modules.addModule(new TowerHardware(6, 1, 0)); //TODO Real bot
		modules.addModule(new TravellerHardware(5)); //TODO Real bot
//		modules.addModule(new TravellerHardware(1)); //Test bot
//		modules.addModule(new MockTower()); //Test bot

		modules.addModule(new GrabberControlCommand());
		modules.addModule(new ElevatorControlCommand());
		
		XboxController driverController = new XboxController(0);
		modules.addModule(new MoveAndClimbWithXbox(driverController, Hand.kLeft));
		modules.addModule(new AutoTurnWithPOV(driverController));

//		XboxController coDriverController = new XboxController(1);
		XboxController coDriverController = driverController;
//		modules.addModule(new ControlGrabberWithXbox(coDriverController, Hand.kLeft, Hand.kLeft));
		modules.addModule(new ControlGrabberWithXbox(coDriverController, null, Hand.kLeft));
//		modules.addModule(new SeanElevatorControl(coDriverController, Hand.kRight));
		modules.addModule(new JunElevatorControl(coDriverController, Hand.kRight));
		
		modules.addModule(new CenterSwitchAutonomous());
		modules.addModule(new SideAltSwitchAutonomous());
		modules.addModule(new SidePreferentialSwitchAutonomous());
		
		modules.addModule(new SideAltScaleAutonomous());
		modules.addModule(new SidePreferentialScaleAutonomous());
		
//		modules.addModule(new SidePreferentialScalePreferentialSwitchAutonomous());
//		modules.addModule(new SideAltScalePreferentialSwitchAutonomous());
//		modules.addModule(new SideAltScaleAltSwitchAutonomous());
//		modules.addModule(new SidePreferentialScaleAltSwitchAutonomous());
		
		modules.addModule(new CrossAutoLineAutonomous());
		
//		modules.addModule(autonomous = new StrategyTestAutoSwitcher(new TestCubePickupAutonomous()));
//		modules.addModule(autonomous = new TestAutoSwitching());
//		modules.addModule(autonomous = new SwitchAutoSwitcher());
		modules.addModule(autonomous = new ScaleAutoSwitcher());
//		modules.addModule(autonomous = new TwoPrismAutoSwitcher());
		
		System.out.println("Built and linked all modules in " + (System.nanoTime() - time) / 1E9 + " seconds.");
	}

	@Override
	public void autonomousInit()
	{
//		modules.getModule(GrabberController.class).get().setRaised(false);
		activate();
//		autonomous.start();//TODO Turn off auto 
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
	}
	
	private void activate()
	{
		Scheduler.getInstance().removeAll();
		
		List<PerpetualCommand> baseLineControl = modules.getModules(PerpetualCommand.class).collect(Collectors.toList());
		Collections.reverse(baseLineControl);
		baseLineControl.stream().forEachOrdered(PerpetualCommand::reset);
		baseLineControl.stream().forEachOrdered(PerpetualCommand::start);
	}
}