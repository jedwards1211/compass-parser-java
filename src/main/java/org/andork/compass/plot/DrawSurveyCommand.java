package org.andork.compass.plot;

import java.util.Objects;

public class DrawSurveyCommand implements LocationCommand {
	private DrawOperation operation;
	private final Location location = new Location();
	private String stationName;
	private double left = Double.NaN;
	private double right = Double.NaN;
	private double up = Double.NaN;
	private double down = Double.NaN;
	private double distanceFromEntrance = 0;

	public DrawSurveyCommand(DrawOperation operation) {
		operation = Objects.requireNonNull(operation);
	}

	/**
	 * @return the distance from the entrance to the station
	 */
	public double getDistanceFromEntrance() {
		return distanceFromEntrance;
	}

	@Override
	public double getDown() {
		return down;
	}

	@Override
	public double getLeft() {
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
	public double getRight() {
		return right;
	}

	@Override
	public String getStationName() {
		return stationName;
	}

	@Override
	public double getUp() {
		return up;
	}

	public void setDistanceFromEntrance(double distanceFromEntrance) {
		this.distanceFromEntrance = distanceFromEntrance;
	}

	@Override
	public void setDown(double down) {
		this.down = down;
	}

	@Override
	public void setLeft(double left) {
		this.left = left;
	}

	public void setOperation(DrawOperation type) {
		operation = type;
	}

	@Override
	public void setRight(double right) {
		this.right = right;
	}

	@Override
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	@Override
	public void setUp(double up) {
		this.up = up;
	}
}
