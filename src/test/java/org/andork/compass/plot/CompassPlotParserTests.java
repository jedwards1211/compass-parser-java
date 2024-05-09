package org.andork.compass.plot;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Callable;

import org.andork.compass.CompassParseError;
import org.andork.compass.ExceptionRunnable;
import org.andork.segment.Segment;
import org.andork.segment.SegmentParseException;
import org.andork.segment.SegmentParser;
import org.andork.unit.Length;
import org.junit.Assert;
import org.junit.Test;

public class CompassPlotParserTests {
	private static void assertThrowsParseError(Callable<?> c, int index, String message) {
		try {
			c.call();
			Assert.fail("expected function to throw a SegmentParseException");
		} catch (Exception e) {
			SegmentParseException error = (SegmentParseException) e;
			Assert.assertEquals(index, error.getSegment().startCol);
			Assert.assertEquals(message, error.getMessage());
		}
	}

	private static void assertThrowsParseError(ExceptionRunnable r, int index, String message) {
		try {
			r.run();
			Assert.fail("expected function to throw a SegmentParseException");
		} catch (Exception e) {
			SegmentParseException error = (SegmentParseException) e;
			Assert.assertEquals(index, error.getSegment().startCol);
			Assert.assertEquals(message, error.getMessage());
		}
	}

	private static DrawSurveyCommand parseDrawSurveyCommand(String command) throws SegmentParseException {
		CompassPlotParser parser = new CompassPlotParser();
		return parser.parseDrawSurveyCommand(parser(command));
	}

	private static FeatureCommand parseFeatureCommand(String command) throws SegmentParseException {
		CompassPlotParser parser = new CompassPlotParser();
		return parser.parseFeatureCommand(parser(command));
	}

	private static SegmentParser parser(String text) {
		return new SegmentParser(new Segment(text, "", 0, 0));
	}

	@Test
	public void testInvalidParseDrawSurveyCommand() {
		assertThrowsParseError(() -> parseDrawSurveyCommand("Q"), 0, "invalid command (expected M or D)");
		assertThrowsParseError(() -> parseDrawSurveyCommand("D X"), 2, "invalid min northing");
		assertThrowsParseError(() -> parseDrawSurveyCommand("D128.2"), 1, "missing whitespace before northing");
		assertThrowsParseError(() -> parseDrawSurveyCommand("D 128.2-65.9"), 7, "missing whitespace before easting");
		assertThrowsParseError(() -> parseDrawSurveyCommand("D 128.2 -65.9 -86.8 S "), 21, "missing station name");
		assertThrowsParseError(() -> parseDrawSurveyCommand("D 128.2 -65.9 -86.8 SZ7 P0.0 "), 25,
				"missing whitespace before left");
		assertThrowsParseError(() -> parseDrawSurveyCommand("D 128.2 -65.9 -86.8I 21.8"), 19,
				"missing whitespace before next command");
	}

	@Test
	public void testInvalidParseFeatureCommand() {
		assertThrowsParseError(() -> parseFeatureCommand("Q"), 0, "invalid command (expected L)");
		assertThrowsParseError(() -> parseFeatureCommand("L X"), 2, "invalid min northing");
		assertThrowsParseError(() -> parseFeatureCommand("L128.2"), 1, "missing whitespace before northing");
		assertThrowsParseError(() -> parseFeatureCommand("L 128.2-65.9"), 7, "missing whitespace before easting");
		assertThrowsParseError(() -> parseFeatureCommand("L 128.2 -65.9 -86.8 S "), 21, "missing station name");
		assertThrowsParseError(() -> parseFeatureCommand("L 128.2 -65.9 -86.8 SZ7 P0.0 "), 25,
				"missing whitespace before left");
		assertThrowsParseError(() -> parseFeatureCommand("L 128.2 -65.9 -86.8I 21.8"), 19,
				"missing whitespace before next command");
	}

	@Test
	public void testParseDrawSurveyCommand() throws SegmentParseException {
		String line = "D   128.2   -65.9   -86.8  SZ7  P    0.0    3.0    1.0    2.0  I   21.8";
		DrawSurveyCommand command = parseDrawSurveyCommand(line);
		Assert.assertEquals(Length.feet(128.2), command.getLocation().getNorthing());
		Assert.assertEquals(Length.feet(-65.9), command.getLocation().getEasting());
		Assert.assertEquals(Length.feet(-86.8), command.getLocation().getVertical());
		Assert.assertEquals("Z7", command.getStationName());
		Assert.assertEquals(Length.feet(0.0), command.getLeft());
		Assert.assertEquals(Length.feet(3.0), command.getUp());
		Assert.assertEquals(Length.feet(1.0), command.getDown());
		Assert.assertEquals(Length.feet(2.0), command.getRight());
		Assert.assertEquals(Length.feet(21.8), command.getDistanceFromEntrance());
	}

	@Test
	public void testParseDrawSurveyCommandErrors() throws SegmentParseException {
		CompassPlotParser parser = new CompassPlotParser();
		String line = "D   128.2   -65.9   -86.8  SZ7  P    a0.0    b3.0    .    %2.0  I   21.8";
		DrawSurveyCommand command = parser.parseDrawSurveyCommand(parser(line));
		Assert.assertEquals(Length.feet(128.2), command.getLocation().getNorthing());
		Assert.assertEquals(Length.feet(-65.9), command.getLocation().getEasting());
		Assert.assertEquals(Length.feet(-86.8), command.getLocation().getVertical());
		Assert.assertEquals("Z7", command.getStationName());
		Assert.assertNull(command.getLeft());
		Assert.assertNull(command.getRight());
		Assert.assertNull(command.getUp());
		Assert.assertNull(command.getDown());
		Assert.assertEquals(Length.feet(21.8), command.getDistanceFromEntrance());

		List<CompassParseError> errors = parser.getErrors();
		Assert.assertEquals(line.indexOf("a0.0"), errors.get(0).getSegment().startCol);
		Assert.assertEquals(line.indexOf("b3.0"), errors.get(1).getSegment().startCol);
		Assert.assertEquals(line.indexOf(". "), errors.get(2).getSegment().startCol);
		Assert.assertEquals(line.indexOf("%2.0"), errors.get(3).getSegment().startCol);
	}

	@Test
	public void testParseFeatureCommand() throws SegmentParseException {
		String line = "L   128.2   -65.9   -86.8  SZ7  P    0.0    3.0    1.0    2.0  V   21.8";
		FeatureCommand command = parseFeatureCommand(line);
		Assert.assertEquals(Length.feet(128.2), command.getLocation().getNorthing());
		Assert.assertEquals(Length.feet(-65.9), command.getLocation().getEasting());
		Assert.assertEquals(Length.feet(-86.8), command.getLocation().getVertical());
		Assert.assertEquals("Z7", command.getStationName());
		Assert.assertEquals(Length.feet(0.0), command.getLeft());
		Assert.assertEquals(Length.feet(3.0), command.getRight());
		Assert.assertEquals(Length.feet(1.0), command.getUp());
		Assert.assertEquals(Length.feet(2.0), command.getDown());
		Assert.assertEquals(new BigDecimal("21.8"), command.getValue());
	}

	@Test
	public void testParseFeatureCommandErrors() throws SegmentParseException {
		CompassPlotParser parser = new CompassPlotParser();
		String line = "L   128.2   -65.9   -86.8  SZ7  P    a0.0    b3.0    .    %2.0  V   21.8";
		FeatureCommand command = parser.parseFeatureCommand(parser(line));
		Assert.assertEquals(Length.feet(128.2), command.getLocation().getNorthing());
		Assert.assertEquals(Length.feet(-65.9), command.getLocation().getEasting());
		Assert.assertEquals(Length.feet(-86.8), command.getLocation().getVertical());
		Assert.assertEquals("Z7", command.getStationName());
		Assert.assertNull(command.getLeft());
		Assert.assertNull(command.getRight());
		Assert.assertNull(command.getUp());
		Assert.assertNull(command.getDown());
		Assert.assertEquals(new BigDecimal("21.8"), command.getValue());

		List<CompassParseError> errors = parser.getErrors();
		Assert.assertEquals(line.indexOf("a0.0"), errors.get(0).getSegment().startCol);
		Assert.assertEquals(line.indexOf("b3.0"), errors.get(1).getSegment().startCol);
		Assert.assertEquals(line.indexOf(". "), errors.get(2).getSegment().startCol);
		Assert.assertEquals(line.indexOf("%2.0"), errors.get(3).getSegment().startCol);
	}
	
	@Test
	public void testBlankLines() throws IOException {
		CompassPlotParser parser = new CompassPlotParser();
		parser.parsePlot(getClass().getResourceAsStream("blankLines.plt"), "blankLines.plt");
		Assert.assertEquals(0, parser.getErrors().size());
	}

	@Test
	public void testFullParse() throws IOException {
		CompassPlotParser parser = new CompassPlotParser();
		parser.parsePlot(getClass().getResourceAsStream("testplot.plt"), "testplot.plt");
		Assert.assertEquals(0, parser.getErrors().size());
		for (CompassPlotCommand command : parser.getCommands()) {
			System.out.println(command);
		}
	}
}
