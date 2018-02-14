package org.jmhsrobotics.hardwaremodules;

import java.io.PrintStream;

import org.jmhsrobotics.core.modulesystem.SensorModule;
import org.jmhsrobotics.core.modulesystem.annotations.HardwareModule;
import org.jmhsrobotics.hardwareinterface.EncoderGroup;
import org.jmhsrobotics.hardwareinterface.WheelEncoders;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

@HardwareModule
public class WheelEncodersHardware extends SensorModule implements WheelEncoders
{
	private Encoder leftEncoder, rightEncoder;
	private EncoderData left, right, average, diff;

	public WheelEncodersHardware(int leftEncoderPort1, int leftEncoderPort2, boolean reverseLeftEncoder, int rightEncoderPort1, int rightEncoderPort2, boolean reverseRightEncoder)
	{
		leftEncoder = new Encoder(leftEncoderPort1, leftEncoderPort2, reverseLeftEncoder);
		rightEncoder = new Encoder(rightEncoderPort1, rightEncoderPort2, reverseRightEncoder);
		leftEncoder.setDistancePerPulse(0.031425);
		rightEncoder.setDistancePerPulse(0.031425);
		SmartDashboard.putData("Encoders", this);
	}
	
	@Override
	public void reset()
	{
		leftEncoder.reset();
		rightEncoder.reset();
	}
	
	@Override
	public void updateData()
	{
		left = EncoderGroup.read(leftEncoder);
		right =  EncoderGroup.read(rightEncoder);
		average = EncoderGroup.combine(left, right, (l, r) -> (l + r) / 2);
		diff = EncoderGroup.combine(left, right, (l, r) -> l - r);
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