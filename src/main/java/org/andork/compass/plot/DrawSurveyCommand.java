package org.andork.compass.plot;

import java.math.BigDecimal;
import java.util.Objects;

public class DrawSurveyCommand implements LocationCommand {
	private DrawOperation operation;
	private final Location location = new Location();
	private String stationName;
	private BigDecimal left = null;
	private BigDecimal right = null;
	private BigDecimal up = null;
	private BigDecimal down = null;
	private BigDecimal distanceFromEntrance = BigDecimal.ZERO;

	public DrawSurveyCommand(DrawOperation operation) {
		this.operation = Objects.requireNonNull(operation);
	}

	/**
	 * @return the distance from the entrance to the station
	 */
	public BigDecimal getDistanceFromEntrance() {
		return distanceFromEntrance;
	}

	@Override
	public BigDecimal getDown() {
		return down;
	}

	@Override
	public BigDecimal getLeft() {
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
	public BigDecimal getRight() {
		return right;
	}

	@Override
	public String getStationName() {
		return stationName;
	}

	@Override
	public BigDecimal getUp() {
		return up;
	}

	public void setDistanceFromEntrance(BigDecimal distanceFromEntrance) {
		this.distanceFromEntrance = distanceFromEntrance;
	}

	@Override
	public void setDown(BigDecimal down) {
		this.down = down;
	}

	@Override
	public void setLeft(BigDecimal left) {
		this.left = left;
	}

	public void setOperation(DrawOperation type) {
		operation = type;
	}

	@Override
	public void setRight(BigDecimal right) {
		this.right = right;
	}

	@Override
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	@Override
	public void setUp(BigDecimal up) {
		this.up = up;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DrawSurveyCommand [operation=").append(operation).append(", location=").append(location)
				.append(", stationName=").append(stationName).append(", left=").append(left).append(", right=")
				.append(right).append(", up=").append(up).append(", down=").append(down)
				.append(", distanceFromEntrance=").append(distanceFromEntrance).append("]");
		return builder.toString();
	}
}