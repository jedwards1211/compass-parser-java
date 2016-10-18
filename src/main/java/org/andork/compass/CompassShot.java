package org.andork.compass;

/**
 * Data for a shot between two stations.
 *
 * All <code>double</code> fields may be <code>NaN</code>, indicating the
 * measurement is missing (which may mean the shot is invalid).
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
	private boolean excludeFromLength = false;
	/**
	 * Whether to exclude this shot from plots.
	 */
	private boolean excludeFromPlotting = false;
	/**
	 * Whether to exclude this shot from all processing.
	 */
	private boolean excludeFromAllProcessing = false;
	/**
	 * If <code>true</code>, this shot should not be adjusted when closing
	 * loops.
	 */
	private boolean doNotAdjust = false;

	public boolean doNotAdjust() {
		return doNotAdjust;
	}

	public boolean excludeFromAllProcessing() {
		return excludeFromAllProcessing;
	}

	public boolean excludeFromLength() {
		return excludeFromLength;
	}

	public boolean excludeFromPlotting() {
		return excludeFromPlotting;
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
