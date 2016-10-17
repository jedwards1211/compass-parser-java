package org.andork.compass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.andork.compass.CompassParseError.Severity;
import org.junit.Test;

public class CompassParserTests {
	@Test
	public void azimuthTests() throws IOException {
		String text = "A3  A4    4.25   -15.00 -85.00  5.00  3.50  0.75  0.50 360.0 85.0";
		CompassParser parser = new CompassParser("test.dat", text);
		parser.setSource("test.dat");
		final CompassTripHeader header = new CompassTripHeader();
		header.setHasBacksights(true);
		parser.setTripHeader(header);
		parser.parseShot();

		assertEquals(parser.getErrors().size(), 2);
		assertTrue(parser.getErrors().stream().anyMatch(error -> error.equals(new CompassParseError(
				Severity.ERROR,
				"frontsight azimuth must be >= 0 and < 360",
				parser.getSource(),
				parser.getLine(),
				text.indexOf("-15.00"),
				text.indexOf("-15.00") + "-15.00".length() - 1))));
		assertTrue(parser.getErrors().stream().anyMatch(error -> error.equals(new CompassParseError(
				Severity.ERROR,
				"backsight azimuth must be >= 0 and < 360",
				parser.getSource(),
				parser.getLine(),
				text.indexOf("360.0"),
				text.indexOf("360.0") + "360.0".length() - 1))));
	}

	@Test
	public void flagTests() throws IOException {
		final CompassTripHeader header = new CompassTripHeader();
		header.setHasBacksights(false);

		CompassShot shot;
		shot = new CompassParser("test.dat", "A3  A4    4.25   15.00 -85.00  5.00  3.50  0.75  0.50 #|L#", header)
				.parseShot();
		assertTrue(shot.isExcludeFromLength());
		assertFalse(shot.isExcludeFromPlotting());
		assertFalse(shot.isExcludeFromAllProcessing());
		assertFalse(shot.isDoNotAdjust());

		shot = new CompassParser("test.dat", "A3  A4    4.25   15.00 -85.00  5.00  3.50  0.75  0.50 #|P#", header)
				.parseShot();
		assertFalse(shot.isExcludeFromLength());
		assertTrue(shot.isExcludeFromPlotting());
		assertFalse(shot.isExcludeFromAllProcessing());
		assertFalse(shot.isDoNotAdjust());

		shot = new CompassParser("test.dat", "A3  A4    4.25   15.00 -85.00  5.00  3.50  0.75  0.50 #|X#", header)
				.parseShot();
		assertFalse(shot.isExcludeFromLength());
		assertFalse(shot.isExcludeFromPlotting());
		assertTrue(shot.isExcludeFromAllProcessing());
		assertFalse(shot.isDoNotAdjust());

		shot = new CompassParser("test.dat", "A3  A4    4.25   15.00 -85.00  5.00  3.50  0.75  0.50 #|C#", header)
				.parseShot();
		assertFalse(shot.isExcludeFromLength());
		assertFalse(shot.isExcludeFromPlotting());
		assertFalse(shot.isExcludeFromAllProcessing());
		assertTrue(shot.isDoNotAdjust());

		CompassParser parser;
		parser = new CompassParser("test.dat", "A3  A4    4.25   15.00 -85.00  5.00  3.50  0.75  0.50 #|LCP#", header);
		shot = parser.parseShot();
		assertTrue(shot.isExcludeFromLength());
		assertTrue(shot.isExcludeFromPlotting());
		assertFalse(shot.isExcludeFromAllProcessing());
		assertTrue(shot.isDoNotAdjust());

		assertEquals(parser.getErrors().size(), 0);

		String text = "A3  A4    4.25   15.00 -85.00  5.00  3.50  0.75  0.50 #|Q#";
		final CompassParser parser2 = new CompassParser("test.dat", text, header);
		shot = parser2.parseShot();
		System.out.println(parser2.getErrors());
		assertTrue(parser2.getErrors().stream().anyMatch(error -> error.equals(new CompassParseError(
				Severity.WARNING,
				"unrecognized flag: Q",
				parser2.getSource(),
				parser2.getLine(),
				text.indexOf('Q')))));
	}

	@Test
	public void incTests() throws IOException {
		final CompassTripHeader header = new CompassTripHeader();
		header.setHasBacksights(true);

		String text = "A3  A4    4.25   15.00 -91.00  5.00  3.50  0.75  0.50 195.0 92.0";
		final CompassParser parser = new CompassParser("test.dat", text, header);
		parser.parseShot();

		assertEquals(parser.getErrors().size(), 2);
		assertTrue(parser.getErrors().stream().anyMatch(error -> error.equals(new CompassParseError(
				Severity.ERROR,
				"frontsight inclination must be between -90.0 and 90.0",
				parser.getSource(),
				parser.getLine(),
				text.indexOf("-91.00"),
				text.indexOf("-91.00") + "-91.00".length() - 1))));
		assertTrue(parser.getErrors().stream().anyMatch(error -> error.equals(new CompassParseError(
				Severity.ERROR,
				"backsight inclination must be between -90.0 and 90.0",
				parser.getSource(),
				parser.getLine(),
				text.indexOf("92.0"),
				text.indexOf("92.0") + "92.0".length() - 1))));
	}

	@Test
	public void lengthTests() throws IOException {
		final CompassTripHeader header = new CompassTripHeader();
		header.setHasBacksights(false);

		String text = "A3  A4    -4.25   15.00 -85.00  5.00  -3.50  0.75  0.50";
		final CompassParser parser = new CompassParser("test.dat", text, header);
		parser.parseShot();

		assertEquals(parser.getErrors().size(), 2);
		assertTrue(parser.getErrors().stream().anyMatch(error -> error.equals(new CompassParseError(
				Severity.ERROR,
				"length must be >= 0.0",
				parser.getSource(),
				parser.getLine(),
				text.indexOf("-4.25"),
				text.indexOf("-4.25") + "-4.25".length() - 1))));
		assertTrue(parser.getErrors().stream().anyMatch(error -> error.equals(new CompassParseError(
				Severity.ERROR,
				"up must be >= 0.0",
				parser.getSource(),
				parser.getLine(),
				text.indexOf("-3.50"),
				text.indexOf("-3.50") + "-3.50".length() - 1))));
	}

	@Test
	public void testParseBasicShot() throws IOException {
		final CompassTripHeader header = new CompassTripHeader();
		header.setHasBacksights(false);

		final String text = "A3  A4    4.25   15.00 -85.00  5.00  3.50  0.75  0.50";
		final CompassParser parser = new CompassParser("test.dat", text, header);
		final CompassShot shot = parser.parseShot();
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
