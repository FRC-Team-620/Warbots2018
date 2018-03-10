package org.jmhsrobotics.modules.testcommands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.jmhsrobotics.core.modulesystem.ModuleManager;
import org.jmhsrobotics.mockhardware.MockDrive;
import org.jmhsrobotics.modules.OutputSmoother;

public class TestJerkLimiter
{
	public static void main(String[] args)
	{
		File file = new File("data.txt");
		try
		{
			System.setOut(new PrintStream(new FileOutputStream(file)));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
	}
}
