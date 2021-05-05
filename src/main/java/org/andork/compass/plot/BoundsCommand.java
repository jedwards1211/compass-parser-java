package org.andork.compass.plot;

import org.andork.unit.Length;

public interface BoundsCommand extends CompassPlotCommand {
	public Location getLowerBound();

	public Location getUpperBound();

	public default void format(StringBuilder builder) {
		Location lowerBound = getLowerBound();
		Location upperBound = getUpperBound();
		builder
			.append('\t')
			.append(lowerBound.getNorthing().get(Length.feet))
			.append('\t')
			.append(upperBound.getNorthing().get(Length.feet))
			.append('\t')
			.append(lowerBound.getEasting().get(Length.feet))
			.append('\t')
			.append(upperBound.getEasting().get(Length.feet))
			.append('\t')
			.append(lowerBound.getVertical().get(Length.feet))
			.append('\t')
			.append(upperBound.getVertical().get(Length.feet));
	}
}
