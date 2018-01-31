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
	private final static int ANGLE = 0, PRESSURE = 1, ALTITUDE = 2;
	
	private String name, system;
	
	private AHRS navx;

	public NavX(Port port)
	{
		super(3);
		setRefreshRate(0);
		
		navx = new AHRS(port);
		navx.reset();
		
		SmartDashboard.putData("Navx", this);
	}
	
	@Override
	protected void readSensors(double[] dataArray)
	{
		double ang = navx.getAngle();
		System.out.println("Reading NavX at " + ang);
		dataArray[ANGLE] = Angle.fromDegrees(ang).measureDegreesUnsigned();
		dataArray[PRESSURE] = navx.getBarometricPressure();
		dataArray[ALTITUDE] = navx.getAltitude();
	}
	
	public Angle getAngle()
	{
		return Angle.fromDegrees(get(ANGLE));
	}
	
	public PIDSource getAnglePIDSource()
	{
		return getPIDSource(ANGLE);
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