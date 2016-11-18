package org.andork.compass.plot;

public class BeginFeatureCommand implements CompassPlotCommand {
	private String featureName;
	private double minValue = Double.NaN;
	private double maxValue = Double.NaN;

	public String getFeatureName() {
		return featureName;
	}

	/**
	 * @return the max value for this feature, or NaN if not specified
	 */
	public double getMaxValue() {
		return maxValue;
	}

	/**
	 * @return the min value for this feature, or NaN if not specified
	 */
	public double getMinValue() {
		return minValue;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}
}
