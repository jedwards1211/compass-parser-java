package org.andork.compass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.andork.compass.CompassParseError.Severity;

public class CompassParser {
	private static final Pattern NON_WHITESPACE = Pattern.compile("\\S+");
	private File file;
	private int line;
	private final List<CompassParseError> errors = new ArrayList<>();

	private CompassTripHeader tripHeader;

	public List<CompassParseError> getErrors() {
		return errors;
	}

	File getFile() {
		return file;
	}

	int getLine() {
		return line;
	}

	CompassTripHeader getTripHeader() {
		return tripHeader;
	}

	private double parseMeasurement(Matcher matcher, String fieldName) {
		if (!matcher.find()) {
			errors.add(new CompassParseError(
					Severity.ERROR,
					"missing " + fieldName,
					file,
					line,
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
					file,
					line,
					matcher.start(),
					matcher.end() - 1));
			return Double.NaN;
		}
		if (value < -900) {
			return Double.NaN;
		}
		return value;
	}

	CompassShot parseShot(String text) {
		final Matcher matcher = NON_WHITESPACE.matcher(text);

		final CompassShot shot = new CompassShot();
		shot.setFromStationName(parseString(matcher, "from station name"));
		shot.setToStationName(parseString(matcher, "to station name"));
		shot.setLength(parseMeasurement(matcher, "length"));
		shot.setFrontsightAzimuth(parseMeasurement(matcher, "frontsight azimuth"));
		shot.setFrontsightInclination(parseMeasurement(matcher, "frontsight inclination"));
		shot.setLeft(parseMeasurement(matcher, "left"));
		shot.setUp(parseMeasurement(matcher, "up"));
		shot.setDown(parseMeasurement(matcher, "down"));
		shot.setRight(parseMeasurement(matcher, "right"));
		if (tripHeader.isHasBacksights()) {
			shot.setBacksightAzimuth(parseMeasurement(matcher, "backsight azimuth"));
			shot.setBacksightInclination(parseMeasurement(matcher, "backsight inclination"));
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
							file,
							line,
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
								file,
								line,
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
					file,
					line,
					matcher.start(),
					matcher.end() - 1));
			return null;
		}
		return matcher.group();
	}

	void setFile(File file) {
		this.file = file;
	}

	void setLine(int line) {
		this.line = line;
	}

	void setTripHeader(CompassTripHeader tripHeader) {
		this.tripHeader = tripHeader;
	}
}
