package org.andork.compass.plot;

import java.math.BigDecimal;

public class CaveBoundsCommand implements BoundsCommand {
	private final Location lowerBound = new Location();
	private final Location upperBound = new Location();
	private BigDecimal distanceToFarthestStation = null;

	public BigDecimal getDistanceToFarthestStation() {
		return distanceToFarthestStation;
	}

	@Override
	public Location getLowerBound() {
		return lowerBound;
	}

	@Override
	public Location getUpperBound() {
		return upperBound;
	}

	public void setDistanceToFarthestStation(BigDecimal distanceToFarthestStation) {
		this.distanceToFarthestStation = distanceToFarthestStation;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("Z");
		format(builder);
		if (distanceToFarthestStation != null) {
			builder.append("\tI\t").append(distanceToFarthestStation);
		}
		return builder.toString();
	}
}
