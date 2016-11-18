package org.andork.compass.plot;

public interface LocationCommand extends CompassPlotCommand {
	/**
	 * @return distance from station to floor, or NaN if missing or passage
	 */
	public double getDown();

	/**
	 * @return distance from station to left wall or NaN if missing or passage
	 */
	public double getLeft();

	public Location getLocation();

	/**
	 * @return distance from station to right wall or NaN if missing or passage
	 */
	public double getRight();

	/**
	 * @return the name of the station
	 */
	public String getStationName();

	/**
	 * @return distance from station to ceiling or NaN if missing or passage
	 */
	public double getUp();

	public void setDown(double down);

	public void setLeft(double left);

	public void setRight(double right);

	public void setStationName(String stationName);

	public void setUp(double up);
}
