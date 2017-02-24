package org.andork.compass.survey;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.andork.compass.AzimuthUnit;
import org.andork.compass.CompassParseError;
import org.andork.compass.CompassParseError.Severity;
import org.andork.compass.InclinationUnit;
import org.andork.compass.LengthUnit;
import org.andork.compass.LrudAssociation;
import org.andork.compass.LrudItem;
import org.andork.segment.Segment;
import org.junit.Test;

public class CompassParserTests {
	private static Segment extract(Segment seg, String target) {
		int index = seg.indexOf(target);
		return seg.substring(index, index + target.length());
	}

	@Test
	public void azimuthTests() {
		final CompassSurveyParser parser = new CompassSurveyParser();
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
		final CompassSurveyParser parser = new CompassSurveyParser();
		final CompassTripHeader header = new CompassTripHeader();
		header.setHasBacksights(false);

		CompassShot shot;
		shot = parser.parseShot(new Segment("A3 A4 4.25 15.00 -85.00 5.00 3.50 0.75 0.50 #|L#", "test.txt", 0, 0),
				header);
		assertTrue(shot.isExcludedFromLength());
		assertFalse(shot.isExcludedFromPlotting());
		assertFalse(shot.isExcludedFromAllProcessing());
		assertFalse(shot.isDoNotAdjust());

		shot = parser.parseShot(new Segment("A3 A4 4.25 15.00 -85.00 5.00 3.50 0.75 0.50 #|P#", "test.txt", 0, 0),
				header);
		assertFalse(shot.isExcludedFromLength());
		assertTrue(shot.isExcludedFromPlotting());
		assertFalse(shot.isExcludedFromAllProcessing());
		assertFalse(shot.isDoNotAdjust());

		shot = parser.parseShot(new Segment("A3 A4 4.25 15.00 -85.00 5.00 3.50 0.75 0.50 #|X#", "test.txt", 0, 0),
				header);
		assertFalse(shot.isExcludedFromLength());
		assertFalse(shot.isExcludedFromPlotting());
		assertTrue(shot.isExcludedFromAllProcessing());
		assertFalse(shot.isDoNotAdjust());

		shot = parser.parseShot(new Segment("A3 A4 4.25 15.00 -85.00 5.00 3.50 0.75 0.50 #|C#", "test.txt", 0, 0),
				header);
		assertFalse(shot.isExcludedFromLength());
		assertFalse(shot.isExcludedFromPlotting());
		assertFalse(shot.isExcludedFromAllProcessing());
		assertTrue(shot.isDoNotAdjust());

		shot = parser.parseShot(new Segment("A3 A4 4.25 15.00 -85.00 5.00 3.50 0.75 0.50 #|LCP#", "test.txt", 0, 0),
				header);
		assertTrue(shot.isExcludedFromLength());
		assertTrue(shot.isExcludedFromPlotting());
		assertFalse(shot.isExcludedFromAllProcessing());
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
	public void incompleteShotTests() {
		CompassSurveyParser parser;
		Segment segment;

		final CompassTripHeader header = new CompassTripHeader();
		header.setHasBacksights(false);

		parser = new CompassSurveyParser();
		segment = new Segment("A3", "test.txt", 0, 0);
		assertNull(parser.parseShot(segment, header));
		assertEquals(2, parser.getErrors().size());
		assertEquals(Arrays.asList(
				new CompassParseError(Severity.ERROR, "missing to station name", segment.substring(segment.length())),
				new CompassParseError(Severity.ERROR, "missing length", segment.substring(segment.length()))),
				parser.getErrors());

		parser = new CompassSurveyParser();
		segment = new Segment("A3 A4", "test.txt", 0, 0);
		assertNull(parser.parseShot(segment, header));
		assertEquals(Arrays.asList(
				new CompassParseError(Severity.ERROR, "missing length", segment.substring(segment.length()))),
				parser.getErrors());
	}

	@Test
	public void incTests() {
		final CompassSurveyParser parser = new CompassSurveyParser();
		final CompassTripHeader header = new CompassTripHeader();
		header.setHasBacksights(true);

		String text = "A3 A4 4.25 15.00 -91.00 5.00 3.50 0.75 0.50 195.0 92.0";
		Segment segment = new Segment(text, "test.txt", 0, 0);
		parser.parseShot(segment, header);

		assertEquals(parser.getErrors().size(), 2);
		assertTrue(parser.getErrors().stream().anyMatch(error -> error.equals(new CompassParseError(
				Severity.ERROR,
				"frontsight inclination must be between -90 and 90",
				extract(segment, "-91.00")))));
		assertTrue(parser.getErrors().stream().anyMatch(error -> error.equals(new CompassParseError(
				Severity.ERROR,
				"backsight inclination must be between -90 and 90",
				extract(segment, "92.0")))));
	}

	@Test
	public void lengthTests() {
		final CompassSurveyParser parser = new CompassSurveyParser();
		final CompassTripHeader header = new CompassTripHeader();
		header.setHasBacksights(false);

		String text = "A3 A4 -4.25 15.00 -85.00 5.00 -3.50 0.75 0.50";
		Segment segment = new Segment(text, "test.txt", 0, 0);
		parser.parseShot(segment, header);

		assertEquals(1, parser.getErrors().size());
		assertTrue(parser.getErrors().stream().anyMatch(error -> error.equals(new CompassParseError(
				Severity.ERROR,
				"length must be >= 0",
				extract(segment, "-4.25")))));
	}

	@Test
	public void testFlagsAndComments() {
		CompassSurveyParser parser;
		CompassTripHeader header;
		CompassShot shot;

		parser = new CompassSurveyParser();
		header = new CompassTripHeader();
		header.setHasBacksights(false);
		shot = parser.parseShot(new Segment("A3 A4 4.25 15.00 -85.00 5.00 3.50 0.75 0.50 #|LX #", "test.txt", 0, 0),
				header);
		assertTrue(shot.isExcludedFromLength());
		assertFalse(shot.isExcludedFromPlotting());
		assertTrue(shot.isExcludedFromAllProcessing());
		assertNull(shot.getComment());
		assertEquals(parser.getErrors().size(), 0);

		parser = new CompassSurveyParser();
		header = new CompassTripHeader();
		header.setHasBacksights(false);
		shot = parser.parseShot(
				new Segment("A3 A4 4.25 15.00 -85.00 5.00 3.50 0.75 0.50 #|LX#  blah blah", "test.txt", 0, 0),
				header);
		assertTrue(shot.isExcludedFromLength());
		assertFalse(shot.isExcludedFromPlotting());
		assertTrue(shot.isExcludedFromAllProcessing());
		assertEquals(shot.getComment(), "blah blah");
		assertEquals(parser.getErrors().size(), 0);

		parser = new CompassSurveyParser();
		header = new CompassTripHeader();
		header.setHasBacksights(false);
		shot = parser.parseShot(
				new Segment("A3 A4 4.25 15.00 -85.00 5.00 3.50 0.75 0.50  blah blah", "test.txt", 0, 0),
				header);
		assertFalse(shot.isExcludedFromLength());
		assertFalse(shot.isExcludedFromPlotting());
		assertFalse(shot.isExcludedFromAllProcessing());
		assertEquals(shot.getComment(), "blah blah");
		assertEquals(parser.getErrors().size(), 0);

		parser = new CompassSurveyParser();
		header = new CompassTripHeader();
		header.setHasBacksights(true);
		shot = parser.parseShot(
				new Segment("A3 A4 4.25 15.00 -85.00 5.00 3.50 0.75 0.50 195.0 85.0 #|LX#", "test.txt", 0, 0),
				header);
		assertTrue(shot.isExcludedFromLength());
		assertFalse(shot.isExcludedFromPlotting());
		assertTrue(shot.isExcludedFromAllProcessing());
		assertNull(shot.getComment());
		assertEquals(parser.getErrors().size(), 0);

		parser = new CompassSurveyParser();
		header = new CompassTripHeader();
		header.setHasBacksights(true);
		shot = parser.parseShot(
				new Segment("A3 A4 4.25 15.00 -85.00 5.00 3.50 0.75 0.50 195.0 85.0 #|LX#  blah blah", "test.txt", 0,
						0),
				header);
		assertTrue(shot.isExcludedFromLength());
		assertFalse(shot.isExcludedFromPlotting());
		assertTrue(shot.isExcludedFromAllProcessing());
		assertEquals(shot.getComment(), "blah blah");
		assertEquals(parser.getErrors().size(), 0);

		parser = new CompassSurveyParser();
		header = new CompassTripHeader();
		header.setHasBacksights(true);
		shot = parser.parseShot(
				new Segment("A3 A4 4.25 15.00 -85.00 5.00 3.50 0.75 0.50 195.0 85.0  blah blah", "test.txt", 0, 0),
				header);
		assertFalse(shot.isExcludedFromLength());
		assertFalse(shot.isExcludedFromPlotting());
		assertFalse(shot.isExcludedFromAllProcessing());
		assertEquals(shot.getComment(), "blah blah");
		assertEquals(parser.getErrors().size(), 0);
	}

	@Test
	public void testParseBasicShot() {
		final CompassSurveyParser parser = new CompassSurveyParser();
		final CompassTripHeader header = new CompassTripHeader();
		header.setHasBacksights(false);

		final CompassShot shot = parser
				.parseShot(new Segment("A3 A4 4.25 15.00 -85.00 5.00 3.50 0.75 0.50", "test.txt", 0, 0), header);
		assertEquals(shot.getFromStationName(), "A3");
		assertEquals(shot.getToStationName(), "A4");
		assertEquals(new BigDecimal("4.25"), shot.getLength());
		assertEquals(new BigDecimal("15.00"), shot.getFrontsightAzimuth());
		assertEquals(new BigDecimal("-85.00"), shot.getFrontsightInclination());
		assertEquals(new BigDecimal("5.00"), shot.getLeft());
		assertEquals(new BigDecimal("3.50"), shot.getUp());
		assertEquals(new BigDecimal("0.75"), shot.getDown());
		assertEquals(new BigDecimal("0.50"), shot.getRight());
		assertFalse(shot.isExcludedFromLength());
		assertFalse(shot.isExcludedFromPlotting());
		assertFalse(shot.isExcludedFromAllProcessing());
		assertNull(shot.getComment());
		assertEquals(parser.getErrors().size(), 0);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testParseCompassSurveyData() {
		String text = "SECRET CAVE\n" +
				"SURVEY NAME: A\n" +
				"SURVEY DATE: 7 10 79  COMMENT:Entrance Passage\n" +
				"SURVEY TEAM:\n" +
				"D.SMITH,R.BROWN,S.MURRAY\n" +
				"DECLINATION: 1.00  FORMAT: DDDDLUDRADLNF  CORRECTIONS: 2.00 3.00 4.00 CORRECTIONS2: 5.0 6.0\n" +
				"\n" +
				"FROM TO  LENGTH BEARING  DIP    LEFT    UP  DOWN RIGHT\n" +
				"\n" +
				"A2  A1   12.00  135.00   5.00  0.00  4.00  0.50  0.00  Big Room\n" +
				"A2  A3   41.17   46.00   2.00  0.00  0.00  0.00  0.00  #|PC# Room\n" +
				"A3  A4    4.25   15.00 -85.00  5.00  3.50  0.75  0.50\n" +
				"A4  A5   22.50  129.00 -21.00  0.00  0.00  0.00  0.00  #|PX#\n" +
				"\f\n" +
				"SECRET CAVE\n" +
				"SURVEY NAME: B\n" +
				"SURVEY DATE: 7 10 79  COMMENT:Big Room Survey\n" +
				"SURVEY TEAM:\n" +
				"D.SMITH,R.BROWN,S.MURRAY\n" +
				"DECLINATION: 1.00  FORMAT: DDDDLUDRADLNT  CORRECTIONS: 2.00 3.00 4.00 CORRECTIONS2: 5.0 6.0 \n" +
				"\n" +
				"FROM TO   LEN  BEAR   INC LEFT   UP DOWN RIGHT AZM2 INC2 FLAGS COMMENTS\n" +
				"\n" +
				"B2  B1  13.0  35.0  15.0  0.0  2.0  1.5  1.0 215.0 -15.0      Side Passage\n" +
				"B2  B3  22.1  16.0  22.0  6.0  1.0  0.0  2.0 196.0 -22.0 #|PC#\n" +
				"B3  B4   3.2  11.0 -82.0  2.0  2.5  2.7  3.5 191.0  82.0\n" +
				"B4  B5  23.5 111.0  11.0  0.0  0.0  1.0  1.0 291.0 -11.0 #|PX#\n" +
				"\f";

		final CompassSurveyParser parser = new CompassSurveyParser();
		List<CompassTrip> trips = parser.parseCompassSurveyData(new Segment(text, "test.txt", 0, 0));
		assertEquals(trips.size(), 2);

		CompassShot shot;

		CompassTrip trip2 = trips.get(1);
		assertEquals("SECRET CAVE", trip2.getHeader().getCaveName());
		assertEquals(new Date(79, 6, 10), trip2.getHeader().getDate());
		assertEquals("D.SMITH,R.BROWN,S.MURRAY", trip2.getHeader().getTeam());
		assertEquals(new BigDecimal("1.00"), trip2.getHeader().getDeclination());
		assertEquals(trip2.getHeader().getAzimuthUnit(), AzimuthUnit.DEGREES);
		assertEquals(trip2.getHeader().getLengthUnit(), LengthUnit.DECIMAL_FEET);
		assertEquals(trip2.getHeader().getLrudUnit(), LengthUnit.DECIMAL_FEET);
		assertEquals(trip2.getHeader().getInclinationUnit(), InclinationUnit.DEGREES);
		assertArrayEquals(trip2.getHeader().getLrudOrder(), new LrudItem[] {
				LrudItem.LEFT,
				LrudItem.UP,
				LrudItem.DOWN,
				LrudItem.RIGHT,
		});
		assertArrayEquals(trip2.getHeader().getShotMeasurementOrder(), new ShotItem[] {
				ShotItem.AZIMUTH,
				ShotItem.INCLINATION,
				ShotItem.LENGTH,
		});
		assertFalse(trip2.getHeader().hasBacksights());
		assertEquals(LrudAssociation.TO, trip2.getHeader().getLrudAssociation());

		shot = trip2.getShots().get(0);
		assertEquals("B2", shot.getFromStationName());
		assertEquals("B1", shot.getToStationName());
		assertEquals(new BigDecimal("13.0"), shot.getLength());
		assertEquals(new BigDecimal("35.0"), shot.getFrontsightAzimuth());
		assertEquals(new BigDecimal("15.0"), shot.getFrontsightInclination());
		assertEquals(new BigDecimal("0.0"), shot.getLeft());
		assertEquals(new BigDecimal("2.0"), shot.getUp());
		assertEquals(new BigDecimal("1.5"), shot.getDown());
		assertEquals(new BigDecimal("1.0"), shot.getRight());

		assertEquals(parser.getErrors().size(), 0);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testParseCorrectTripHeader() {
		final CompassSurveyParser parser = new CompassSurveyParser();
		final CompassTripHeader header = parser.parseTripHeader(new Segment("SECRET CAVE\n" +
				"SURVEY NAME: A\n" +
				"SURVEY DATE: 7 10 79  COMMENT:Entrance Passage\n" +
				"SURVEY TEAM:\n" +
				"D.SMITH,R.BROWN,S.MURRAY\n" +
				"DECLINATION: 1.00  FORMAT: DDDDLUDRADLBF  CORRECTIONS: 2.00 3.00 4.00 CORRECTIONS2: 5.0 6.0",
				"test.txt", 0, 0));
		assertEquals(header.getCaveName(), "SECRET CAVE");
		assertEquals(header.getSurveyName(), "A");
		assertEquals(header.getDate(), new Date(79, 6, 10));
		assertEquals(header.getTeam(), "D.SMITH,R.BROWN,S.MURRAY");
		assertEquals(new BigDecimal("1.00"), header.getDeclination());
		assertEquals(header.getAzimuthUnit(), AzimuthUnit.DEGREES);
		assertEquals(header.getLengthUnit(), LengthUnit.DECIMAL_FEET);
		assertEquals(header.getLrudUnit(), LengthUnit.DECIMAL_FEET);
		assertEquals(header.getInclinationUnit(), InclinationUnit.DEGREES);
		assertArrayEquals(header.getLrudOrder(), new LrudItem[] {
				LrudItem.LEFT,
				LrudItem.UP,
				LrudItem.DOWN,
				LrudItem.RIGHT,
		});
		assertArrayEquals(header.getShotMeasurementOrder(), new ShotItem[] {
				ShotItem.AZIMUTH,
				ShotItem.INCLINATION,
				ShotItem.LENGTH,
		});
		assertTrue(header.hasBacksights());
		assertEquals(header.getLrudAssociation(), LrudAssociation.FROM);
		assertEquals(parser.getErrors().size(), 0);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testParseTripHeaderWithoutOptionalFlags() {
		final CompassSurveyParser parser = new CompassSurveyParser();
		final CompassTripHeader header = parser.parseTripHeader(new Segment("SECRET CAVE\n" +
				"SURVEY NAME: A\n" +
				"SURVEY DATE: 7 10 79  COMMENT:Entrance Passage\n" +
				"SURVEY TEAM:\n" +
				"D.SMITH,R.BROWN,S.MURRAY\n" +
				"DECLINATION: 1.00  FORMAT: DDDDLUDRADL\n" +
				"\n" +
				"    FROM       TO   LENGTH  BEARING     DIP      LEFT      UP      DOWN     RIGHT\n" +
				"\n",
				"test.txt", 0, 0));
		assertEquals(header.getCaveName(), "SECRET CAVE");
		assertEquals(header.getSurveyName(), "A");
		assertEquals(header.getDate(), new Date(79, 6, 10));
		assertEquals(header.getTeam(), "D.SMITH,R.BROWN,S.MURRAY");
		assertEquals(new BigDecimal("1.00"), header.getDeclination());
		assertEquals(header.getAzimuthUnit(), AzimuthUnit.DEGREES);
		assertEquals(header.getLengthUnit(), LengthUnit.DECIMAL_FEET);
		assertEquals(header.getLrudUnit(), LengthUnit.DECIMAL_FEET);
		assertEquals(header.getInclinationUnit(), InclinationUnit.DEGREES);
		assertArrayEquals(header.getLrudOrder(), new LrudItem[] {
				LrudItem.LEFT,
				LrudItem.UP,
				LrudItem.DOWN,
				LrudItem.RIGHT,
		});
		assertArrayEquals(header.getShotMeasurementOrder(), new ShotItem[] {
				ShotItem.AZIMUTH,
				ShotItem.INCLINATION,
				ShotItem.LENGTH,
		});
		assertFalse(header.hasBacksights());
		assertEquals(LrudAssociation.FROM, header.getLrudAssociation());
		assertEquals(0, parser.getErrors().size());
	}

	@Test
	public void testParseShotWithBacksights() {
		final CompassSurveyParser parser = new CompassSurveyParser();
		final CompassTripHeader header = new CompassTripHeader();
		header.setHasBacksights(true);

		final CompassShot shot = parser
				.parseShot(new Segment("A3 A4 4.25 15.00 -85.00 5.00 3.50 0.75 0.50 195.0 85.00", "test.txt", 0, 0),
						header);
		assertEquals(shot.getFromStationName(), "A3");
		assertEquals(shot.getToStationName(), "A4");
		assertEquals(shot.getLength(), new BigDecimal(4.25));
		assertEquals(new BigDecimal("15.00"), shot.getFrontsightAzimuth());
		assertEquals(new BigDecimal("-85.00"), shot.getFrontsightInclination());
		assertEquals(new BigDecimal("5.00"), shot.getLeft());
		assertEquals(new BigDecimal("3.50"), shot.getUp());
		assertEquals(new BigDecimal("0.75"), shot.getDown());
		assertEquals(new BigDecimal("0.50"), shot.getRight());
		assertEquals(new BigDecimal("195.0"), shot.getBacksightAzimuth());
		assertEquals(new BigDecimal("85.00"), shot.getBacksightInclination());
		assertFalse(shot.isExcludedFromLength());
		assertFalse(shot.isExcludedFromPlotting());
		assertFalse(shot.isExcludedFromAllProcessing());
		assertNull(shot.getComment());
		assertEquals(parser.getErrors().size(), 0);
	}

	@Test
	public void testSplitHeaderAndData() {
		Segment[] parts;
		String text;

		text = "SECRET CAVE\n" +
				"SURVEY NAME: A\n" +
				"SURVEY DATE: 7 10 79  COMMENT:Entrance Passage\n" +
				"SURVEY TEAM:\n" +
				"D.SMITH,R.BROWN,S.MURRAY\n" +
				"DECLINATION: 1.00  FORMAT: DDDDLUDRADLNF  CORRECTIONS: 2.00 3.00 4.00 CORRECTIONS2: 5.0 6.0\n" +
				"\n" +
				"FROM TO  LENGTH BEARING  DIP    LEFT    UP  DOWN RIGHT\n" +
				"\n" +
				"A2  A1   12.00  135.00   5.00  0.00  4.00  0.50  0.00  Big Room\n" +
				"A2  A3   41.17   46.00   2.00  0.00  0.00  0.00  0.00  #|PC# Room";
		parts = CompassSurveyParser.splitHeaderAndData(new Segment(text, "test.txt", 0, 0));
		assertEquals(text.substring(0, text.indexOf("A2")).trim(), parts[0].toString());
		assertEquals(text.substring(text.indexOf("A2")).trim(), parts[1].toString());
	}
}
