package org.jmhsrobotics.modules.testcommands;

import org.jmhsrobotics.core.modulesystem.ModuleManager;
import org.jmhsrobotics.core.modulesystem.Sublinker;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;
import org.jmhsrobotics.mockhardware.MockDrive;
import org.jmhsrobotics.mockhardware.MockDriveController;
import org.jmhsrobotics.modules.autonomous.AutonomousCommand;

public class TestTransformses extends AutonomousCommand
{
	@Override
	public void onLink(Sublinker linker)
	{
		AutoStrategy path = new AutoStrategy(linker);
		path.addSequential(new Point(0, 60), 30);
		path.addSequential(Angle.RIGHT, Angle.fromDegrees(10));
	}

	public static void main(String[] args)
	{
		ModuleManager modules = new ModuleManager();
		modules.addModule(new MockDrive());
		modules.addModule(new MockDriveController());
		TestTransformses test = new TestTransformses();
		modules.addModule(test);
	}
}
