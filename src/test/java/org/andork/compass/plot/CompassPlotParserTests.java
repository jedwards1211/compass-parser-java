package org.andork.compass.plot;

import java.util.List;
import java.util.concurrent.Callable;

import org.andork.compass.CompassParseError;
import org.andork.compass.ExceptionRunnable;
import org.andork.compass.LineParser;
import org.andork.segment.Segment;
import org.junit.Assert;
import org.junit.Test;

public class CompassPlotParserTests {
	private static void assertThrowsParseError(Callable<?> c, int index, String message) {
		try {
			c.call();
			Assert.fail("expected function to throw a CompassParseError");
		} catch (Exception e) {
			CompassParseError error = (CompassParseError) e;
			Assert.assertEquals(index, error.getSegment().startCol);
			Assert.assertEquals(message, error.getMessage());
		}
	}

	private static void assertThrowsParseError(ExceptionRunnable r, int index, String message) {
		try {
			r.run();
			Assert.fail("expected function to throw a CompassParseError");
		} catch (Exception e) {
			CompassParseError error = (CompassParseError) e;
			Assert.assertEquals(index, error.getSegment().startCol);
			Assert.assertEquals(message, error.getMessage());
		}
	}

	private static DrawSurveyCommand parseDrawSurveyCommand(String command) throws CompassParseError {
		CompassPlotParser parser = new CompassPlotParser();
		return parser.parseDrawSurveyCommand(parser(command));
	}

	private static FeatureCommand parseFeatureCommand(String command) throws CompassParseError {
		CompassPlotParser parser = new CompassPlotParser();
		return parser.parseFeatureCommand(parser(command));
	}

	private static LineParser parser(String text) {
		return new LineParser(new Segment(text, "", 0, 0));
	}

	@Test
	public void testInvalidParseDrawSurveyCommand() {
		assertThrowsParseError(() -> parseDrawSurveyCommand("Q"), 0, "Invalid command: Q; expected D or M");
		assertThrowsParseError(() -> parseDrawSurveyCommand("D X"), 2, "invalid northing");
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
		assertThrowsParseError(() -> parseFeatureCommand("Q"), 0, "command should be L");
		assertThrowsParseError(() -> parseFeatureCommand("L X"), 2, "invalid northing");
		assertThrowsParseError(() -> parseFeatureCommand("L128.2"), 1, "missing whitespace before northing");
		assertThrowsParseError(() -> parseFeatureCommand("L 128.2-65.9"), 7, "missing whitespace before easting");
		assertThrowsParseError(() -> parseFeatureCommand("L 128.2 -65.9 -86.8 S "), 21, "missing station name");
		assertThrowsParseError(() -> parseFeatureCommand("L 128.2 -65.9 -86.8 SZ7 P0.0 "), 25,
				"missing whitespace before left");
		assertThrowsParseError(() -> parseFeatureCommand("L 128.2 -65.9 -86.8I 21.8"), 19,
				"missing whitespace before next command");
	}

	@Test
	public void testParseDrawSurveyCommand() throws CompassParseError {
		String line = "D   128.2   -65.9   -86.8  SZ7  P    0.0    3.0    1.0    2.0  I   21.8";
		DrawSurveyCommand command = parseDrawSurveyCommand(line);
		Assert.assertEquals(128.2, command.getLocation().getNorthing(), 0.0);
		Assert.assertEquals(-65.9, command.getLocation().getEasting(), 0.0);
		Assert.assertEquals(-86.8, command.getLocation().getVertical(), 0.0);
		Assert.assertEquals("Z7", command.getStationName());
		Assert.assertEquals(0.0, command.getLeft(), 0.0);
		Assert.assertEquals(3.0, command.getRight(), 0.0);
		Assert.assertEquals(1.0, command.getUp(), 0.0);
		Assert.assertEquals(2.0, command.getDown(), 0.0);
		Assert.assertEquals(21.8, command.getDistanceFromEntrance(), 0.0);
	}

	@Test
	public void testParseDrawSurveyCommandErrors() throws CompassParseError {
		CompassPlotParser parser = new CompassPlotParser();
		String line = "D   128.2   -65.9   -86.8  SZ7  P    a0.0    b3.0    .    %2.0  I   21.8";
		DrawSurveyCommand command = parser.parseDrawSurveyCommand(parser(line));
		Assert.assertEquals(128.2, command.getLocation().getNorthing(), 0.0);
		Assert.assertEquals(-65.9, command.getLocation().getEasting(), 0.0);
		Assert.assertEquals(-86.8, command.getLocation().getVertical(), 0.0);
		Assert.assertEquals("Z7", command.getStationName());
		Assert.assertTrue(Double.isNaN(command.getLeft()));
		Assert.assertTrue(Double.isNaN(command.getRight()));
		Assert.assertTrue(Double.isNaN(command.getUp()));
		Assert.assertTrue(Double.isNaN(command.getDown()));
		Assert.assertEquals(21.8, command.getDistanceFromEntrance(), 0.0);

		List<CompassParseError> errors = parser.getErrors();
		Assert.assertEquals(line.indexOf("a0.0"), errors.get(0).getSegment().startCol);
		Assert.assertEquals(line.indexOf("b3.0"), errors.get(1).getSegment().startCol);
		Assert.assertEquals(line.indexOf(". "), errors.get(2).getSegment().startCol);
		Assert.assertEquals(line.indexOf("%2.0"), errors.get(3).getSegment().startCol);
	}

	@Test
	public void testParseFeatureCommand() throws CompassParseError {
		String line = "L   128.2   -65.9   -86.8  SZ7  P    0.0    3.0    1.0    2.0  V   21.8";
		FeatureCommand command = parseFeatureCommand(line);
		Assert.assertEquals(128.2, command.getLocation().getNorthing(), 0.0);
		Assert.assertEquals(-65.9, command.getLocation().getEasting(), 0.0);
		Assert.assertEquals(-86.8, command.getLocation().getVertical(), 0.0);
		Assert.assertEquals("Z7", command.getStationName());
		Assert.assertEquals(0.0, command.getLeft(), 0.0);
		Assert.assertEquals(3.0, command.getRight(), 0.0);
		Assert.assertEquals(1.0, command.getUp(), 0.0);
		Assert.assertEquals(2.0, command.getDown(), 0.0);
		Assert.assertEquals(21.8, command.getValue(), 0.0);
	}

	@Test
	public void testParseFeatureCommandErrors() throws CompassParseError {
		CompassPlotParser parser = new CompassPlotParser();
		String line = "L   128.2   -65.9   -86.8  SZ7  P    a0.0    b3.0    .    %2.0  V   21.8";
		FeatureCommand command = parser.parseFeatureCommand(parser(line));
		Assert.assertEquals(128.2, command.getLocation().getNorthing(), 0.0);
		Assert.assertEquals(-65.9, command.getLocation().getEasting(), 0.0);
		Assert.assertEquals(-86.8, command.getLocation().getVertical(), 0.0);
		Assert.assertEquals("Z7", command.getStationName());
		Assert.assertTrue(Double.isNaN(command.getLeft()));
		Assert.assertTrue(Double.isNaN(command.getRight()));
		Assert.assertTrue(Double.isNaN(command.getUp()));
		Assert.assertTrue(Double.isNaN(command.getDown()));
		Assert.assertEquals(21.8, command.getValue(), 0.0);

		List<CompassParseError> errors = parser.getErrors();
		Assert.assertEquals(line.indexOf("a0.0"), errors.get(0).getSegment().startCol);
		Assert.assertEquals(line.indexOf("b3.0"), errors.get(1).getSegment().startCol);
		Assert.assertEquals(line.indexOf(". "), errors.get(2).getSegment().startCol);
		Assert.assertEquals(line.indexOf("%2.0"), errors.get(3).getSegment().startCol);
	}
}
