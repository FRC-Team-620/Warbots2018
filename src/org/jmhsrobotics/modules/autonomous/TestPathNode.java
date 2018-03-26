package org.jmhsrobotics.modules.autonomous;

public class TestPathNode extends PathNode
{
	private double time;
	private String message;
	
	public TestPathNode(String message, double time)
	{
		this.message = message;
		this.time = time;
	}
	
	@Override
	protected void execute()
	{
		System.out.println("Test node: " + message);
	}

	@Override
	protected boolean isFinished()
	{
		return timeSinceInitialized() > time;
	}
	
	@Override
	protected void end()
	{
		System.out.println("Ending Test node: " + message);
	}
}
