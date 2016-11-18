package org.andork.compass.plot;

public class CaveBoundsCommand implements BoundsCommand {
	private final Location lowerBound = new Location();
	private final Location upperBound = new Location();
	private double distanceToFarthestStation = Double.NaN;

	public double getDistanceToFarthestStation() {
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

	public void setDistanceToFarthestStation(double distanceToFarthestStation) {
		this.distanceToFarthestStation = distanceToFarthestStation;
	}
}
