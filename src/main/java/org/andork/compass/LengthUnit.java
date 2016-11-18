package org.andork.compass;

import java.math.BigDecimal;

public enum LengthUnit {
	DECIMAL_FEET, FEET_AND_INCHES, METERS;

	private static final BigDecimal FEET_TO_METERS = new BigDecimal("0.3048");

	public static BigDecimal convert(BigDecimal feet, LengthUnit to) {
		if (feet == null) {
			return null;
		}
		return to == METERS ? feet.multiply(FEET_TO_METERS) : feet;
	}
}
