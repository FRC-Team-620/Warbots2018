package org.jmhsrobotics.hardwaremodules;

import java.io.PrintStream;

import org.jmhsrobotics.core.modulesystem.SensorModule;
import org.jmhsrobotics.hardwareinterface.DragWheelEncoders;
import org.jmhsrobotics.hardwareinterface.GenericEncoderInterface;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DragEncodersHardware extends SensorModule implements DragWheelEncoders
{	
	private Encoder forwardEncoder, sideEncoder;
	private EncoderData forward, side;
	
	public DragEncodersHardware()
	{
		forwardEncoder = new Encoder(20, 21);
		sideEncoder = new Encoder(22, 23);
		forwardEncoder.setDistancePerPulse(0.031425);
		sideEncoder.setDistancePerPulse(0.031425);
		SmartDashboard.putData("Drag Encoders", this);
	}
	
	@Override
	public void updateData()
	{
		forward = GenericEncoderInterface.read(forwardEncoder);
		side = GenericEncoderInterface.read(sideEncoder);
	}
	
	@Override
	public EncoderData forward()
	{
		updateIfNeeded();
		return forward;
	}

	@Override
	public EncoderData side()
	{
		updateIfNeeded();
		return side;
	}

	@Override
	public double getSideEncoderCorrection(double radiansTurned)
	{
		//This should be a constant calculated from the geometry of the encoder position
		return -1; //TODO Add turn encoder trigonometric correction
	}

	@Override
	public void initSendable(SendableBuilder builder)
	{
		builder.addDoubleProperty("Forward/Backward", forward()::getDist, null);
		builder.addDoubleProperty("Side", side()::getDist, null);
	}

	@Override
	public void printData(PrintStream out)
	{
		out.println("Forward/Backward dist " + forward().getDist());
		out.println("Unadjusted side dist " + side().getDist());
	}
}
