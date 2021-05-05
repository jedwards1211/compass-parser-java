package org.andork.compass.plot;

import java.math.BigDecimal;

import org.andork.unit.Length;
import org.andork.unit.UnitizedDouble;

public class FeatureCommand implements LocationCommand {
	private final Location location = new Location();
	private String stationName;
	private UnitizedDouble<Length> left = null;
	private UnitizedDouble<Length> right = null;
	private UnitizedDouble<Length> up = null;
	private UnitizedDouble<Length> down = null;
	private BigDecimal value = null;

	@Override
	public UnitizedDouble<Length> getDown() {
		return down;
	}

	@Override
	public UnitizedDouble<Length> getLeft() {
		return left;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public UnitizedDouble<Length> getRight() {
		return right;
	}

	@Override
	public String getStationName() {
		return stationName;
	}

	@Override
	public UnitizedDouble<Length> getUp() {
		return up;
	}

	/**
	 * @return the value associated with this feature, or NaN if missing
	 */
	public BigDecimal getValue() {
		return value;
	}

	@Override
	public void setDown(UnitizedDouble<Length> down) {
		this.down = down;
	}

	@Override
	public void setLeft(UnitizedDouble<Length> left) {
		this.left = left;
	}

	@Override
	public void setRight(UnitizedDouble<Length> right) {
		this.right = right;
	}

	@Override
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	@Override
	public void setUp(UnitizedDouble<Length> up) {
		this.up = up;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("L");
		format(builder);
		if (value != null) {
			builder.append("\tV\t").append(value);
		}
		return builder.toString();
	}

}
