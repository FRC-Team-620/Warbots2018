package org.jmhsrobotics.modules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jmhsrobotics.core.modulesystem.Module;

import edu.wpi.first.wpilibj.command.Command;

public class PersistantDataModule implements Module
{
	private final static DateFormat dateFormatter = DateFormat.getDateInstance();
	
	/**
	 * Accesses a data file by name. If the file does not exist, create it.
	 * 
	 * @return A File object representing the file accessed.
	 * @throws IOException 
	 */
	public File getDataFile(String name) throws IOException
	{
		File filename = new File("/home/lvuser/" + name + ".txt");
		if(!filename.exists())
			filename.createNewFile();
		
		return filename;
	}

	/**
	 * Reads the contents of a file.
	 * 
	 * @param file
	 *            The file to read
	 * @return The contents of the file, formatted as an array of strings, where
	 *         each string in the array represents a line of the file.
	 * @throws IOException 
	 */
	public String[] read(File file) throws IOException
	{
		List<String> lines = new ArrayList<>();

		FileReader filereader = new FileReader(file);
		try (BufferedReader buffreader = new BufferedReader(filereader))
		{
			String line = buffreader.readLine();
			while (line != null)
			{
				lines.add(line);
				line = buffreader.readLine();
			}
		}

		return lines.toArray(new String[lines.size()]);
	}
	
	public String[] read(String file) throws IOException
	{
		return read(getDataFile(file));
	}

	/**
	 * Writes to a file.
	 * 
	 * @param file
	 *            The file to write to
	 * @param data
	 *            The data to write to the file. Each string in the array represents
	 *            a line of the file.
	 */
	public void write(File file, String[] data) throws IOException
	{
		FileWriter filewriter = new FileWriter(file);
		try (BufferedWriter buffwriter = new BufferedWriter(filewriter))
		{
			for (String line : data)
			{
				buffwriter.write(line);
				buffwriter.newLine();
			}
		}
	}
	
	public void write(String file, String[] data) throws IOException
	{
		write(getDataFile(file), data);
	}

	public DateFormat getDateFormat()
	{
		return dateFormatter;
	}
	
	@Override
	public Command getTest()
	{
		return null;
	}
}
