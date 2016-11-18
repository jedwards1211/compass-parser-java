package org.andork.compass;

import java.math.BigDecimal;

public enum InclinationUnit {
	DEGREES, PERCENT_GRADE, DEGREES_AND_MINUTES, GRADS, DEPTH_GAUGE;

	public static BigDecimal convert(BigDecimal value, InclinationUnit toUnit) {
		if (value == null) {
			return null;
		}
		switch (toUnit) {
		case PERCENT_GRADE:
			return new BigDecimal(Math.tan(value.doubleValue()) * 100);
		case GRADS:
			return new BigDecimal(value.doubleValue() * 200 / 180);
		default:
			return value;
		}
	}
}
