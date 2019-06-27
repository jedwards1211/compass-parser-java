package org.andork.compass.plot;

import java.math.BigDecimal;

public class BeginFeatureCommand implements CompassPlotCommand {
	private String featureName;
	private BigDecimal minValue = null;
	private BigDecimal maxValue = null;

	public String getFeatureName() {
		return featureName;
	}

	/**
	 * @return the max value for this feature, or NaN if not specified
	 */
	public BigDecimal getMaxValue() {
		return maxValue;
	}

	/**
	 * @return the min value for this feature, or NaN if not specified
	 */
	public BigDecimal getMinValue() {
		return minValue;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public void setMaxValue(BigDecimal maxValue) {
		this.maxValue = maxValue;
	}

	public void setMinValue(BigDecimal minValue) {
		this.minValue = minValue;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("F")
			.append(featureName.substring(0, Math.min(12, featureName.length())));
		if (minValue != null && maxValue != null) {
			builder.append("\tR\t");
			builder.append(minValue.floatValue()).append("\t").append(maxValue.floatValue());
		}
		return builder.toString();
	}
}
