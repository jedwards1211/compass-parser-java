package org.andork.compass.plot;

import org.andork.unit.Length;
import org.andork.unit.UnitizedDouble;

public class CaveBoundsCommand implements BoundsCommand {
	private final Location lowerBound = new Location();
	private final Location upperBound = new Location();
	private UnitizedDouble<Length> distanceToFarthestStation = null;

	public UnitizedDouble<Length> getDistanceToFarthestStation() {
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

	public void setDistanceToFarthestStation(UnitizedDouble<Length> distanceToFarthestStation) {
		this.distanceToFarthestStation = distanceToFarthestStation;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("Z");
		format(builder);
		if (distanceToFarthestStation != null) {
			builder.append("\tI\t").append(distanceToFarthestStation.get(Length.feet));
		}
		return builder.toString();
	}
}
