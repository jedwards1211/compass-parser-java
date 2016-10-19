package org.andork.compass;

/**
 * Data for a shot between two stations.
 *
 * All <code>double</code> fields may be <code>NaN</code>, indicating the
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
	private double length = Double.NaN;
	/**
	 * Compass bearing toward to station at from station, in degrees
	 */
	private double frontsightAzimuth = Double.NaN;
	/**
	 * Vertical angle toward to station at from station, in degrees
	 */
	private double frontsightInclination = Double.NaN;
	/**
	 * Compass bearing toward from station at to station, in degrees
	 */
	private double backsightAzimuth = Double.NaN;
	/**
	 * Vertical angle toward from station at to station, in degrees
	 */
	private double backsightInclination = Double.NaN;
	/**
	 * Distance from station determined by corresponding
	 * {@link CompassTripHeader#getLrudAssociation()} to left wall (when looking
	 * toward the next station), in feet.
	 */
	private double left = Double.NaN;
	/**
	 * Distance from station determined by corresponding
	 * {@link CompassTripHeader#getLrudAssociation()} to left wall (when looking
	 * toward the next station), in feet.
	 */
	private double right = Double.NaN;
	/**
	 * Distance from station determined by corresponding
	 * {@link CompassTripHeader#getLrudAssociation()} to the ceiling.
	 */
	private double up = Double.NaN;
	/**
	 * Distance from station determined by corresponding
	 * {@link CompassTripHeader#getLrudAssociation()} to the floor.
	 */
	private double down = Double.NaN;
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

	public void setExcludedFromAllProcessing(boolean excludeFromAllProcessing) {
		this.excludedFromAllProcessing = excludeFromAllProcessing;
	}

	public void setExcludedFromLength(boolean excludeFromLength) {
		this.excludedFromLength = excludeFromLength;
	}

	public void setExcludedFromPlotting(boolean excludeFromPlotting) {
		this.excludedFromPlotting = excludeFromPlotting;
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
