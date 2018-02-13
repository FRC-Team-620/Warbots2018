package org.jmhsrobotics.modules.testcommands;

import org.jmhsrobotics.hardwareinterface.HybridLifter;
import org.jmhsrobotics.hardwareinterface.TurnTable;

public class TestEnumLinkage
{
	public static void main(String[] args)
	{
		System.out.println("--Lifter--");
		for(HybridLifter.Position p : HybridLifter.Position.values())
			System.out.println(p + " prev: " + p.getAdjacentBelow() + " next: " + p.getAdjacentAbove());
		
		System.out.println("--TurnTable--");
		for(TurnTable.Position p : TurnTable.Position.values())
			System.out.println(p + " prev: " + p.getLeftAdjacent() + " next: " + p.getRightAdjacent());
	}
}
