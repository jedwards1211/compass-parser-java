package org.andork.compass.plot;

import java.util.Objects;

import org.andork.unit.Length;
import org.andork.unit.UnitizedDouble;

public class Location {
	private UnitizedDouble<Length> northing;
	private UnitizedDouble<Length> easting;
	private UnitizedDouble<Length> vertical;

	public Location() {
		this(Length.meters(Double.NaN), Length.meters(Double.NaN), Length.meters(Double.NaN));
	}

	public Location(UnitizedDouble<Length> northing, UnitizedDouble<Length> easting, UnitizedDouble<Length> vertical) {
		this.northing = northing;
		this.easting = easting;
		this.vertical = vertical;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Location other = (Location) obj;
		if (easting == null) {
			if (other.easting != null) {
				return false;
			}
		}
		else if (!easting.equals(other.easting)) {
			return false;
		}
		if (northing == null) {
			if (other.northing != null) {
				return false;
			}
		}
		else if (!northing.equals(other.northing)) {
			return false;
		}
		if (vertical == null) {
			if (other.vertical != null) {
				return false;
			}
		}
		else if (!vertical.equals(other.vertical)) {
			return false;
		}
		return true;
	}

	public UnitizedDouble<Length> getEasting() {
		return easting;
	}

	public UnitizedDouble<Length> getNorthing() {
		return northing;
	}

	public UnitizedDouble<Length> getVertical() {
		return vertical;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (easting == null ? 0 : easting.hashCode());
		result = prime * result + (northing == null ? 0 : northing.hashCode());
		result = prime * result + (vertical == null ? 0 : vertical.hashCode());
		return result;
	}

	public void setEasting(UnitizedDouble<Length> easting) {
		this.easting = Objects.requireNonNull(easting);
	}

	public void setNorthing(UnitizedDouble<Length> northing) {
		this.northing = Objects.requireNonNull(northing);
	}

	public void setVertical(UnitizedDouble<Length> vertical) {
		this.vertical = Objects.requireNonNull(vertical);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder
			.append("Location [northing=")
			.append(northing)
			.append(", easting=")
			.append(easting)
			.append(", vertical=")
			.append(vertical)
			.append("]");
		return builder.toString();
	}

	public static void formatBounds(Location lowerBound, Location upperBound, StringBuilder builder) {
		builder
			.append(lowerBound.getNorthing())
			.append('\t')
			.append(upperBound.getNorthing())
			.append('\t')
			.append(lowerBound.getEasting())
			.append('\t')
			.append(upperBound.getEasting())
			.append('\t')
			.append(lowerBound.getVertical())
			.append('\t')
			.append(upperBound.getVertical());
	}
}
