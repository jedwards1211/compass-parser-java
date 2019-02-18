package org.andork.compass.project;

import java.util.Objects;

public class DatumDirective implements CompassProjectDirective {
	public final String datum;

	public DatumDirective(String datum) {
		this.datum = Objects.requireNonNull(datum);
	}
	
	public String toString() {
		return '&' + datum + ';';
	}
}
