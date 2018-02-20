package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.modulesystem.annotations.HardwareModule;
import org.jmhsrobotics.hardwareinterface.Pneumatics;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.command.Command;

@HardwareModule
public class PneumaticsControlHardware implements Module, Pneumatics
{
	private int port;
	private Compressor compressor;
	
	public PneumaticsControlHardware(int port)
	{
		this.port = port;
		compressor = new Compressor(port);
		compressor.setClosedLoopControl(true);
	}
	
	public int getPort()
	{
		return port;
	}

	@Override
	public Command getTest()
	{
		return null;
	}
}
