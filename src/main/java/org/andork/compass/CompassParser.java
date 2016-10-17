package org.andork.compass;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.andork.compass.CompassParseError.Severity;

public class CompassParser {
	private static final Pattern NON_WHITESPACE = Pattern.compile("\\S+");
	private String source;
	private LineNumberReader reader;
	private final List<CompassParseError> errors = new ArrayList<>();

	private CompassTripHeader tripHeader;

	CompassParser(String source, String text) {
		this.source = source;
		reader = new LineNumberReader(new StringReader(text));
	}

	CompassParser(String source, String text, CompassTripHeader header) {
		this(source, text);
		tripHeader = header;
	}

	public List<CompassParseError> getErrors() {
		return errors;
	}

	int getLine() {
		return reader.getLineNumber() - 1;
	}

	String getSource() {
		return source;
	}

	CompassTripHeader getTripHeader() {
		return tripHeader;
	}

	private double parseAzimuth(Matcher matcher, String fieldName) {
		double measurement = parseMeasurement(matcher, fieldName);
		if (measurement < 0 || measurement >= 360) {
			errors.add(new CompassParseError(
					Severity.ERROR,
					fieldName + " must be >= 0 and < 360",
					source,
					reader.getLineNumber() - 1,
					matcher.start(),
					matcher.end() - 1));
		}
		return measurement;
	}

	private double parseMeasurement(Matcher matcher, String fieldName) {
		if (!matcher.find()) {
			errors.add(new CompassParseError(
					Severity.ERROR,
					"missing " + fieldName,
					source,
					reader.getLineNumber() - 1,
					matcher.regionEnd()));
			return Double.NaN;
		}
		double value;
		try {
			value = Double.parseDouble(matcher.group());
		} catch (NumberFormatException ex) {
			errors.add(new CompassParseError(
					Severity.ERROR,
					"missing " + fieldName,
					source,
					reader.getLineNumber() - 1,
					matcher.start(),
					matcher.end() - 1));
			return Double.NaN;
		}
		if (value < -900) {
			return Double.NaN;
		}
		return value;
	}

	private double parseMeasurement(Matcher matcher, String fieldName, double min) {
		double measurement = parseMeasurement(matcher, fieldName);
		if (measurement < min) {
			errors.add(new CompassParseError(
					Severity.ERROR,
					fieldName + " must be >= " + min,
					source,
					reader.getLineNumber() - 1,
					matcher.start(),
					matcher.end() - 1));
		}
		return measurement;
	}

	private double parseMeasurement(Matcher matcher, String fieldName, double min, double max) {
		double measurement = parseMeasurement(matcher, fieldName);
		if (measurement < min || measurement > max) {
			errors.add(new CompassParseError(
					Severity.ERROR,
					fieldName + " must be between " + min + " and " + max,
					source,
					reader.getLineNumber() - 1,
					matcher.start(),
					matcher.end() - 1));
		}
		return measurement;
	}

	CompassShot parseShot() throws IOException {
		String text = reader.readLine();
		final Matcher matcher = NON_WHITESPACE.matcher(text);

		final CompassShot shot = new CompassShot();
		shot.setFromStationName(parseString(matcher, "from station name"));
		shot.setToStationName(parseString(matcher, "to station name"));
		shot.setLength(parseMeasurement(matcher, "length", 0));
		shot.setFrontsightAzimuth(parseAzimuth(matcher, "frontsight azimuth"));
		shot.setFrontsightInclination(parseMeasurement(matcher, "frontsight inclination", -90, 90));
		shot.setLeft(parseMeasurement(matcher, "left", 0));
		shot.setUp(parseMeasurement(matcher, "up", 0));
		shot.setDown(parseMeasurement(matcher, "down", 0));
		shot.setRight(parseMeasurement(matcher, "right", 0));
		if (tripHeader.isHasBacksights()) {
			shot.setBacksightAzimuth(parseAzimuth(matcher, "backsight azimuth"));
			shot.setBacksightInclination(parseMeasurement(matcher, "backsight inclination", -90, 90));
		}
		int commentStart = matcher.end();
		if (matcher.find()) {
			if (matcher.group().startsWith("#|")) {
				final String flags = matcher.group();
				commentStart = matcher.end();
				if (!flags.endsWith("#")) {
					errors.add(new CompassParseError(
							Severity.WARNING,
							"missing # after flags",
							source,
							reader.getLineNumber() - 1,
							matcher.end()));
				}
				for (int i = 2; i < flags.length() - 1; i++) {
					final char flag = flags.charAt(i);
					switch (flag) {
					case 'l':
					case 'L':
						shot.setExcludeFromLength(true);
						break;
					case 'p':
					case 'P':
						shot.setExcludeFromPlotting(true);
						break;
					case 'x':
					case 'X':
						shot.setExcludeFromAllProcessing(true);
						break;
					case 'c':
					case 'C':
						shot.setDoNotAdjust(true);
						break;
					default:
						errors.add(new CompassParseError(
								Severity.WARNING,
								"unrecognized flag: " + flag,
								source,
								reader.getLineNumber() - 1,
								matcher.start() + i));
						break;
					}
				}
			}
		}
		shot.setComment(text.substring(commentStart).trim());
		return shot;
	}

	private String parseString(Matcher matcher, String fieldName) {
		if (!matcher.find()) {
			errors.add(new CompassParseError(
					CompassParseError.Severity.ERROR,
					"missing " + fieldName,
					source,
					reader.getLineNumber() - 1,
					matcher.start(),
					matcher.end() - 1));
			return null;
		}
		return matcher.group();
	}

	void setSource(String source) {
		this.source = source;
	}

	void setTripHeader(CompassTripHeader tripHeader) {
		this.tripHeader = tripHeader;
	}

}
