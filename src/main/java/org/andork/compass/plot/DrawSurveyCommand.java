package org.andork.compass.plot;

import java.util.Objects;

import org.andork.unit.Length;
import org.andork.unit.UnitizedDouble;

public class DrawSurveyCommand implements LocationCommand {
	private DrawOperation operation;
	private final Location location = new Location();
	private String stationName;
	private UnitizedDouble<Length> left = null;
	private UnitizedDouble<Length> right = null;
	private UnitizedDouble<Length> up = null;
	private UnitizedDouble<Length> down = null;
	private UnitizedDouble<Length> distanceFromEntrance = Length.feet(0);

	public DrawSurveyCommand(DrawOperation operation) {
		this.operation = Objects.requireNonNull(operation);
	}

	/**
	 * @return the distance from the entrance to the station
	 */
	public UnitizedDouble<Length> getDistanceFromEntrance() {
		return distanceFromEntrance;
	}

	@Override
	public UnitizedDouble<Length> getDown() {
		return down;
	}

	@Override
	public UnitizedDouble<Length> getLeft() {
		return left;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	public DrawOperation getOperation() {
		return operation;
	}

	@Override
	public UnitizedDouble<Length> getRight() {
		return right;
	}

	@Override
	public String getStationName() {
		return stationName;
	}

	@Override
	public UnitizedDouble<Length> getUp() {
		return up;
	}

	public void setDistanceFromEntrance(UnitizedDouble<Length> distanceFromEntrance) {
		this.distanceFromEntrance = distanceFromEntrance;
	}

	@Override
	public void setDown(UnitizedDouble<Length> down) {
		this.down = down;
	}

	@Override
	public void setLeft(UnitizedDouble<Length> left) {
		this.left = left;
	}

	public void setOperation(DrawOperation type) {
		operation = type;
	}

	@Override
	public void setRight(UnitizedDouble<Length> right) {
		this.right = right;
	}

	@Override
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	@Override
	public void setUp(UnitizedDouble<Length> up) {
		this.up = up;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(operation == DrawOperation.MOVE_TO ? "M" : "D");
		format(builder);
		builder.append("\tI\t").append(distanceFromEntrance != null ? distanceFromEntrance.get(Length.feet) : "-9.0");
		return builder.toString();
	}
}
