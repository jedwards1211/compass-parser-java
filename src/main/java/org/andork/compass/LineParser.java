package org.andork.compass;

import java.util.regex.Pattern;

import org.andork.compass.CompassParseError.Severity;
import org.andork.segment.Segment;
import org.andork.segment.SegmentMatcher;

public class LineParser {
	private static final Pattern WHITESPACE = Pattern.compile("\\s+");
	private static final Pattern NONWHITESPACE = Pattern.compile("\\S+");
	private static final Pattern DOUBLE_LITERAL = Pattern.compile("-?(\\d+(\\.\\d*)?|\\.\\d+)([eE][+-]?\\d+)?");
	private final Segment segment;
	private int index = 0;

	public LineParser(Segment segment) {
		super();
		this.segment = segment;
	}

	public void advance(int amount) {
		index += amount;
		// check index
		segment.charAt(index);
	}

	public void advanceToWhitespace() {
		while (index < segment.length() && !Character.isWhitespace(segment.charAt(index))) {
			index++;
		}
	}

	public boolean atEnd() {
		return index >= segment.length();
	}

	public void character(char c, String errorMessage) throws CompassParseError {
		if (index >= segment.length() || segment.charAt(index) != c) {
			throw new CompassParseError(Severity.ERROR, errorMessage,
					segment.charAtAsSegment(index));
		}
		index++;
	}

	public char charAtIndex() {
		return segment.charAt(index);
	}

	public double doubleLiteral(String errorMessage) throws CompassParseError {
		Segment segment = pattern(DOUBLE_LITERAL, errorMessage).group();
		try {
			return Double.parseDouble(segment.toString());
		} catch (Exception ex) {
			throw new CompassParseError(Severity.ERROR, errorMessage, segment);
		}
	}

	public int getIndex() {
		return index;
	}

	public Segment getSegment() {
		return segment;
	}

	public Segment nonwhitespace(String errorMessage) throws CompassParseError {
		return pattern(NONWHITESPACE, errorMessage).group();
	}

	public SegmentMatcher pattern(Pattern p, String errorMessage) throws CompassParseError {
		SegmentMatcher m = new SegmentMatcher(segment, p);
		m.region(index, segment.length());
		if (!m.find() || m.start() > index) {
			throw new CompassParseError(Severity.ERROR, errorMessage, segment.charAtAsSegment(index));
		}
		index = m.end();
		return m;
	}

	public SegmentMatcher pattern(String pattern, String errorMessage) throws CompassParseError {
		return pattern(Pattern.compile(pattern), errorMessage);
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void throwError(String message) throws CompassParseError {
		throw new CompassParseError(Severity.ERROR, message, segment.charAtAsSegment(index));
	}

	public void whitespace(String errorMessage) throws CompassParseError {
		pattern(WHITESPACE, errorMessage);
	}
}
