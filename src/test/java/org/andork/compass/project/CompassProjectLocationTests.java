package org.andork.compass.project;

import org.andork.unit.Angle;
import org.andork.unit.Length;
import org.junit.Assert;
import org.junit.Test;

public class CompassProjectLocationTests {
	@Test
	public void testToString() {
		LocationDirective location = new LocationDirective(
			Length.meters(123.45), Length.meters(345.678), Length.meters(10234),
			13, Angle.degrees(2.04));
		Assert.assertEquals("@123.450,345.678,10234.000,13,2.040;", location.toString());
	}
}
