package org.jmhsrobotics.modules.testcommands;

import org.jmhsrobotics.core.util.PIDCalculator;
import org.jmhsrobotics.core.util.PIDSensor;
import org.jmhsrobotics.modules.autonomous.AutoSwitcher;

public class TestPIDCalculator
{
	static double speed;
	
	public static void main(String[] args)
	{
		System.out.println(AutoSwitcher.StartingPosition.center.toString().toUpperCase().charAt(0));
		
		PIDSensor distanceSensor = PIDSensor.fromDispAndRate(() -> 0, () -> 0);
		PIDCalculator distanceController = new PIDCalculator(0.004, 0, 10, distanceSensor, o -> speed = -o);
		distanceController.setOutputRange(-1, 1);
		distanceController.setSetpoint(60);
		
		distanceController.update();
		System.out.println(speed);
	}
}
