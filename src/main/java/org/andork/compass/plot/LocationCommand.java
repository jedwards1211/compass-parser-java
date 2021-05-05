package org.andork.compass.plot;

import org.andork.unit.Length;
import org.andork.unit.UnitizedDouble;

public interface LocationCommand extends CompassPlotCommand {
	/**
	 * @return distance from station to floor, or NaN if missing or passage
	 */
	public UnitizedDouble<Length> getDown();

	/**
	 * @return distance from station to left wall or NaN if missing or passage
	 */
	public UnitizedDouble<Length> getLeft();

	public Location getLocation();

	/**
	 * @return distance from station to right wall or NaN if missing or passage
	 */
	public UnitizedDouble<Length> getRight();

	/**
	 * @return the name of the station
	 */
	public String getStationName();

	/**
	 * @return distance from station to ceiling or NaN if missing or passage
	 */
	public UnitizedDouble<Length> getUp();

	public void setDown(UnitizedDouble<Length> down);

	public void setLeft(UnitizedDouble<Length> left);

	public void setRight(UnitizedDouble<Length> right);

	public void setStationName(String stationName);

	public void setUp(UnitizedDouble<Length> up);

	public default void format(StringBuilder builder) {
		Location location = getLocation();
		builder.append('\t').append(location.getNorthing().get(Length.feet));
		builder.append('\t').append(location.getEasting().get(Length.feet));
		builder.append('\t').append(location.getVertical().get(Length.feet));
		String stationName = getStationName();
		builder.append("\tS").append(stationName.substring(0, Math.min(12, stationName.length())));
		builder.append("\tP");
		UnitizedDouble<Length> left = getLeft();
		builder.append('\t').append(left != null ? left.get(Length.feet) : "-9.0");
		UnitizedDouble<Length> up = getUp();
		builder.append('\t').append(up != null ? up.get(Length.feet) : "-9.0");
		UnitizedDouble<Length> down = getDown();
		builder.append('\t').append(down != null ? down.get(Length.feet) : "-9.0");
		UnitizedDouble<Length> right = getRight();
		builder.append('\t').append(right != null ? right.get(Length.feet) : "-9.0");
	}
}
