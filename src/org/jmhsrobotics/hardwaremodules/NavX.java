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

@HardwareModule
public class NavX extends SensorModule implements Barometer, Gyro
{
	private final static int ANGLE = 0, PRESSURE = 1, ALTITUDE = 2;
	
	private AHRS navx;

	public NavX(Port port)
	{
		super(3);
		setRefreshRate(0);
		
		navx = new AHRS(port);
		navx.reset();
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
}