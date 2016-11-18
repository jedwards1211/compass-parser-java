package org.andork.compass.survey;

import java.math.BigDecimal;

/**
 * Data for a shot between two stations.
 *
 * All <code>BigDecimal</code> fields may be <code>NaN</code>, indicating the
 * measurement is missing (in the case of length, azimuth, or inclination) or
 * passage (in the case of LRUDs).
 */
public class CompassShot {
	/**
	 * The name of the from station
	 */
	private String fromStationName;
	/**
	 * The name of the to station
	 */
	private String toStationName;
	/**
	 * Distance between the two stations, in feet
	 */
	private BigDecimal length = null;
	/**
	 * Compass bearing toward to station at from station, in degrees
	 */
	private BigDecimal frontsightAzimuth = null;
	/**
	 * Vertical angle toward to station at from station, in degrees
	 */
	private BigDecimal frontsightInclination = null;
	/**
	 * Compass bearing toward from station at to station, in degrees
	 */
	private BigDecimal backsightAzimuth = null;
	/**
	 * Vertical angle toward from station at to station, in degrees
	 */
	private BigDecimal backsightInclination = null;
	/**
	 * Distance from station determined by corresponding
	 * {@link CompassTripHeader#getLrudAssociation()} to left wall (when looking
	 * toward the next station), in feet.
	 */
	private BigDecimal left = null;
	/**
	 * Distance from station determined by corresponding
	 * {@link CompassTripHeader#getLrudAssociation()} to left wall (when looking
	 * toward the next station), in feet.
	 */
	private BigDecimal right = null;
	/**
	 * Distance from station determined by corresponding
	 * {@link CompassTripHeader#getLrudAssociation()} to the ceiling.
	 */
	private BigDecimal up = null;
	/**
	 * Distance from station determined by corresponding
	 * {@link CompassTripHeader#getLrudAssociation()} to the floor.
	 */
	private BigDecimal down = null;
	/**
	 * Arbitrary user comment for this shot
	 */
	private String comment;
	/**
	 * Whether to exclude this shot's {@link #length} from the total length of
	 * the cave.
	 */
	private boolean excludedFromLength = false;
	/**
	 * Whether to exclude this shot from plots.
	 */
	private boolean excludedFromPlotting = false;
	/**
	 * Whether to exclude this shot from all processing.
	 */
	private boolean excludedFromAllProcessing = false;
	/**
	 * If <code>true</code>, this shot should not be adjusted when closing
	 * loops.
	 */
	private boolean doNotAdjust = false;

	public BigDecimal getBacksightAzimuth() {
		return backsightAzimuth;
	}

	public BigDecimal getBacksightInclination() {
		return backsightInclination;
	}

	public String getComment() {
		return comment;
	}

	public BigDecimal getDown() {
		return down;
	}

	public String getFromStationName() {
		return fromStationName;
	}

	public BigDecimal getFrontsightAzimuth() {
		return frontsightAzimuth;
	}

	public BigDecimal getFrontsightInclination() {
		return frontsightInclination;
	}

	public BigDecimal getLeft() {
		return left;
	}

	public BigDecimal getLength() {
		return length;
	}

	public BigDecimal getRight() {
		return right;
	}

	public String getToStationName() {
		return toStationName;
	}

	public BigDecimal getUp() {
		return up;
	}

	public boolean isDoNotAdjust() {
		return doNotAdjust;
	}

	public boolean isExcludedFromAllProcessing() {
		return excludedFromAllProcessing;
	}

	public boolean isExcludedFromLength() {
		return excludedFromLength;
	}

	public boolean isExcludedFromPlotting() {
		return excludedFromPlotting;
	}

	public void setBacksightAzimuth(BigDecimal backsightAzimuth) {
		this.backsightAzimuth = backsightAzimuth;
	}

	public void setBacksightInclination(BigDecimal backsightInclination) {
		this.backsightInclination = backsightInclination;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setDoNotAdjust(boolean doNotAdjust) {
		this.doNotAdjust = doNotAdjust;
	}

	public void setDown(BigDecimal down) {
		this.down = down;
	}

	public void setExcludedFromAllProcessing(boolean excludeFromAllProcessing) {
		excludedFromAllProcessing = excludeFromAllProcessing;
	}

	public void setExcludedFromLength(boolean excludeFromLength) {
		excludedFromLength = excludeFromLength;
	}

	public void setExcludedFromPlotting(boolean excludeFromPlotting) {
		excludedFromPlotting = excludeFromPlotting;
	}

	public void setFromStationName(String fromStationName) {
		this.fromStationName = fromStationName;
	}

	public void setFrontsightAzimuth(BigDecimal frontsightAzimuth) {
		this.frontsightAzimuth = frontsightAzimuth;
	}

	public void setFrontsightInclination(BigDecimal frontsightInclination) {
		this.frontsightInclination = frontsightInclination;
	}

	public void setLeft(BigDecimal left) {
		this.left = left;
	}

	public void setLength(BigDecimal length) {
		this.length = length;
	}

	public void setRight(BigDecimal right) {
		this.right = right;
	}

	public void setToStationName(String toStationName) {
		this.toStationName = toStationName;
	}

	public void setUp(BigDecimal up) {
		this.up = up;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CompassShot [fromStationName=").append(fromStationName).append(", toStationName=")
				.append(toStationName).append(", length=").append(length).append(", frontsightAzimuth=")
				.append(frontsightAzimuth).append(", frontsightInclination=").append(frontsightInclination)
				.append(", backsightAzimuth=").append(backsightAzimuth).append(", backsightInclination=")
				.append(backsightInclination).append(", left=").append(left).append(", right=").append(right)
				.append(", up=").append(up).append(", down=").append(down).append(", comment=").append(comment)
				.append(", excludedFromLength=").append(excludedFromLength).append(", excludedFromPlotting=")
				.append(excludedFromPlotting).append(", excludedFromAllProcessing=").append(excludedFromAllProcessing)
				.append(", doNotAdjust=").append(doNotAdjust).append("]");
		return builder.toString();
	}
}
