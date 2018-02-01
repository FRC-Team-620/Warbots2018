package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.core.modulesystem.SensorModule;
import org.jmhsrobotics.core.modulesystem.annotations.HardwareModule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.hardwareinterface.Barometer;
import org.jmhsrobotics.hardwareinterface.Gyro;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

@HardwareModule
public class NavX extends SensorModule implements Barometer, Gyro
{
	private final static int ANGLE = 0, PRESSURE = 1, ALTITUDE = 2, RATE = 3, PID = 4;
	
	private String name, system;
	
	private AHRS navx;

	public NavX(Port port)
	{
		super(5);
		setRefreshRate(0);
		
		navx = new AHRS(port);
		navx.reset();
		
		SmartDashboard.putData("Navx", this);
	}
	
	@Override
	protected void readSensors(double[] dataArray)
	{
		dataArray[ANGLE] = Angle.fromDegrees(navx.getAngle()).measureDegreesUnsigned();
		dataArray[RATE] = navx.getRate();
		dataArray[PID] = navx.pidGet();
		dataArray[PRESSURE] = navx.getBarometricPressure();
		dataArray[ALTITUDE] = navx.getAltitude();
	}
	
	public Angle getAngle()
	{
		return Angle.fromDegrees(get(ANGLE));
	}
	
	public double getRotationRate()
	{
		return get(RATE);
	}
	
	public PIDSource getAnglePIDSource()
	{
		return getPIDSource(PID);
	}
	
	public double getBarometricPressure()
	{
		return get(PRESSURE);
	}
	
	public PIDSource getBarometricPressurePIDSource()
	{
		return getPIDSource(PRESSURE);
	}
	
	public double getAltitude()
	{
		return get(ALTITUDE);
	}
	
	public PIDSource getAltitudePIDSource()
	{
		return getPIDSource(ALTITUDE);
	}

	@Override
	public void setPIDSourceType(PIDSourceType type)
	{
		navx.setPIDSourceType(type);
	}

	@Override
	public PIDSourceType getPIDSourceType()
	{
		return navx.getPIDSourceType();
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String getSubsystem()
	{
		return system;
	}

	@Override
	public void setSubsystem(String subsystem)
	{
		system = subsystem;
	}

	@Override
	public void initSendable(SendableBuilder builder)
	{
		builder.addDoubleProperty("Angle", () -> this.getAngle().measureDegrees(), null);
		builder.addDoubleProperty("Pressure", this::getBarometricPressure, null);
	}
}