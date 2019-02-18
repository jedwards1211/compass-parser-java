package org.andork.compass.project;

public class UTMZoneDirective implements CompassProjectDirective {
	public final int utmZone;

	public UTMZoneDirective(int utmZone) {
		if (utmZone < 1 || utmZone > 60) {
			throw new IllegalArgumentException("Invalid UTM Zone: " + utmZone);
		}
		this.utmZone = utmZone;
	}
	
	public String toString() {
		return "$" + utmZone + ";";
	}
}
