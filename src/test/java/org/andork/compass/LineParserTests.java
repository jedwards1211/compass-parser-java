package org.andork.compass;

import java.math.BigDecimal;
import java.util.concurrent.Callable;

import org.andork.segment.Segment;
import org.junit.Assert;
import org.junit.Test;

public class LineParserTests {
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

	private static LineParser parser(String text) {
		return new LineParser(new Segment(text, "", 0, 0));
	}

	@Test
	public void testBigDecimal() throws CompassParseError {
		assertThrowsParseError(() -> parser(" 3.5").bigDecimal("test"), 0, "test");
		assertThrowsParseError(() -> parser("a3.5").bigDecimal("test"), 0, "test");
		assertThrowsParseError(() -> parser("e3.5").bigDecimal("test"), 0, "test");
		LineParser p = parser("3.5kj");
		Assert.assertEquals(new BigDecimal("3.5"), p.bigDecimal("test"));
		Assert.assertEquals(3, p.getIndex());
		p = parser("-3.5e4");
		Assert.assertEquals(new BigDecimal("-3.5e4"), p.bigDecimal("test"));
		Assert.assertEquals(p.getSegment().length(), p.getIndex());
		p = parser("-.5e-2");
		Assert.assertEquals(new BigDecimal("-.5e-2"), p.bigDecimal("test"));
		Assert.assertEquals(p.getSegment().length(), p.getIndex());
		p = parser("-5");
		Assert.assertEquals(new BigDecimal("-5"), p.bigDecimal("test"));
		Assert.assertEquals(p.getSegment().length(), p.getIndex());
	}

	@Test
	public void testCharacter() throws CompassParseError {
		LineParser p = parser("abcd");
		p.character('a', "test");
		assertThrowsParseError(() -> p.character('a', "test"), 1, "test");
		p.character('b', "test");
		assertThrowsParseError(() -> p.character('b', "test"), 2, "test");
	}

	@Test
	public void testEmptySegment() {
		LineParser p = new LineParser(new Segment("", "", 0, 0));
		assertThrowsParseError(() -> p.whitespace("test"), 0, "test");
		assertThrowsParseError(() -> p.nonwhitespace("test"), 0, "test");
		assertThrowsParseError(() -> p.bigDecimal("test"), 0, "test");
		assertThrowsParseError(() -> p.character('c', "test"), 0, "test");
	}

	@Test
	public void testEndOfSegment() {
		LineParser p = new LineParser(new Segment("hello", "", 0, 0));
		p.setIndex(p.getSegment().length());
		assertThrowsParseError(() -> p.whitespace("test"), 5, "test");
		assertThrowsParseError(() -> p.nonwhitespace("test"), 5, "test");
		assertThrowsParseError(() -> p.bigDecimal("test"), 5, "test");
		assertThrowsParseError(() -> p.character('c', "test"), 5, "test");
	}

	@Test
	public void testNonwhitespace() throws CompassParseError {
		LineParser p = parser("hello  ");
		p.nonwhitespace("test");
		Assert.assertEquals(5, p.getIndex());
		LineParser p2 = parser("   hello");
		assertThrowsParseError(() -> p2.nonwhitespace("test"), 0, "test");
	}

	@Test
	public void testWhitespace() throws CompassParseError {
		LineParser p = parser("   hello");
		p.whitespace("test");
		Assert.assertEquals(3, p.getIndex());
		LineParser p2 = parser("hello");
		assertThrowsParseError(() -> p2.whitespace("test"), 0, "test");
	}
}
