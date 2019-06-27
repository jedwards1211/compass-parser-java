package org.andork.compass.plot;

public interface BoundsCommand extends CompassPlotCommand {
	public Location getLowerBound();

	public Location getUpperBound();
	
	public default void format(StringBuilder builder) {
		Location lowerBound = getLowerBound();
		Location upperBound = getUpperBound();
		builder.append('\t').append(lowerBound.getNorthing())
			.append('\t').append(upperBound.getNorthing())
			.append('\t').append(lowerBound.getEasting())
			.append('\t').append(upperBound.getEasting())
			.append('\t').append(lowerBound.getVertical())
			.append('\t').append(upperBound.getVertical());
	}
}
