package org.jmhsrobotics.modules.testcommands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

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
