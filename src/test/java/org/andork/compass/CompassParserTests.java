package org.andork.compass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.andork.compass.CompassParseError.Severity;
import org.junit.Test;

public class CompassParserTests {
	private static Segment extract(Segment seg, String target) {
		int index = seg.indexOf(target);
		return seg.substring(index, index + target.length());
	}

	@Test
	public void azimuthTests() {
		final CompassParser parser = new CompassParser();
		final CompassTripHeader header = new CompassTripHeader();
		header.setHasBacksights(true);

		String text = "A3  A4    4.25   -15.00 -85.00  5.00  3.50  0.75  0.50 360.0 85.0";
		Segment segment = new Segment(text, "test.txt", 5, 0);
		parser.parseShot(segment, header);

		assertEquals(parser.getErrors().size(), 2);
		assertTrue(parser.getErrors().stream().anyMatch(error -> error.equals(new CompassParseError(
				Severity.ERROR,
				"frontsight azimuth must be >= 0 and < 360",
				extract(segment, "-15.00")))));
		assertTrue(parser.getErrors().stream().anyMatch(error -> error.equals(new CompassParseError(
				Severity.ERROR,
				"backsight azimuth must be >= 0 and < 360",
				extract(segment, "360.0")))));
	}

	@Test
	public void flagTests() {
		final CompassParser parser = new CompassParser();
		final CompassTripHeader header = new CompassTripHeader();
		header.setHasBacksights(false);

		CompassShot shot;
		shot = parser.parseShot(new Segment("A3 A4 4.25 15.00 -85.00 5.00 3.50 0.75 0.50 #|L#", "test.txt", 0, 0),
				header);
		assertTrue(shot.isExcludeFromLength());
		assertFalse(shot.isExcludeFromPlotting());
		assertFalse(shot.isExcludeFromAllProcessing());
		assertFalse(shot.isDoNotAdjust());

		shot = parser.parseShot(new Segment("A3 A4 4.25 15.00 -85.00 5.00 3.50 0.75 0.50 #|P#", "test.txt", 0, 0),
				header);
		assertFalse(shot.isExcludeFromLength());
		assertTrue(shot.isExcludeFromPlotting());
		assertFalse(shot.isExcludeFromAllProcessing());
		assertFalse(shot.isDoNotAdjust());

		shot = parser.parseShot(new Segment("A3 A4 4.25 15.00 -85.00 5.00 3.50 0.75 0.50 #|X#", "test.txt", 0, 0),
				header);
		assertFalse(shot.isExcludeFromLength());
		assertFalse(shot.isExcludeFromPlotting());
		assertTrue(shot.isExcludeFromAllProcessing());
		assertFalse(shot.isDoNotAdjust());

		shot = parser.parseShot(new Segment("A3 A4 4.25 15.00 -85.00 5.00 3.50 0.75 0.50 #|C#", "test.txt", 0, 0),
				header);
		assertFalse(shot.isExcludeFromLength());
		assertFalse(shot.isExcludeFromPlotting());
		assertFalse(shot.isExcludeFromAllProcessing());
		assertTrue(shot.isDoNotAdjust());

		shot = parser.parseShot(new Segment("A3 A4 4.25 15.00 -85.00 5.00 3.50 0.75 0.50 #|LCP#", "test.txt", 0, 0),
				header);
		assertTrue(shot.isExcludeFromLength());
		assertTrue(shot.isExcludeFromPlotting());
		assertFalse(shot.isExcludeFromAllProcessing());
		assertTrue(shot.isDoNotAdjust());

		assertEquals(parser.getErrors().size(), 0);

		String text = "A3 A4 4.25 15.00 -85.00 5.00 3.50 0.75 0.50 #|Q#";
		Segment segment = new Segment(text, "test.txt", 0, 0);
		shot = parser.parseShot(segment, header);
		assertTrue(parser.getErrors().stream().anyMatch(error -> error.equals(new CompassParseError(
				Severity.WARNING,
				"unrecognized flag: Q",
				extract(segment, "Q")))));
	}

	@Test
	public void incTests() {
		final CompassParser parser = new CompassParser();
		final CompassTripHeader header = new CompassTripHeader();
		header.setHasBacksights(true);

		String text = "A3 A4 4.25 15.00 -91.00 5.00 3.50 0.75 0.50 195.0 92.0";
		Segment segment = new Segment(text, "test.txt", 0, 0);
		parser.parseShot(segment, header);

		assertEquals(parser.getErrors().size(), 2);
		assertTrue(parser.getErrors().stream().anyMatch(error -> error.equals(new CompassParseError(
				Severity.ERROR,
				"frontsight inclination must be between -90.0 and 90.0",
				extract(segment, "-91.00")))));
		assertTrue(parser.getErrors().stream().anyMatch(error -> error.equals(new CompassParseError(
				Severity.ERROR,
				"backsight inclination must be between -90.0 and 90.0",
				extract(segment, "92.0")))));
	}

	@Test
	public void lengthTests() {
		final CompassParser parser = new CompassParser();
		final CompassTripHeader header = new CompassTripHeader();
		header.setHasBacksights(false);

		String text = "A3 A4 -4.25 15.00 -85.00 5.00 -3.50 0.75 0.50";
		Segment segment = new Segment(text, "test.txt", 0, 0);
		parser.parseShot(segment, header);

		assertEquals(parser.getErrors().size(), 2);
		assertTrue(parser.getErrors().stream().anyMatch(error -> error.equals(new CompassParseError(
				Severity.ERROR,
				"length must be >= 0.0",
				extract(segment, "-4.25")))));
		assertTrue(parser.getErrors().stream().anyMatch(error -> error.equals(new CompassParseError(
				Severity.ERROR,
				"up must be >= 0.0",
				extract(segment, "-3.50")))));
	}

	@Test
	public void testParseBasicShot() {
		final CompassParser parser = new CompassParser();
		final CompassTripHeader header = new CompassTripHeader();
		header.setHasBacksights(false);

		final CompassShot shot = parser
				.parseShot(new Segment("A3 A4 4.25 15.00 -85.00 5.00 3.50 0.75 0.50", "test.txt", 0, 0), header);
		assertEquals(shot.getFromStationName(), "A3");
		assertEquals(shot.getToStationName(), "A4");
		assertEquals(shot.getLength(), 4.25, 0.0);
		assertEquals(shot.getFrontsightAzimuth(), 15.0, 0.0);
		assertEquals(shot.getFrontsightInclination(), -85.0, 0.0);
		assertEquals(shot.getLeft(), 5.0, 0.0);
		assertEquals(shot.getUp(), 3.5, 0.0);
		assertEquals(shot.getDown(), 0.75, 0.0);
		assertEquals(shot.getRight(), 0.5, 0.0);
		assertFalse(shot.isExcludeFromLength());
		assertFalse(shot.isExcludeFromPlotting());
		assertFalse(shot.isExcludeFromAllProcessing());
		assertEquals(shot.getComment(), "");
		assertEquals(parser.getErrors().size(), 0);
	}
}
