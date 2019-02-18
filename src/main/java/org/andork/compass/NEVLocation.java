package org.andork.compass;

import java.util.Objects;

import org.andork.unit.Length;
import org.andork.unit.UnitizedDouble;

public class NEVLocation {
	public final UnitizedDouble<Length> easting;
	public final UnitizedDouble<Length> northing;
	public final UnitizedDouble<Length> elevation;

	public NEVLocation(UnitizedDouble<Length> easting, UnitizedDouble<Length> northing,
			UnitizedDouble<Length> elevation) {
		this.easting = Objects.requireNonNull(easting);
		this.northing = Objects.requireNonNull(northing);
		this.elevation = Objects.requireNonNull(elevation);
	}

	@Override
	public String toString() {
		return "NEVLocation [easting=" + easting + ", northing=" + northing + ", elevation=" + elevation + "]";
	}
}
