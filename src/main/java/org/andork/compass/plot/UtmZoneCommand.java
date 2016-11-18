package org.andork.compass.plot;

public class UtmZoneCommand implements CompassPlotCommand {
	private String utmZone;

	public String getUtmZone() {
		return utmZone;
	}

	public void setUtmZone(String utmZone) {
		this.utmZone = utmZone;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UTMZoneCommand [utmZone=").append(utmZone).append("]");
		return builder.toString();
	}
}
