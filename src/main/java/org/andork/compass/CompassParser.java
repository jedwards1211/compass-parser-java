package org.andork.compass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.andork.compass.CompassParseError.Severity;

public class CompassParser {
	private static final Pattern NON_WHITESPACE = Pattern.compile("\\S+");
	private final List<CompassParseError> errors = new ArrayList<>();

	public CompassParser() {

	}

	public List<CompassParseError> getErrors() {
		return Collections.unmodifiableList(errors);
	}

	private double parseAzimuth(SegmentMatcher matcher, String fieldName) {
		double measurement = parseMeasurement(matcher, fieldName);
		if (measurement < 0 || measurement >= 360) {
			errors.add(new CompassParseError(
					Severity.ERROR,
					fieldName + " must be >= 0 and < 360",
					matcher.group()));
		}
		return measurement;
	}

	private double parseMeasurement(SegmentMatcher matcher, String fieldName) {
		if (!matcher.find()) {
			errors.add(new CompassParseError(
					Severity.ERROR,
					"missing " + fieldName,
					matcher.group().substring(matcher.regionEnd())));
			return Double.NaN;
		}
		double value;
		try {
			value = Double.parseDouble(matcher.group().toString());
		} catch (NumberFormatException ex) {
			errors.add(new CompassParseError(
					Severity.ERROR,
					"missing " + fieldName,
					matcher.group()));
			return Double.NaN;
		}
		if (value < -900) {
			return Double.NaN;
		}
		return value;
	}

	private double parseMeasurement(SegmentMatcher matcher, String fieldName, double min) {
		double measurement = parseMeasurement(matcher, fieldName);
		if (measurement < min) {
			errors.add(new CompassParseError(
					Severity.ERROR,
					fieldName + " must be >= " + min,
					matcher.group()));
		}
		return measurement;
	}

	private double parseMeasurement(SegmentMatcher matcher, String fieldName, double min, double max) {
		double measurement = parseMeasurement(matcher, fieldName);
		if (measurement < min || measurement > max) {
			errors.add(new CompassParseError(
					Severity.ERROR,
					fieldName + " must be between " + min + " and " + max,
					matcher.group()));
		}
		return measurement;
	}

	public CompassShot parseShot(Segment segment, CompassTripHeader tripHeader) {
		final SegmentMatcher matcher = new SegmentMatcher(segment, NON_WHITESPACE);

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
				final Segment flags = matcher.group();
				commentStart = matcher.end();
				if (!flags.endsWith("#")) {
					errors.add(new CompassParseError(
							Severity.WARNING,
							"missing # after flags",
							flags.substring(flags.length())));
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
								flags.charAtAsSegment(i)));
						break;
					}
				}
			}
		}
		shot.setComment(segment.substring(commentStart).toString().trim());
		return shot;
	}

	private String parseString(SegmentMatcher matcher, String fieldName) {
		if (!matcher.find()) {
			errors.add(new CompassParseError(
					CompassParseError.Severity.ERROR,
					"missing " + fieldName,
					matcher.group()));
			return null;
		}
		return matcher.group().toString();
	}
}
