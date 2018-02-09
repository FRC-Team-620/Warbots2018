package org.jmhsrobotics.hardwaremodules;

import java.io.PrintStream;

import org.jmhsrobotics.core.modulesystem.SensorModule;
import org.jmhsrobotics.hardwareinterface.WheelEncodersInterface;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class WheelEncoders extends SensorModule implements WheelEncodersInterface
{
	private Encoder leftEncoder, rightEncoder;
	private EncoderData left, right, average, diff;

	public WheelEncoders()
	{
		leftEncoder = new Encoder(2, 3, true);
		rightEncoder = new Encoder(0, 1);
		SmartDashboard.putData("Encoders", this);
	}
	
	@Override
	public void updateData()
	{
		left = WheelEncodersInterface.read(leftEncoder);
		right =  WheelEncodersInterface.read(rightEncoder);
		average = WheelEncodersInterface.combine(left, right, (l, r) -> (l + r) / 2);
		diff = WheelEncodersInterface.combine(left, right, (l, r) -> l - r);
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