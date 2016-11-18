package org.andork.compass.plot;

import java.math.BigDecimal;
import java.util.Objects;

public class Location {
	private BigDecimal northing;
	private BigDecimal easting;
	private BigDecimal vertical;

	public Location() {
		this(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
	}

	public Location(BigDecimal northing, BigDecimal easting, BigDecimal vertical) {
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
		} else if (!easting.equals(other.easting)) {
			return false;
		}
		if (northing == null) {
			if (other.northing != null) {
				return false;
			}
		} else if (!northing.equals(other.northing)) {
			return false;
		}
		if (vertical == null) {
			if (other.vertical != null) {
				return false;
			}
		} else if (!vertical.equals(other.vertical)) {
			return false;
		}
		return true;
	}

	public BigDecimal getEasting() {
		return easting;
	}

	public BigDecimal getNorthing() {
		return northing;
	}

	public BigDecimal getVertical() {
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

	public void setEasting(BigDecimal easting) {
		this.easting = Objects.requireNonNull(easting);
	}

	public void setNorthing(BigDecimal northing) {
		this.northing = Objects.requireNonNull(northing);
	}

	public void setVertical(BigDecimal vertical) {
		this.vertical = Objects.requireNonNull(vertical);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Location [northing=").append(northing).append(", easting=").append(easting)
				.append(", vertical=")
				.append(vertical).append("]");
		return builder.toString();
	}
}
