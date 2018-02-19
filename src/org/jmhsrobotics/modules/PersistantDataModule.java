package org.jmhsrobotics.modules;

import java.io.File;

import org.jmhsrobotics.core.modulesystem.Module;

import edu.wpi.first.wpilibj.command.Command;

public class PersistantDataModule implements Module
{
	/**
	 * Accesses a data file by name. If the file does not exist, create it.
	 * 
	 * @return A File object representing the file accessed.
	 */
	public File getDataFile(String name)
	{
		return null;
	}
	
	/**
	 * Reads the contents of a file.
	 * 
	 * @param file The file to read
	 * @return	The contents of the file, formatted as an array of strings,
	 * where each string in the array represents a line of the file.
	 */
	public String[] read(File file)
	{
		return null;
	}
	
	/**
	 * Writes to a file.
	 * 
	 * @param file The file to write to
	 * @param data The data to write to the file. Each string in the array represents
	 * a line of the file.
	 */
	public void write(File file, String[] data)
	{
		
	}
	
	@Override
	public Command getTest()
	{
		return null;
	}
}
