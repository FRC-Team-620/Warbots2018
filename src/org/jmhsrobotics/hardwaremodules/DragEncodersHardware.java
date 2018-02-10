package org.jmhsrobotics.hardwaremodules;

import java.io.PrintStream;

import org.jmhsrobotics.core.modulesystem.SensorModule;
import org.jmhsrobotics.core.modulesystem.annotations.HardwareModule;
import org.jmhsrobotics.hardwareinterface.DragWheelEncoders;
import org.jmhsrobotics.hardwareinterface.GenericEncoderInterface;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

@HardwareModule
public class DragEncodersHardware extends SensorModule implements DragWheelEncoders
{	
	private Encoder forwardEncoder, sideEncoder;
	private EncoderData forward, side;
	
	public DragEncodersHardware(int forwardEncoderPort1, int forwardEncoderPort2, boolean reverseForwardEncoder, int sideEncoderPort1, int sideEncoderPort2, boolean reverseSideEncoder)
	{
		forwardEncoder = new Encoder(forwardEncoderPort1, forwardEncoderPort2, reverseForwardEncoder);
		sideEncoder = new Encoder(sideEncoderPort1, sideEncoderPort2, reverseSideEncoder);
		forwardEncoder.setDistancePerPulse(0.031425);
		sideEncoder.setDistancePerPulse(0.031425);
		SmartDashboard.putData("Drag Encoders", this);
	}
	
	@Override
	public void reset()
	{
		forwardEncoder.reset();
		sideEncoder.reset();
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
		builder.addDoubleProperty("Forward", forward()::getDist, null);
		builder.addDoubleProperty("Unadjusted side dist", side()::getDist, null);
	}

	@Override
	public void printData(PrintStream out)
	{
		out.println("Forward dist " + forward().getDist());
		out.println("Unadjusted side dist " + side().getDist());
	}
}
