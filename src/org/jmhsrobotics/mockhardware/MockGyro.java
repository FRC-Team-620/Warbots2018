package org.jmhsrobotics.mockhardware;

import java.util.Optional;

import org.jmhsrobotics.core.modulesystem.SensorModule;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.hardwareinterface.Gyro;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class MockGyro extends SensorModule implements Gyro
{
	private @Submodule Optional<MockDrive> drive;

	private String name, system;
	
	private PIDSourceType type;

	public MockGyro()
	{
		super(1);
		type = PIDSourceType.kDisplacement;
	}

	@Override
	protected void readSensors(double[] dataArray)
	{
		if (type == PIDSourceType.kRate)
		{
			if (drive.isPresent())
				dataArray[0] = drive.get().getDDir().plus(Angle.fromDegrees(Math.random() * .1 - .2)).measureDegrees();
			else dataArray[0] = Math.random() * 10 - 5;
		}
		else
		{
			if (drive.isPresent())
				dataArray[0] = drive.get().getDir().plus(Angle.fromDegrees(Math.random() * .2 - .4)).measureDegreesUnsigned();
			else dataArray[0] = Math.random() * 360;
		}

		System.out.println("Retrieved gyro data of type " + type + " as " + dataArray[0]);
	}

	@Override
	public void setPIDSourceType(PIDSourceType type)
	{
		this.type = type;
	}

	@Override
	public PIDSourceType getPIDSourceType()
	{
		return type;
	}

	@Override
	public Angle getAngle()
	{
		return Angle.fromDegrees(get(0));
	}

	@Override
	public PIDSource getAnglePIDSource()
	{
		return getPIDSource(0);
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
		builder.addDoubleProperty("Angle", () -> this.getAngle().measureDegreesUnsigned(), null);
	}
}
