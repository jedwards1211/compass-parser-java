package org.andork.compass;

import java.math.BigDecimal;

public enum AzimuthUnit {
	DEGREES, QUADS, GRADS;

	public static BigDecimal convert(BigDecimal degrees, AzimuthUnit to) {
		if (degrees == null) {
			return null;
		}
		return to == GRADS ? new BigDecimal(degrees.doubleValue() * 40 / 36) : degrees;
	}
}
