package org.andork.compass.project;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.andork.unit.Length;
import org.andork.unit.Unit;

public class FileDirective implements CompassProjectDirective {
	public final String file;
	public final List<LinkStation> linkStations;

	public FileDirective(String file, List<LinkStation> linkStations) {
		this.file = Objects.requireNonNull(file);
		this.linkStations = linkStations != null
			? linkStations
			: Collections.emptyList();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('#').append(file);
		for (LinkStation station : linkStations) {
			builder.append(",\r\n ").append(station.name);
			if (station.location != null) {
				Unit<Length> unit = station.location.easting.unit;
				builder.append('[').append(Length.feet == unit ? 'f' : 'm').append(',')
					.append(station.location.easting.doubleValue(unit)).append(',')
					.append(station.location.northing.doubleValue(unit)).append(',')
					.append(station.location.elevation.doubleValue(unit)).append(']');
			}
		}
		builder.append(';');
		return builder.toString();
	}
}
