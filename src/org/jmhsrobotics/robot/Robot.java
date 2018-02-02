/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved. */
/* Open Source Software - may be modified and shared by FRC teams. The code */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project. */
/*----------------------------------------------------------------------------*/

package org.jmhsrobotics.robot;

import org.jmhsrobotics.core.modules.OperatorInterface;
import org.jmhsrobotics.core.modules.SubsystemManager;
import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.ModuleManager;
import org.jmhsrobotics.core.util.HybridRobot;
import org.jmhsrobotics.hardwaremodules.DriveTrain;
import org.jmhsrobotics.hardwaremodules.NavX;
import org.jmhsrobotics.hardwaremodules.WheelEncoders;
import org.jmhsrobotics.modules.AutoSwitcher;
import org.jmhsrobotics.modules.DashboardControl;
import org.jmhsrobotics.modules.DriveWithJoystick;
import org.jmhsrobotics.modules.drivecontroll.CorrectiveDrive;

import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
		subsystems.addEmptySubsystem("DriveTrain");

		modules.addModule(new OperatorInterface());
		modules.addModule(new DriveTrain());
		modules.addModule(new NavX(Port.kMXP));
		modules.addModule(new WheelEncoders());
//		modules.addModule(new MockDrive());
//		modules.addModule(new MockGyro());
		
		modules.addModule(new CorrectiveDrive());
		
		CommandModule dbControl = new DashboardControl();
		modules.addModule(dbControl);
		subsystems.getSubsystem("DriveTrain").setDefaultCommand(dbControl);
//		modules.addModule(new DriveStraight());
		
		CommandModule driveWithJoystick = new DriveWithJoystick();
		modules.addModule(driveWithJoystick);
		SmartDashboard.putData("Drive With Joystick", driveWithJoystick);
//		subsystems.getSubsystem("DriveTrain").setDefaultCommand(driveWithJoystick);
		
//		modules.addModule(modules.getAllModuleTests());
		autonomous = new AutoSwitcher();
		modules.addModule(autonomous);

		System.out.println("Built and linked all modules in " + (System.nanoTime() - time) / 1E9 + " seconds.");
	}

	@Override
	public void autonomousInit()
	{
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
		autonomous.cancel();
	}

	@Override
	public void teleopPeriodic()
	{
		Scheduler.getInstance().run();
	}

	@Override
	public void testInit()
	{}

	@Override
	public void testPeriodic()
	{}
}