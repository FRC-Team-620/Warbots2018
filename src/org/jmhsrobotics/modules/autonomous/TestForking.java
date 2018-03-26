package org.jmhsrobotics.modules.autonomous;

public class TestForking extends AutonomousCommand
{
	@Override
	protected void initialize()
	{
		System.out.println("Testing forking");
	}
	
	@Override
	protected void loadPath(AutoStrategy strategy)
	{
		System.out.println("Loading path for fork testing");
		strategy.addSequential(new TestPathNode("Test 1", 2));
		
		AutoStrategy oneAndTwo = strategy.createBranch();
		oneAndTwo.addParallel(new TestPathNode("Test 2", 3));
		oneAndTwo.addParallel(new TestPathNode("Test 3", 1));
		strategy.addSequential(oneAndTwo);
		
		strategy.addSequential(new TestPathNode("Test 4", 2));
	}
	
	@Override
	protected void end()
	{
		System.out.println("Ending forking test");
	}
}
