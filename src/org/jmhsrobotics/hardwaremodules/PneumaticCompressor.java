package org.jmhsrobotics.hardwaremodules;

import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.modulesystem.PerpetualCommand;
import org.jmhsrobotics.core.modulesystem.annotations.HardwareModule;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.command.Command;

@HardwareModule
public class PneumaticCompressor implements Module, PerpetualCommand
{
	private int port;
	private Compressor compressor;
	
	public PneumaticCompressor(int port)
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

	@Override
	public void reset()
	{
	}

	@Override
	public void start()
	{
		compressor.start();
	}

	@Override
	public void cancel()
	{
		compressor.stop();
	}
}
