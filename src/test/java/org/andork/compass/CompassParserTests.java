package org.andork.compass;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

public class CompassParserTests {
	@Test
	public void testParseBasicShot() {
		final CompassParser parser = new CompassParser();
		parser.setFile(new File("test.dat"));
		parser.setLine(5);
		final CompassTripHeader header = new CompassTripHeader();
		header.setHasBacksights(false);
		parser.setTripHeader(header);

		final CompassShot shot = parser.parseShot("A3  A4    4.25   15.00 -85.00  5.00  3.50  0.75  0.50");
		assertEquals(shot.getFromStationName(), "A3");
		assertEquals(shot.getToStationName(), "A4");
		assertEquals(shot.getLength(), 4.25, 0.0);
		assertEquals(shot.getFrontsightAzimuth(), 15.0, 0.0);
		assertEquals(shot.getFrontsightInclination(), -85.0, 0.0);
		assertEquals(shot.getLeft(), 5.0, 0.0);
		assertEquals(shot.getUp(), 3.5, 0.0);
		assertEquals(shot.getDown(), 0.75, 0.0);
		assertEquals(shot.getRight(), 0.5, 0.0);
		assertEquals(shot.getComment(), "");
	}
}
