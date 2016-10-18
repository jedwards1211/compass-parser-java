package org.andork.compass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.andork.compass.CompassParseError.Severity;

public class CompassParser {
	private static final Pattern EOL = Pattern.compile("\r|\n|\r\n");
	private static final Pattern NON_WHITESPACE = Pattern.compile("\\S+");
	private static final Pattern HEADER_FIELDS = Pattern
			.compile("SURVEY (NAME|DATE|TEAM):|COMMENT:|DECLINATION:|FORMAT:|CORRECTIONS2?:");

	static Segment[] splitHeaderAndData(Segment segment) {
		SegmentMatcher matcher = new SegmentMatcher(segment, EOL);
		int i = 0;
		int headerEnd = 0;
		while (i < 8 && matcher.find()) {
			i++;
			headerEnd = matcher.end();
		}
		return new Segment[] { segment.substring(0, headerEnd).trim(), segment.substring(headerEnd).trim() };
	}

	private final List<CompassParseError> errors = new ArrayList<>();

	public CompassParser() {

	}

	public boolean checkText(Segment segment, String text) {
		if (!segment.startsWith(text)) {
			errors.add(new CompassParseError(
					CompassParseError.Severity.ERROR,
					"expected " + text + " here",
					segment.substring(0, Math.min(text.length(), segment.length()))));
			return false;
		}
		return true;
	}

	public List<CompassParseError> getErrors() {
		return Collections.unmodifiableList(errors);
	}

	private void getFields(SegmentMatcher matcher, BiConsumer<String, Segment> iteratee) {
		if (!matcher.find()) {
			return;
		}
		String lastMatch = matcher.group().toString();
		int lastEnd = matcher.end();
		while (matcher.find()) {
			iteratee.accept(lastMatch, matcher.segment().substring(lastEnd, matcher.start()).trim());

			lastMatch = matcher.group().toString();
			lastEnd = matcher.end();
		}
		iteratee.accept(lastMatch, matcher.segment().substring(lastEnd));
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

	private AzimuthUnit parseAzimuthUnit(Segment unit) {
		switch (unit.charAt(0)) {
		case 'D':
			return AzimuthUnit.DEGREES;
		case 'Q':
			return AzimuthUnit.QUADS;
		case 'R':
			return AzimuthUnit.GRADS;
		default:
			errors.add(new CompassParseError(
					CompassParseError.Severity.ERROR,
					"unrecognized azimuth unit: " + unit.charAt(0),
					unit));
			return AzimuthUnit.DEGREES;
		}
	}

	public List<CompassTrip> parseCompassSurveyData(Segment segment) {
		List<CompassTrip> trips = new ArrayList<CompassTrip>();
		for (Segment tripText : segment.split("\f")) {
			CompassTrip trip = parseTrip(tripText.trim());
			if (trip != null) {
				trips.add(trip);
			}
		}
		return trips;
	}

	@SuppressWarnings("deprecation")
	private Date parseDate(Segment segment) {
		SegmentMatcher dateMatcher = new SegmentMatcher(segment, NON_WHITESPACE);
		Integer month = parseInt(dateMatcher, "month");
		Segment monthGroup = month != null ? dateMatcher.group() : null;
		Integer day = parseInt(dateMatcher, "day");
		Segment dayGroup = day != null ? dateMatcher.group() : null;
		Integer year = parseInt(dateMatcher, "year");
		Segment yearGroup = year != null ? dateMatcher.group() : null;
		if (month == null || day == null || year == null) {
			return null;
		}
		if (month < 1 || month > 12) {
			errors.add(new CompassParseError(
					CompassParseError.Severity.ERROR,
					"month must be between 1 and 12",
					monthGroup));
			return null;
		}
		if (day < 1 || day > 31) {
			errors.add(new CompassParseError(
					CompassParseError.Severity.ERROR,
					"day must be between 1 and 31",
					dayGroup));
			return null;
		}
		if (year < 0) {
			errors.add(new CompassParseError(
					CompassParseError.Severity.ERROR,
					"year must be >= 0",
					yearGroup));
		}

		return new Date(year >= 100 ? year - 1900 : year, month - 1, day);
	}

	private void parseFormat(CompassTripHeader header, Segment format) {
		int i = 0;
		if (format.length() < 11) {
			errors.add(new CompassParseError(
					CompassParseError.Severity.ERROR,
					"format must be at least 11 characters long",
					format.substring(format.length())));
			return;
		}
		header.setAzimuthUnit(parseAzimuthUnit(format.charAtAsSegment(i++)));
		header.setLengthUnit(parseLengthUnit(format.charAtAsSegment(i++)));
		header.setLrudUnit(parseLengthUnit(format.charAtAsSegment(i++)));
		header.setInclinationUnit(parseInclinationUnit(format.charAtAsSegment(i++)));
		parseMeasurements(format.substring(i), header.getLrudOrder(), this::parseLrudMeasurement, "lrud measurement");
		i += 4;
		parseMeasurements(format.substring(i), header.getShotMeasurementOrder(), this::parseShotMeasurement,
				"shot measurement");
		i += 3;
		if (format.length() > i) {
			header.setHasBacksights(format.charAt(i++) == 'B');
		}
		if (format.length() > i) {
			header.setLrudAssociation(parseLrudAssociation(format.charAtAsSegment(i++)));
		}
	}

	private InclinationUnit parseInclinationUnit(Segment unit) {
		switch (unit.charAt(0)) {
		case 'D':
			return InclinationUnit.DEGREES;
		case 'G':
			return InclinationUnit.PERCENT_GRADE;
		case 'M':
			return InclinationUnit.DEGREES_AND_MINUTES;
		case 'R':
			return InclinationUnit.GRADS;
		case 'W':
			return InclinationUnit.DEPTH_GAUGE;
		default:
			errors.add(new CompassParseError(
					CompassParseError.Severity.ERROR,
					"unrecognized inclination unit: " + unit.charAt(0),
					unit));
			return InclinationUnit.DEGREES;
		}
	}

	private Integer parseInt(SegmentMatcher matcher, String fieldName) {
		if (!matcher.find()) {
			errors.add(new CompassParseError(
					CompassParseError.Severity.ERROR,
					"missing " + fieldName,
					matcher.group()));
			return null;
		}
		try {
			return Integer.parseInt(matcher.group().toString());
		} catch (NumberFormatException ex) {
			errors.add(new CompassParseError(
					CompassParseError.Severity.ERROR,
					"invalid " + fieldName,
					matcher.group()));
			return null;
		}
	}

	private LengthUnit parseLengthUnit(Segment unit) {
		switch (unit.charAt(0)) {
		case 'D':
			return LengthUnit.DECIMAL_FEET;
		case 'I':
			return LengthUnit.FEET_AND_INCHES;
		case 'M':
			return LengthUnit.METERS;
		default:
			errors.add(new CompassParseError(
					CompassParseError.Severity.ERROR,
					"unrecognized distance unit: " + unit.charAt(0),
					unit));
			return LengthUnit.DECIMAL_FEET;
		}
	}

	private LrudAssociation parseLrudAssociation(Segment segment) {
		switch (segment.charAt(0)) {
		case 'F':
			return LrudAssociation.FROM;
		case 'T':
			return LrudAssociation.TO;
		default:
			errors.add(new CompassParseError(
					CompassParseError.Severity.ERROR,
					"unrecognized station side: " + segment.charAt(0),
					segment));
			return null;
		}
	}

	private LrudMeasurement parseLrudMeasurement(Segment segment) {
		switch (segment.charAt(0)) {
		case 'L':
			return LrudMeasurement.LEFT;
		case 'R':
			return LrudMeasurement.RIGHT;
		case 'U':
			return LrudMeasurement.UP;
		case 'D':
			return LrudMeasurement.DOWN;
		default:
			errors.add(new CompassParseError(
					CompassParseError.Severity.ERROR,
					"unrecognized passage dimension measurement: " + segment.charAt(0),
					segment));
			return null;
		}
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

	private <T> void parseMeasurements(Segment segment, T[] measurements, Function<Segment, T> parser,
			String measurementName) {
		for (int i = 0; i < measurements.length; i++) {
			if (segment.length() <= i) {
				errors.add(new CompassParseError(
						Severity.ERROR,
						"missing " + measurementName,
						segment.substring(segment.length())));
			}
			measurements[i] = parser.apply(segment.charAtAsSegment(i));
		}
	}

	public CompassShot parseShot(Segment segment, CompassTripHeader tripHeader) {
		final SegmentMatcher matcher = new SegmentMatcher(segment, NON_WHITESPACE);

		final CompassShot shot = new CompassShot();
		shot.setFromStationName(parseString(matcher, "from station name"));
		shot.setToStationName(parseString(matcher, "to station name"));
		if (shot.getFromStationName() == null && shot.getToStationName() == null) {
			return null;
		}

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

	private ShotMeasurement parseShotMeasurement(Segment segment) {
		switch (segment.charAt(0)) {
		case 'L':
			return ShotMeasurement.LENGTH;
		case 'A':
			return ShotMeasurement.AZIMUTH;
		case 'D':
			return ShotMeasurement.INCLINATION;
		default:
			errors.add(new CompassParseError(
					CompassParseError.Severity.ERROR,
					"unrecognized shot measurement: " + segment.charAt(0),
					segment));
			return null;
		}
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

	public CompassTrip parseTrip(Segment segment) {
		final Segment[] parts = splitHeaderAndData(segment);
		CompassTrip trip = new CompassTrip();
		CompassTripHeader header = parseTripHeader(parts[0]);
		trip.setHeader(header);

		List<CompassShot> shots = new ArrayList<CompassShot>();

		final Segment[] data = parts[1].split(EOL);
		for (Segment line : data) {
			CompassShot shot = parseShot(line, header);
			if (shot != null) {
				shots.add(shot);
			}
		}
		trip.setShots(shots);
		return trip;
	}

	public CompassTripHeader parseTripHeader(Segment segment) {
		final CompassTripHeader header = new CompassTripHeader();
		final Segment[] parts = segment.trim().split("\r|\n|\r\n", 2);
		header.setCaveName(parts[0].toString());
		getFields(new SegmentMatcher(parts[1], HEADER_FIELDS), (field, value) -> {
			if (field.equals("SURVEY NAME:")) {
				header.setSurveyName(parseString(new SegmentMatcher(value, NON_WHITESPACE), "survey name"));
			} else if (field.equals("SURVEY DATE:")) {
				header.setDate(parseDate(value));
			} else if (field.equals("COMMENT:")) {
				header.setComment(value.toString());
			} else if (field.equals("SURVEY TEAM:")) {
				header.setTeam(value.toString());
			} else if (field.equals("DECLINATION:")) {
				header.setDeclination(
						parseMeasurement(new SegmentMatcher(value, NON_WHITESPACE), "declination"));
			} else if (field.equals("FORMAT:")) {
				parseFormat(header, value);
			} else if (field.equals("CORRECTIONS:")) {
				SegmentMatcher matcher = new SegmentMatcher(value, NON_WHITESPACE);
				header.setLengthCorrection(parseMeasurement(matcher, "length correction"));
				header.setFrontsightAzimuthCorrection(parseMeasurement(matcher, "frontsight azimuth correction"));
				header.setFrontsightInclinationCorrection(
						parseMeasurement(matcher, "frontsight inclination correction"));
			} else if (field.equals("CORRECTIONS2:")) {
				SegmentMatcher matcher = new SegmentMatcher(value, NON_WHITESPACE);
				header.setBacksightAzimuthCorrection(parseMeasurement(matcher, "backsight azimuth correction"));
				header.setBacksightInclinationCorrection(parseMeasurement(matcher, "backsight inclination correction"));
			}
		});
		return header;
	}
}
