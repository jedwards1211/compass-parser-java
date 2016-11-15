package org.andork.compass;

public enum InclinationUnit {
	DEGREES, PERCENT_GRADE, DEGREES_AND_MINUTES, GRADS, DEPTH_GAUGE;

	public static double convert(double value, InclinationUnit toUnit) {
		switch (toUnit) {
		case PERCENT_GRADE:
			return Math.tan(value) * 100;
		case GRADS:
			return value * 200 / 180;
		default:
			return value;
		}
	}
}
