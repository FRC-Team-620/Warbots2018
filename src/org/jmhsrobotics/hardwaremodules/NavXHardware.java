package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.core.modulesystem.SensorModule;
import org.jmhsrobotics.core.modulesystem.annotations.HardwareModule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.hardwareinterface.Barometer;
import org.jmhsrobotics.hardwareinterface.Gyro;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

@HardwareModule
public class NavXHardware extends SensorModule implements Gyro, Barometer
{
	private AHRS navx;

	private Angle angle;
	private Angle rotationRate;
	
	private double pressure;
	
	/**
	 * @param port The Port that the navx is connected to. This should be either an instance of
	 * SerialPort.Port, I2C.Port, or SPI.Port.
	 */
	public NavXHardware(Enum<?> port)
	{
		setCachePeriod(0.004);
		
		try
		{
			navx = AHRS.class.getConstructor(port.getClass()).newInstance(port);
			navx.reset();
			
			SmartDashboard.putData("Navx ", this);
		}
		catch (ReflectiveOperationException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void reset()
	{
		navx.reset();
	}

	@Override
	public void updateData()
	{
		angle = Angle.fromDegrees(navx.getAngle());
		rotationRate = Angle.fromDegrees(navx.getRate());
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
	public Angle getRotationPerSecond()
	{
		updateIfNeeded();
		return rotationRate;
	}
	
	@Override
	public void initSendable(SendableBuilder builder)
	{
		builder.addDoubleProperty("Angle", getAngle()::measureDegrees, null);
		builder.addDoubleProperty("Rotation Rate", getRotationPerSecond()::measureDegrees, null);
		builder.addDoubleProperty("Pressure", this::getBarometricPressure, null);
	}
}