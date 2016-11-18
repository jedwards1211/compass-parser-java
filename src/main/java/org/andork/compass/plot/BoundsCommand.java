package org.andork.compass.plot;

public interface BoundsCommand extends CompassPlotCommand {
	public Location getLowerBound();

	public Location getUpperBound();
}
