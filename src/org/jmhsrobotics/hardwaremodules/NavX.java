package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.core.modulesystem.SensorModule;
import org.jmhsrobotics.core.modulesystem.annotations.HardwareModule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.hardwareinterface.Barometer;
import org.jmhsrobotics.hardwareinterface.Gyro;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SerialPort.Port;

@HardwareModule
public class NavX extends SensorModule implements Barometer, Gyro
{
	private final static int ANGLE = 0, PRESSURE = 1, ALTITUDE = 2;
	
	private AHRS navx;

	public NavX()
	{
		super(3);
		setRefreshRate(40);
		
		navx = new AHRS(Port.kUSB);
		navx.reset();
	}
	
	@Override
	protected void readSensors(double[] dataArray)
	{
		dataArray[ANGLE] = Angle.fromDegrees(navx.getAngle()).measureDegreesUnsigned();
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