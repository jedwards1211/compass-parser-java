package org.andork.compass.plot;

public class SurveyBoundsCommand implements BoundsCommand {
	private final Location lowerBound = new Location();
	private final Location upperBound = new Location();

	@Override
	public Location getLowerBound() {
		return lowerBound;
	}

	@Override
	public Location getUpperBound() {
		return upperBound;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("X");
		format(builder);
		return builder.toString();
	}

}
