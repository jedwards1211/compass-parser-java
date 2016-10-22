package org.andork.compass;

public enum AzimuthUnit {
	DEGREES, QUADS, GRADS;

	public static double convert(double degrees, AzimuthUnit to) {
		return to == GRADS ? degrees * 40.0 / 36.0 : degrees;
	}
}
