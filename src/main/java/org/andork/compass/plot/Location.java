package org.andork.compass.plot;

public class Location {
	private double northing;
	private double easting;
	private double vertical;

	public Location() {
		this(0, 0, 0);
	}

	public Location(double northing, double easting, double vertical) {
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
		if (Double.doubleToLongBits(easting) != Double.doubleToLongBits(other.easting)) {
			return false;
		}
		if (Double.doubleToLongBits(northing) != Double.doubleToLongBits(other.northing)) {
			return false;
		}
		if (Double.doubleToLongBits(vertical) != Double.doubleToLongBits(other.vertical)) {
			return false;
		}
		return true;
	}

	public void get(double[] nev) {
		nev[0] = northing;
		nev[1] = easting;
		nev[2] = vertical;
	}

	public double getEasting() {
		return easting;
	}

	public double getNorthing() {
		return northing;
	}

	public double getVertical() {
		return vertical;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(easting);
		result = prime * result + (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(northing);
		result = prime * result + (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(vertical);
		result = prime * result + (int) (temp ^ temp >>> 32);
		return result;
	}

	public void set(double[] nev) {
		northing = nev[0];
		easting = nev[1];
		vertical = nev[2];
	}

	public void setEasting(double easting) {
		this.easting = easting;
	}

	public void setNorthing(double northing) {
		this.northing = northing;
	}

	public void setVertical(double vertical) {
		this.vertical = vertical;
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
