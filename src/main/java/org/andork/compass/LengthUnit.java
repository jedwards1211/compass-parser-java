package org.andork.compass;

public enum LengthUnit {
	DECIMAL_FEET, FEET_AND_INCHES, METERS;

	public static double convert(double feet, LengthUnit to) {
		return to == METERS ? feet * 0.3048 : feet;
	}
}
