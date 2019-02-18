package org.andork.compass.project;

import java.util.Objects;

import org.andork.unit.Angle;
import org.andork.unit.UnitizedDouble;

public class UTMConvergenceDirective implements CompassProjectDirective {
	public final UnitizedDouble<Angle> utmConvergence;
	
	public UTMConvergenceDirective(UnitizedDouble<Angle> utmConvergence) {
		this.utmConvergence = Objects.requireNonNull(utmConvergence);
	}

	public String toString() {
		return "%" + LocationDirective.numberFormat.format(utmConvergence.doubleValue(Angle.degrees)) + ";";
	}
}
