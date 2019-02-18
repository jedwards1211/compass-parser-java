package org.andork.compass.project;

import java.util.Arrays;

import org.andork.compass.NEVLocation;
import org.andork.unit.Length;
import org.junit.Assert;
import org.junit.Test;

public class CompassProjectFileTests {
	@Test
	public void testToString() {
		FileDirective file = new FileDirective("FULFORD.DAT", Arrays.asList(
				new LinkStation("A", new NEVLocation(Length.feet(1.1), Length.feet(2.2), Length.feet(3.3))),
				new LinkStation("B", null),
				new LinkStation("C", new NEVLocation(Length.meters(4.4), Length.meters(5.5), Length.meters(6.6)))
			));
		Assert.assertEquals("#FULFORD.DAT,\r\n A[f,1.1,2.2,3.3],\r\n B,\r\n C[m,4.4,5.5,6.6];", file.toString());
	}
}
