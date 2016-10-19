package org.andork.compass;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.andork.compass.CompassParseError.Severity;
import org.andork.segment.Segment;
import org.andork.segment.SegmentMatcher;

public class CompassParser {
	private static final Pattern EOL = Pattern.compile("\r\n|\r|\n");
	private static final Pattern COLUMN_HEADER = Pattern.compile("^\\s*FROM\\s+TO[^\r\n]+(\r\n|\r|\n){2}",
			Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
	private static final Pattern NON_WHITESPACE = Pattern.compile("\\S+");
	private static final Pattern HEADER_FIELDS = Pattern.compile(
			"SURVEY (NAME|DATE|TEAM):|COMMENT:|DECLINATION:|FORMAT:|CORRECTIONS2?:",
			Pattern.CASE_INSENSITIVE);

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Usage: java " + CompassParser.class.getName() + " <compass file> [<compass files...>]");
			return;
		}

		final CompassParser parser = new CompassParser();
		try {
			for (String file : args) {
				final byte[] bytes = Files.readAllBytes(Paths.get(file));
				final Segment segment = new Segment(new String(bytes), file, 0, 0);
				parser.parseCompassSurveyData(segment);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			System.exit(1);
		}

		if (parser.getErrors().isEmpty()) {
			System.out.println("No errors!");
			return;
		}
		for (CompassParseError error : parser.getErrors()) {
			System.out.println(error);
		}
		System.exit(1);
	}

	static Segment[] splitHeaderAndData(Segment segment) {
		SegmentMatcher matcher = new SegmentMatcher(segment, COLUMN_HEADER);
		int headerEnd = matcher.find() ? matcher.end() : segment.length();
		return new Segment[] { segment.substring(0, headerEnd).trim(), segment.substring(headerEnd).trim() };
	}

	private final List<CompassParseError> errors = new ArrayList<>();

	public CompassParser() {

	}

	private void addError(String message, Segment segment) {
		errors.add(new CompassParseError(Severity.ERROR, message, segment));
	}

	private void addWarning(String message, Segment segment) {
		errors.add(new CompassParseError(Severity.WARNING, message, segment));
	}

	public List<CompassParseError> getErrors() {
		return Collections.unmodifiableList(errors);
	}

	void getFields(SegmentMatcher matcher, BiConsumer<String, Segment> iteratee) {
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
		iteratee.accept(lastMatch, matcher.segment().substring(lastEnd).trim());
	}

	double parseAzimuth(SegmentMatcher matcher, String fieldName) {
		double measurement = parseMeasurement(matcher, fieldName);
		if (measurement < 0 || measurement >= 360) {
			addError(fieldName + " must be >= 0 and < 360", matcher.group());
		}
		return measurement;
	}

	AzimuthUnit parseAzimuthUnit(Segment unit) {
		switch (Character.toUpperCase(unit.charAt(0))) {
		case 'D':
			return AzimuthUnit.DEGREES;
		case 'Q':
			return AzimuthUnit.QUADS;
		case 'R':
			return AzimuthUnit.GRADS;
		default:
			addError("unrecognized azimuth unit: " + unit.charAt(0), unit);
			return AzimuthUnit.DEGREES;
		}
	}

	/**
	 * Parses the file at the given {@code path}.
	 */
	public List<CompassTrip> parseCompassSurveyData(Path path) throws IOException {
		byte[] bytes = Files.readAllBytes(path);
		return parseCompassSurveyData(new Segment(new String(bytes), path, 0, 0));
	}

	/**
	 * Parses the data in the given {@link Segment}.
	 */
	List<CompassTrip> parseCompassSurveyData(Segment segment) {
		List<CompassTrip> trips = new ArrayList<CompassTrip>();
		for (Segment text : segment.split(Pattern.compile("\f"))) {
			CompassTrip trip = parseTrip(text.trim());
			if (trip != null) {
				trips.add(trip);
			}
		}
		return trips;
	}

	/**
	 * Parses the given {@code data}.
	 *
	 * @param data
	 *            the data to parse
	 * @param source
	 *            If any errors or warnings are generated they will reference
	 *            this object. For instance you can pass a {@link File},
	 *            {@link Path}, or {@link URL}.
	 */
	public List<CompassTrip> parseCompassSurveyData(String data, Object source) {
		return parseCompassSurveyData(new Segment(data, source, 0, 0));
	}

	@SuppressWarnings("deprecation")
	Date parseDate(Segment segment) {
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
			addError("month must be between 1 and 12", monthGroup);
			return null;
		}
		if (day < 1 || day > 31) {
			addError("day must be between 1 and 31", dayGroup);
			return null;
		}
		if (year < 0) {
			addError("year must be >= 0", yearGroup);
		}

		return new Date(year >= 100 ? year - 1900 : year, month - 1, day);
	}

	InclinationUnit parseInclinationUnit(Segment unit) {
		switch (Character.toUpperCase(unit.charAt(0))) {
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
			addError("unrecognized inclination unit: " + unit.charAt(0), unit);
			return InclinationUnit.DEGREES;
		}
	}

	Integer parseInt(SegmentMatcher matcher, String fieldName) {
		if (!matcher.find()) {
			addError("missing " + fieldName, matcher.group());
			return null;
		}
		try {
			return Integer.parseInt(matcher.group().toString());
		} catch (NumberFormatException ex) {
			addError("invalid " + fieldName, matcher.group());
			return null;
		}
	}

	LengthUnit parseLengthUnit(Segment unit) {
		switch (Character.toUpperCase(unit.charAt(0))) {
		case 'D':
			return LengthUnit.DECIMAL_FEET;
		case 'I':
			return LengthUnit.FEET_AND_INCHES;
		case 'M':
			return LengthUnit.METERS;
		default:
			addError("unrecognized length unit: " + unit.charAt(0), unit);
			return LengthUnit.DECIMAL_FEET;
		}
	}

	LrudAssociation parseLrudAssociation(Segment segment) {
		switch (Character.toUpperCase(segment.charAt(0))) {
		case 'F':
			return LrudAssociation.FROM;
		case 'T':
			return LrudAssociation.TO;
		default:
			addError("unrecognized LRUD association: " + segment.charAt(0), segment);
			return null;
		}
	}

	LrudItem parseLrudItem(Segment segment) {
		switch (Character.toUpperCase(segment.charAt(0))) {
		case 'L':
			return LrudItem.LEFT;
		case 'R':
			return LrudItem.RIGHT;
		case 'U':
			return LrudItem.UP;
		case 'D':
			return LrudItem.DOWN;
		default:
			addError("unrecognized LRUD item: " + segment.charAt(0), segment);
			return null;
		}
	}

	double parseLrudMeasurement(SegmentMatcher matcher, String fieldName) {
		if (!matcher.find()) {
			addError("missing " + fieldName, matcher.group().substring(matcher.regionEnd()));
			return Double.NaN;
		}
		double value;
		try {
			value = Double.parseDouble(matcher.group().toString());
		} catch (NumberFormatException ex) {
			addError("missing " + fieldName, matcher.group());
			return Double.NaN;
		}
		if (value < -1) {
			return Double.NaN;
		}
		// Compass barfs on LRUDs between -1 and 0
		if (value < 0) {
			addError(fieldName + " must be >= 0.0", matcher.group());
		}
		return value;
	}

	double parseMeasurement(SegmentMatcher matcher, String fieldName) {
		if (!matcher.find()) {
			addError("missing " + fieldName, matcher.segment().substring(matcher.regionEnd()));
			return Double.NaN;
		}
		double value;
		try {
			value = Double.parseDouble(matcher.group().toString());
		} catch (NumberFormatException ex) {
			addError("missing " + fieldName, matcher.group());
			return Double.NaN;
		}
		if (value <= -900) {
			return Double.NaN;
		}
		return value;
	}

	double parseMeasurement(SegmentMatcher matcher, String fieldName, double min) {
		double measurement = parseMeasurement(matcher, fieldName);
		if (measurement < min) {
			addError(fieldName + " must be >= " + min, matcher.group());
		}
		return measurement;
	}

	double parseMeasurement(SegmentMatcher matcher, String fieldName, double min, double max) {
		double measurement = parseMeasurement(matcher, fieldName);
		if (measurement < min || measurement > max) {
			addError(fieldName + " must be between " + min + " and " + max, matcher.group());
		}
		return measurement;
	}

	<T> void parseOrder(Segment segment, T[] order, Function<Segment, T> parser,
			String itemName) {
		for (int i = 0; i < order.length; i++) {
			if (segment.length() <= i) {
				addError("missing " + itemName, segment.substring(segment.length()));
			}
			order[i] = parser.apply(segment.charAtAsSegment(i));
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
		shot.setLeft(parseLrudMeasurement(matcher, "left"));
		shot.setUp(parseLrudMeasurement(matcher, "up"));
		shot.setDown(parseLrudMeasurement(matcher, "down"));
		shot.setRight(parseLrudMeasurement(matcher, "right"));
		if (tripHeader.hasBacksights()) {
			shot.setBacksightAzimuth(parseAzimuth(matcher, "backsight azimuth"));
			shot.setBacksightInclination(parseMeasurement(matcher, "backsight inclination", -90, 90));
		}
		int commentStart = matcher.end();
		if (matcher.find()) {
			if (matcher.group().startsWith("#|")) {
				final Segment flags = matcher.group();
				commentStart = matcher.end();
				if (!flags.endsWith("#")) {
					addError("missing # after flags", flags.substring(flags.length()));
				}
				for (int i = 2; i < flags.length() - 1; i++) {
					final char flag = flags.charAt(i);
					switch (Character.toUpperCase(flag)) {
					case 'L':
						shot.setExcludedFromLength(true);
						break;
					case 'P':
						shot.setExcludedFromPlotting(true);
						break;
					case 'X':
						shot.setExcludedFromAllProcessing(true);
						break;
					case 'C':
						shot.setDoNotAdjust(true);
						break;
					default:
						addWarning("unrecognized flag: " + flag, flags.charAtAsSegment(i));
						break;
					}
				}
			}
		}
		shot.setComment(segment.substring(commentStart).toString().trim());
		return shot;
	}

	void parseShotFormat(CompassTripHeader header, Segment format) {
		int i = 0;
		if (format.length() < 11) {
			addError("format must be at least 11 characters long", format.substring(format.length()));
			return;
		}
		header.setAzimuthUnit(parseAzimuthUnit(format.charAtAsSegment(i++)));
		header.setLengthUnit(parseLengthUnit(format.charAtAsSegment(i++)));
		header.setLrudUnit(parseLengthUnit(format.charAtAsSegment(i++)));
		header.setInclinationUnit(parseInclinationUnit(format.charAtAsSegment(i++)));
		parseOrder(format.substring(i), header.getLrudOrder(), this::parseLrudItem, "LRUD item");
		i += 4;
		parseOrder(format.substring(i), header.getShotMeasurementOrder(), this::parseShotItem,
				"shot item");
		i += 3;
		if (format.length() > i) {
			header.setHasBacksights(format.charAt(i++) == 'B');
		}
		if (format.length() > i) {
			header.setLrudAssociation(parseLrudAssociation(format.charAtAsSegment(i++)));
		}
	}

	ShotItem parseShotItem(Segment segment) {
		switch (Character.toUpperCase(segment.charAt(0))) {
		case 'L':
			return ShotItem.LENGTH;
		case 'A':
			return ShotItem.AZIMUTH;
		case 'D':
			return ShotItem.INCLINATION;
		default:
			addError("unrecognized shot item: " + segment.charAt(0), segment);
			return null;
		}
	}

	String parseString(SegmentMatcher matcher, String fieldName) {
		if (!matcher.find()) {
			addError("missing " + fieldName, matcher.segment().substring(matcher.segment().length()));
			return null;
		}
		return matcher.group().toString();
	}

	public CompassTrip parseTrip(Segment segment) {
		final Segment[] parts = splitHeaderAndData(segment);
		CompassTrip trip = new CompassTrip();
		CompassTripHeader header = parseTripHeader(parts[0]);
		if (header == null) {
			return null;
		}
		trip.setHeader(header);

		List<CompassShot> shots = new ArrayList<CompassShot>();

		if (!parts[1].isEmpty()) {
			final Segment[] data = parts[1].split(EOL);
			for (Segment line : data) {
				CompassShot shot = parseShot(line, header);
				if (shot != null) {
					shots.add(shot);
				}
			}
		}
		trip.setShots(shots);
		return trip;
	}

	public CompassTripHeader parseTripHeader(Segment segment) {
		final CompassTripHeader header = new CompassTripHeader();
		final Segment[] parts = segment.trim().split(EOL, 2);
		if (parts.length < 2) {
			return null;
		}
		header.setCaveName(parts[0].toString());
		getFields(new SegmentMatcher(parts[1], HEADER_FIELDS), (field, value) -> {
			if (field.equalsIgnoreCase("SURVEY NAME:")) {
				header.setSurveyName(parseString(new SegmentMatcher(value, NON_WHITESPACE), "survey name"));
			} else if (field.equalsIgnoreCase("SURVEY DATE:")) {
				header.setDate(parseDate(value));
			} else if (field.equalsIgnoreCase("COMMENT:")) {
				header.setComment(value.toString());
			} else if (field.equalsIgnoreCase("SURVEY TEAM:")) {
				header.setTeam(value.toString());
			} else if (field.equalsIgnoreCase("DECLINATION:")) {
				header.setDeclination(
						parseMeasurement(new SegmentMatcher(value, NON_WHITESPACE), "declination"));
			} else if (field.equalsIgnoreCase("FORMAT:")) {
				parseShotFormat(header, value);
			} else if (field.equalsIgnoreCase("CORRECTIONS:")) {
				SegmentMatcher matcher = new SegmentMatcher(value, NON_WHITESPACE);
				header.setLengthCorrection(parseMeasurement(matcher, "length correction"));
				header.setFrontsightAzimuthCorrection(parseMeasurement(matcher, "frontsight azimuth correction"));
				header.setFrontsightInclinationCorrection(
						parseMeasurement(matcher, "frontsight inclination correction"));
			} else if (field.equalsIgnoreCase("CORRECTIONS2:")) {
				SegmentMatcher matcher = new SegmentMatcher(value, NON_WHITESPACE);
				header.setBacksightAzimuthCorrection(parseMeasurement(matcher, "backsight azimuth correction"));
				header.setBacksightInclinationCorrection(parseMeasurement(matcher, "backsight inclination correction"));
			}
		});
		return header;
	}
}
