package org.andork.compass.plot;

import java.math.BigDecimal;

public interface LocationCommand extends CompassPlotCommand {
	/**
	 * @return distance from station to floor, or NaN if missing or passage
	 */
	public BigDecimal getDown();

	/**
	 * @return distance from station to left wall or NaN if missing or passage
	 */
	public BigDecimal getLeft();

	public Location getLocation();

	/**
	 * @return distance from station to right wall or NaN if missing or passage
	 */
	public BigDecimal getRight();

	/**
	 * @return the name of the station
	 */
	public String getStationName();

	/**
	 * @return distance from station to ceiling or NaN if missing or passage
	 */
	public BigDecimal getUp();

	public void setDown(BigDecimal down);

	public void setLeft(BigDecimal left);

	public void setRight(BigDecimal right);

	public void setStationName(String stationName);

	public void setUp(BigDecimal up);
	
	public default void format(StringBuilder builder) {
		Location location = getLocation();
		builder.append('\t').append(location.getNorthing());
		builder.append('\t').append(location.getEasting());
		builder.append('\t').append(location.getVertical());
		String stationName = getStationName();
		builder.append("\tS").append(stationName.substring(0, Math.min(12, stationName.length())));
		builder.append("\tP");
		BigDecimal left = getLeft();
		builder.append('\t').append(left != null ? left : "-9.0");
		BigDecimal up = getUp();
		builder.append('\t').append(up != null ? up : "-9.0");
		BigDecimal down = getDown();
		builder.append('\t').append(down != null ? down : "-9.0");
		BigDecimal right = getRight();
		builder.append('\t').append(right != null ? right : "-9.0");
	}
}
