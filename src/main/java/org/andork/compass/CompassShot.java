package org.andork.compass;

public class CompassShot {
	private String fromStationName;
	private String toStationName;
	private double length;
	private double frontsightAzimuth;
	private double frontsightInclination;
	private double backsightAzimuth;
	private double backsightInclination;
	private double left;
	private double right;
	private double up;
	private double down;
	private String comment;
	private boolean excludeFromLength;
	private boolean excludeFromPlotting;
	private boolean excludeFromAllProcessing;
	private boolean doNotAdjust;

	public double getBacksightAzimuth() {
		return backsightAzimuth;
	}

	public double getBacksightInclination() {
		return backsightInclination;
	}

	public String getComment() {
		return comment;
	}

	public double getDown() {
		return down;
	}

	public String getFromStationName() {
		return fromStationName;
	}

	public double getFrontsightAzimuth() {
		return frontsightAzimuth;
	}

	public double getFrontsightInclination() {
		return frontsightInclination;
	}

	public double getLeft() {
		return left;
	}

	public double getLength() {
		return length;
	}

	public double getRight() {
		return right;
	}

	public String getToStationName() {
		return toStationName;
	}

	public double getUp() {
		return up;
	}

	public boolean isDoNotAdjust() {
		return doNotAdjust;
	}

	public boolean isExcludeFromAllProcessing() {
		return excludeFromAllProcessing;
	}

	public boolean isExcludeFromLength() {
		return excludeFromLength;
	}

	public boolean isExcludeFromPlotting() {
		return excludeFromPlotting;
	}

	public void setBacksightAzimuth(double backsightAzimuth) {
		this.backsightAzimuth = backsightAzimuth;
	}

	public void setBacksightInclination(double backsightInclination) {
		this.backsightInclination = backsightInclination;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setDoNotAdjust(boolean doNotAdjust) {
		this.doNotAdjust = doNotAdjust;
	}

	public void setDown(double down) {
		this.down = down;
	}

	public void setExcludeFromAllProcessing(boolean excludeFromAllProcessing) {
		this.excludeFromAllProcessing = excludeFromAllProcessing;
	}

	public void setExcludeFromLength(boolean excludeFromLength) {
		this.excludeFromLength = excludeFromLength;
	}

	public void setExcludeFromPlotting(boolean excludeFromPlotting) {
		this.excludeFromPlotting = excludeFromPlotting;
	}

	public void setFromStationName(String fromStationName) {
		this.fromStationName = fromStationName;
	}

	public void setFrontsightAzimuth(double frontsightAzimuth) {
		this.frontsightAzimuth = frontsightAzimuth;
	}

	public void setFrontsightInclination(double frontsightInclination) {
		this.frontsightInclination = frontsightInclination;
	}

	public void setLeft(double left) {
		this.left = left;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public void setRight(double right) {
		this.right = right;
	}

	public void setToStationName(String toStationName) {
		this.toStationName = toStationName;
	}

	public void setUp(double up) {
		this.up = up;
	}
}
