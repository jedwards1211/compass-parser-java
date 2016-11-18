package org.andork.compass.plot;

import java.math.BigDecimal;

public class FeatureCommand implements LocationCommand {
	private final Location location = new Location();
	private String stationName;
	private BigDecimal left = null;
	private BigDecimal right = null;
	private BigDecimal up = null;
	private BigDecimal down = null;
	private BigDecimal value = null;

	@Override
	public BigDecimal getDown() {
		return down;
	}

	@Override
	public BigDecimal getLeft() {
		return left;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public BigDecimal getRight() {
		return right;
	}

	@Override
	public String getStationName() {
		return stationName;
	}

	@Override
	public BigDecimal getUp() {
		return up;
	}

	/**
	 * @return the value associated with this feature, or NaN if missing
	 */
	public BigDecimal getValue() {
		return value;
	}

	@Override
	public void setDown(BigDecimal down) {
		this.down = down;
	}

	@Override
	public void setLeft(BigDecimal left) {
		this.left = left;
	}

	@Override
	public void setRight(BigDecimal right) {
		this.right = right;
	}

	@Override
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	@Override
	public void setUp(BigDecimal up) {
		this.up = up;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
}
