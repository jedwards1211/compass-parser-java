package org.andork.compass.plot;

public class FeatureCommand implements LocationCommand {
	private final Location location = new Location();
	private String stationName;
	private double left = Double.NaN;
	private double right = Double.NaN;
	private double up = Double.NaN;
	private double down = Double.NaN;
	private double value = Double.NaN;

	@Override
	public double getDown() {
		return down;
	}

	@Override
	public double getLeft() {
		return left;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public double getRight() {
		return right;
	}

	@Override
	public String getStationName() {
		return stationName;
	}

	@Override
	public double getUp() {
		return up;
	}

	/**
	 * @return the value associated with this feature, or NaN if missing
	 */
	public double getValue() {
		return value;
	}

	@Override
	public void setDown(double down) {
		this.down = down;
	}

	@Override
	public void setLeft(double left) {
		this.left = left;
	}

	@Override
	public void setRight(double right) {
		this.right = right;
	}

	@Override
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	@Override
	public void setUp(double up) {
		this.up = up;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
