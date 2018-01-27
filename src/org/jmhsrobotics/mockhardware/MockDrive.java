package org.jmhsrobotics.mockhardware;

import org.jmhsrobotics.hardwareinterface.WheelSwitchingDrive;
import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.hardwareinterface.WheelConfiguration;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class MockDrive implements Module, WheelSwitchingDrive
{
	private WheelConfiguration wc;
	private double x, dx, y, dy;
	private Angle dir, ddir;

	public MockDrive()
	{
		x = y = 0;
		dir = Angle.ZERO;
	}

	@Override
	public void drive(double speed, double turn)
	{
		System.out.println("Driving at speed " + speed + " and turning " + turn);

		speed += Math.random() * .1 - .2;
		turn += Math.random() * .1 - .2;

		ddir = Angle.fromTurns(turn);
		dx = speed * Math.sin(dir.measureRadians());
		dy = speed * Math.cos(dir.measureRadians());

		dir = dir.plus(ddir);
		x += dx;
		y += dy;

		System.out.println("X:" + x + " Y:" + y + " Dir:" + dir);
	}

	@Override
	public void setWheelConfiguration(WheelConfiguration wc)
	{
		this.wc = wc;
		System.out.println("Set wheel configuration to " + wc);
	}

	@Override
	public WheelConfiguration getWheelConfiguration()
	{
		System.out.println("Retrieving wheel configuration");
		return wc;
	}

	public double getX()
	{
		return x;
	}

	public double getDX()
	{
		return dx;
	}

	public double getY()
	{
		return y;
	}

	public double getDY()
	{
		return dy;
	}

	public Angle getDir()
	{
		return dir;
	}

	public Angle getDDir()
	{
		return ddir;
	}

	@Override
	public Command getTest()
	{
		return new InstantCommand()
		{
			@Override
			protected void initialize()
			{
				System.out.println("Testing mock drive");
			}
		};
	}
}