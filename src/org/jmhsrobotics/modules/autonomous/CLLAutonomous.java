package org.jmhsrobotics.modules.autonomous;

import org.jmhsrobotics.core.modulesystem.AutonomousCommand;
import org.jmhsrobotics.core.modulesystem.Sublinker;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.hardwareinterface.DriveController;

public class CLLAutonomous extends AutonomousCommand
{
	private @Submodule DriveController drive;
	
	@Override
	public String getID()
	{
		return "CLL";
	}
	
	@Override
	public void onLink(Sublinker sublinker)
	{
		LinkedComponentHelper helper = new LinkedComponentHelper(sublinker);
		helper.addSequential(new DriveTo(0, 60, 30));
		helper.addSequential(new DriveTo(60, 60, 6));
		helper.addSequential(new TurnTo(Angle.RIGHT, Angle.fromDegrees(10)));
		
//		addSequential(new Path(drive, false,
//				new PositionNode(0, 210, 30),
//				new PositionNode(-168, 200, 6),
//				new AngleNode(Angle.INVERSE_RIGHT, Angle.fromDegrees(10))));
	}
}
