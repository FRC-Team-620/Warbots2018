package org.jmhsrobotics.core.modulesystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;
import java.util.stream.Stream;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

class DummySendableBuilder implements SendableBuilder
{
	private String type;
	private List<DummySendableEntry> entries;
	
	public DummySendableBuilder()
	{
		entries = new ArrayList<>();
	}
	
	public String getType()
	{
		return type;
	}
	
	public Stream<DummySendableEntry> entries()
	{
		return entries.stream();
	}

	@Override
	public void setSmartDashboardType(String type)
	{
		this.type = type;
	}

	@Override
	public void setSafeState(Runnable func)
	{
	}

	@Override
	public void setUpdateTable(Runnable func)
	{
	}

	@Override
	public NetworkTableEntry getEntry(String key)
	{
		return null;
	}

	@Override
	public void addBooleanProperty(String key, BooleanSupplier getter, BooleanConsumer setter)
	{
		entries.add(new DummySendableEntry(key, () -> String.valueOf(getter.getAsBoolean())));
	}

	@Override
	public void addDoubleProperty(String key, DoubleSupplier getter, DoubleConsumer setter)
	{
		entries.add(new DummySendableEntry(key, () -> String.valueOf(getter.getAsDouble())));
	}

	@Override
	public void addStringProperty(String key, Supplier<String> getter, Consumer<String> setter)
	{
		entries.add(new DummySendableEntry(key, getter));
	}

	@Override
	public void addBooleanArrayProperty(String key, Supplier<boolean[]> getter, Consumer<boolean[]> setter)
	{
		entries.add(new DummySendableEntry(key, () -> Arrays.toString(getter.get())));
	}

	@Override
	public void addDoubleArrayProperty(String key, Supplier<double[]> getter, Consumer<double[]> setter)
	{
		entries.add(new DummySendableEntry(key, () -> Arrays.toString(getter.get())));
	}

	@Override
	public void addStringArrayProperty(String key, Supplier<String[]> getter, Consumer<String[]> setter)
	{
		entries.add(new DummySendableEntry(key, () -> Arrays.toString(getter.get())));
	}

	@Override
	public void addRawProperty(String key, Supplier<byte[]> getter, Consumer<byte[]> setter)
	{
		entries.add(new DummySendableEntry(key, () -> Arrays.toString(getter.get())));
	}

	@Override
	public void addValueProperty(String key, Supplier<NetworkTableValue> getter, Consumer<NetworkTableValue> setter)
	{
		entries.add(new DummySendableEntry(key, () -> String.valueOf(getter.get())));
	}
}