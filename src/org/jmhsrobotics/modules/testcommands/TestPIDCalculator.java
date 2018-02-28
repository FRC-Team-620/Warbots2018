package org.jmhsrobotics.modules.testcommands;

import org.jmhsrobotics.core.util.PIDCalculator;
import org.jmhsrobotics.core.util.PIDSensor;

public class TestPIDCalculator
{
	static double speed;
	
	public static void main(String[] args)
	{
		PIDSensor distanceSensor = PIDSensor.fromDispAndRate(() -> 120, () -> -10);
		PIDCalculator distanceController = new PIDCalculator(0.004, 0, 10, distanceSensor, o -> speed = o);
		distanceController.setInputRange(0, 360);
		distanceController.setContinuous();
		distanceController.setOutputRange(-1, 1);
		distanceController.setSetpoint(60);
		
		distanceController.update();
		System.out.println(speed);
	}
}
