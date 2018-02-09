package org.jmhsrobotics.hardwaremodules;

import java.io.PrintStream;

import org.jmhsrobotics.core.modulesystem.SensorModule;
import org.jmhsrobotics.hardwareinterface.GenericEncoderInterface;
import org.jmhsrobotics.hardwareinterface.WheelEncoders;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class WheelEncodersHardware extends SensorModule implements WheelEncoders
{
	private Encoder leftEncoder, rightEncoder;
	private EncoderData left, right, average, diff;

	public WheelEncodersHardware()
	{
		leftEncoder = new Encoder(2, 3, true);
		rightEncoder = new Encoder(0, 1);
		leftEncoder.setDistancePerPulse(0.031425);
		rightEncoder.setDistancePerPulse(0.031425);
		SmartDashboard.putData("Encoders", this);
	}
	
	@Override
	public void updateData()
	{
		left = GenericEncoderInterface.read(leftEncoder);
		right =  GenericEncoderInterface.read(rightEncoder);
		average = GenericEncoderInterface.combine(left, right, (l, r) -> (l + r) / 2);
		diff = GenericEncoderInterface.combine(left, right, (l, r) -> l - r);
	}
	
	@Override
	public EncoderData left()
	{
		updateIfNeeded();
		return left;
	}

	@Override
	public EncoderData right()
	{
		updateIfNeeded();
		return right;
	}

	@Override
	public EncoderData average()
	{
		updateIfNeeded();
		return average;
	}

	@Override
	public EncoderData diff()
	{
		updateIfNeeded();
		return diff;
	}

	@Override
	public void initSendable(SendableBuilder builder)
	{
		builder.addDoubleProperty("Average Dist", () -> this.average().getDist(), null);
		builder.addDoubleProperty("Left Dist", () -> this.left().getDist(), null);
		builder.addDoubleProperty("Right Dist", () -> this.right().getDist(), null);
		builder.addDoubleProperty("Average Rate", () -> this.average().getRate(), null);
	}
	
	@Override
	public void printData(PrintStream out)
	{
		out.println("Average Dist" + this.average().getDist());
		out.println("Left Dist" + this.left().getDist());
		out.println("Right Dist" + this.right().getDist());
		out.println("Average Rate" + this.average().getRate());
	}
}