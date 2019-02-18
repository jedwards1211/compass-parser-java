package org.andork.compass.project;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;

import org.andork.compass.NEVLocation;
import org.andork.unit.Angle;
import org.andork.unit.Length;
import org.andork.unit.UnitizedDouble;

public class LocationDirective extends NEVLocation implements CompassProjectDirective {
	public final int utmZone;
	public final UnitizedDouble<Angle> utmConvergence;
	public static final NumberFormat numberFormat = new DecimalFormat("0.000");

	public LocationDirective(UnitizedDouble<Length> easting, UnitizedDouble<Length> northing,
			UnitizedDouble<Length> elevation, int utmZone, UnitizedDouble<Angle> utmConvergence) {
		super(easting, northing, elevation);
		this.utmZone = utmZone;
		this.utmConvergence = Objects.requireNonNull(utmConvergence);
	}
	
	public String toString() {
		return new StringBuilder()
			.append('@')
			.append(numberFormat.format(easting.doubleValue(Length.meters))).append(',')
			.append(numberFormat.format(northing.doubleValue(Length.meters))).append(',')
			.append(numberFormat.format(elevation.doubleValue(Length.meters))).append(',')
			.append(utmZone).append(',')
			.append(numberFormat.format(utmConvergence.doubleValue(Angle.degrees))).append(';')
			.toString();
	}
}
