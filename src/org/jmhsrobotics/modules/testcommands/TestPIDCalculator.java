package org.jmhsrobotics.modules.testcommands;

import org.jmhsrobotics.core.util.DummyPIDOutput;
import org.jmhsrobotics.core.util.PIDCalculator;
import org.jmhsrobotics.core.util.PIDSensor;

public class TestPIDCalculator
{
	public static void main(String[] args)
	{
		PIDSensor input = PIDSensor.fromDispAndRate(() -> 33, () -> -10);
		DummyPIDOutput output = new DummyPIDOutput();
		
		PIDCalculator calculator = new PIDCalculator(0.03, 0, 0.5, input, output);
		calculator.setInputRange(0, 360);
		calculator.setContinuous();
		calculator.setOutputRange(-0.5, 0.5);
		
		calculator.setSetpoint(30);
		
		calculator.update();
		System.out.println("Output: " + output.get());
	}
}
