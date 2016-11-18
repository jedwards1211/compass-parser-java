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
		StringBuilder builder = new StringBuilder();
		builder.append("SurveyBoundsCommand [lowerBound=").append(lowerBound).append(", upperBound=").append(upperBound)
				.append("]");
		return builder.toString();
	}

}
