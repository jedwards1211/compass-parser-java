package org.andork.compass.survey;

import org.andork.unit.Angle;
import org.andork.unit.Length;
import org.andork.unit.UnitizedDouble;

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
	private UnitizedDouble<Length> length = null;
	/**
	 * Compass bearing toward to station at from station, in degrees
	 */
	private UnitizedDouble<Angle> frontsightAzimuth = null;
	/**
	 * Vertical angle toward to station at from station, in degrees
	 */
	private UnitizedDouble<Angle> frontsightInclination = null;
	/**
	 * Compass bearing toward from station at to station, in degrees
	 */
	private UnitizedDouble<Angle> backsightAzimuth = null;
	/**
	 * Vertical angle toward from station at to station, in degrees
	 */
	private UnitizedDouble<Angle> backsightInclination = null;
	/**
	 * Distance from station determined by corresponding
	 * {@link CompassTripHeader#getLrudAssociation()} to left wall (when looking
	 * toward the next station), in feet.
	 */
	private UnitizedDouble<Length> left = null;
	/**
	 * Distance from station determined by corresponding
	 * {@link CompassTripHeader#getLrudAssociation()} to left wall (when looking
	 * toward the next station), in feet.
	 */
	private UnitizedDouble<Length> right = null;
	/**
	 * Distance from station determined by corresponding
	 * {@link CompassTripHeader#getLrudAssociation()} to the ceiling.
	 */
	private UnitizedDouble<Length> up = null;
	/**
	 * Distance from station determined by corresponding
	 * {@link CompassTripHeader#getLrudAssociation()} to the floor.
	 */
	private UnitizedDouble<Length> down = null;
	/**
	 * Arbitrary user comment for this shot
	 */
	private String comment;
	/**
	 * Whether to exclude this shot's {@link #length} from the total length of the
	 * cave.
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
	 * If <code>true</code>, this shot should not be adjusted when closing loops.
	 */
	private boolean doNotAdjust = false;

	public UnitizedDouble<Angle> getBacksightAzimuth() {
		return backsightAzimuth;
	}

	public UnitizedDouble<Angle> getBacksightInclination() {
		return backsightInclination;
	}

	public String getComment() {
		return comment;
	}

	public UnitizedDouble<Length> getDown() {
		return down;
	}

	public String getFromStationName() {
		return fromStationName;
	}

	public UnitizedDouble<Angle> getFrontsightAzimuth() {
		return frontsightAzimuth;
	}

	public UnitizedDouble<Angle> getFrontsightInclination() {
		return frontsightInclination;
	}

	public UnitizedDouble<Length> getLeft() {
		return left;
	}

	public UnitizedDouble<Length> getLength() {
		return length;
	}

	public UnitizedDouble<Length> getRight() {
		return right;
	}

	public String getToStationName() {
		return toStationName;
	}

	public UnitizedDouble<Length> getUp() {
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

	public void setBacksightAzimuth(UnitizedDouble<Angle> backsightAzimuth) {
		this.backsightAzimuth = backsightAzimuth;
	}

	public void setBacksightInclination(UnitizedDouble<Angle> backsightInclination) {
		this.backsightInclination = backsightInclination;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setDoNotAdjust(boolean doNotAdjust) {
		this.doNotAdjust = doNotAdjust;
	}

	public void setDown(UnitizedDouble<Length> down) {
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

	public void setFrontsightAzimuth(UnitizedDouble<Angle> frontsightAzimuth) {
		this.frontsightAzimuth = frontsightAzimuth;
	}

	public void setFrontsightInclination(UnitizedDouble<Angle> frontsightInclination) {
		this.frontsightInclination = frontsightInclination;
	}

	public void setLeft(UnitizedDouble<Length> left) {
		this.left = left;
	}

	public void setLength(UnitizedDouble<Length> length) {
		this.length = length;
	}

	public void setRight(UnitizedDouble<Length> right) {
		this.right = right;
	}

	public void setToStationName(String toStationName) {
		this.toStationName = toStationName;
	}

	public void setUp(UnitizedDouble<Length> up) {
		this.up = up;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder
			.append("CompassShot [fromStationName=")
			.append(fromStationName)
			.append(", toStationName=")
			.append(toStationName)
			.append(", length=")
			.append(length)
			.append(", frontsightAzimuth=")
			.append(frontsightAzimuth)
			.append(", frontsightInclination=")
			.append(frontsightInclination)
			.append(", backsightAzimuth=")
			.append(backsightAzimuth)
			.append(", backsightInclination=")
			.append(backsightInclination)
			.append(", left=")
			.append(left)
			.append(", right=")
			.append(right)
			.append(", up=")
			.append(up)
			.append(", down=")
			.append(down)
			.append(", comment=")
			.append(comment)
			.append(", excludedFromLength=")
			.append(excludedFromLength)
			.append(", excludedFromPlotting=")
			.append(excludedFromPlotting)
			.append(", excludedFromAllProcessing=")
			.append(excludedFromAllProcessing)
			.append(", doNotAdjust=")
			.append(doNotAdjust)
			.append("]");
		return builder.toString();
	}
}
