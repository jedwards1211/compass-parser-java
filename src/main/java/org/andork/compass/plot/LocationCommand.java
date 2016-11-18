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
}
