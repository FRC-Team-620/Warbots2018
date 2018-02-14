package org.jmhsrobotics.modules.testcommands;

import org.jmhsrobotics.hardwareinterface.ElevatorController;
import org.jmhsrobotics.hardwareinterface.TurnTableController;

public class TestEnumLinkage
{
	public static void main(String[] args)
	{
		System.out.println("--Lifter--");
		for(ElevatorController.Position p : ElevatorController.Position.values())
			System.out.println(p + " prev: " + p.getAdjacentBelow() + " next: " + p.getAdjacentAbove());
		
		System.out.println("--TurnTable--");
		for(TurnTableController.Position p : TurnTableController.Position.values())
			System.out.println(p + " prev: " + p.getLeftAdjacent() + " next: " + p.getRightAdjacent());
	}
}
