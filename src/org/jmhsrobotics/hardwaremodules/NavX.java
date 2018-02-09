package org.jmhsrobotics.hardwaremodules;

import java.io.PrintStream;

import org.jmhsrobotics.core.modulesystem.SensorModule;
import org.jmhsrobotics.core.modulesystem.annotations.HardwareModule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.hardwareinterface.Barometer;
import org.jmhsrobotics.hardwareinterface.Gyro;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

@HardwareModule
public class NavX extends SensorModule implements Gyro, Barometer
{
	private AHRS navx;

	private Angle angle;
	private double rotationRate;
	
	private double pressure;
	
	public NavX(Port port)
	{
		setRefreshRate(0);
		
		navx = new AHRS(port);
		navx.reset();
		
		SmartDashboard.putData("Navx", this);
	}

	@Override
	public void updateData()
	{
		angle = Angle.fromDegrees(navx.getAngle());
		rotationRate = navx.getRate();
		pressure = navx.getBarometricPressure();
	}

	@Override
	public double getBarometricPressure()
	{
		updateIfNeeded();
		return pressure;
	}

	@Override
	public Angle getAngle()
	{
		updateIfNeeded();
		return angle;
	}

	@Override
	public double getRotationRate()
	{
		updateIfNeeded();
		return rotationRate;
	}
	
	@Override
	public void initSendable(SendableBuilder builder)
	{
		builder.addDoubleProperty("Angle", getAngle()::measureDegrees, null);
		builder.addDoubleProperty("Rotation Rate", this::getRotationRate, null);
		builder.addDoubleProperty("Pressure", this::getBarometricPressure, null);
	}

	@Override
	public void printData(PrintStream out)
	{
		out.println("Angle: " + getAngle());
		out.println("Rotation Rate: " + getRotationRate());
		out.println("Barometric Pressure: " + getBarometricPressure());
	}
}