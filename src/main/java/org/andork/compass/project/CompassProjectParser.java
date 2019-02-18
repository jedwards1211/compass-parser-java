package org.andork.compass.project;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.andork.compass.NEVLocation;
import org.andork.segment.Segment;
import org.andork.segment.SegmentMatcher;
import org.andork.segment.SegmentParseException;
import org.andork.unit.Angle;
import org.andork.unit.Length;
import org.andork.unit.Unit;
import org.andork.unit.UnitizedDouble;

/**
 * .MAK file format notes
 * 
 * <pre><code>
 * @357715.700,4372837.600,3048.000,13,-1.050;
 * &North American 1983;
 * !ot;
 * 
 * /
 * 
 * $13;
 * &North American 1983;
 * #FULFORD.DAT,
 *  A1[f,1173607.995,14346579.967,10000.000],
 *  SC3[f,1173537.730,14346710.958,9938.648],
 *  S4[f,1173638.451,14346578.084,10020.013],
 *  SS6[f,1173818.570,14346406.496,10018.701];
 * 
 * /
 * 
 * #FULSURF.DAT;
 * </code></pre>
 * 
 * === Things I haven't figured out yet ===
 * <code>!ot;</code> and <code>/</code> lines are a mystery.
 * 
 * === Project Location line ===
 * Starts with @
 * All values in meters.
 * @<Easting>,<Northing>,<Elevation>,<UTM Zone>,<Convergence Angle>;
 * @357715.717,4372837.574,3048.000,13,-1.050;
 * 
 * This is accessible in Compass via Edit > Set Project Location...
 * 
 * === Datum line ===
 * Starts with &
 * &North American 1983;
 * 
 * === UTM Zone line ===
 * Starts with $
 * $13;
 * (UTM Zone for following surveys is 13)
 * 
 * === Data file line ===
 * Starts with #
 * May be broken up into multiple lines
 * #<Filename>,[Fixed Station ...];
 * 
 * Each fixed station is of the form
 * <Station Name>[<unit>,<Easting>,<Northing>,<Elevation>]
 * The unit can be f (feet) or m (meters)
 * 
 * #FULFORD.DAT,
 *  A1[f,1173607.995,14346579.967,10000.000],
 *  SC3[f,1173537.730,14346710.958,9938.648],
 *  S4[f,1173638.451,14346578.084,10020.013],
 *  SS6[f,1173818.570,14346406.496,10018.701];
 * 
 * This is accessible in Compass via Edit > Edit File Node...
 *   and going to the Links/Fixed Stations tab.
 * 
 */
public class CompassProjectParser {
	private static Pattern endOfLineRx = Pattern.compile("\\r\\n?|\\n");
	private static Pattern fileNameRx = Pattern.compile("[^,;/]+");
	private static Pattern datumRx = Pattern.compile("[^;/]+");
	private static Pattern linkStationRx = Pattern.compile("[^,;/\\[]+");
	private static Pattern numberRx = Pattern.compile("[-+]?\\d+(\\.\\d*)?|\\.\\d+");

	private final CompassProjectVisitor visitor;
	private int i;
	private Segment data;
	private SegmentMatcher endOfLineMatcher;
	private SegmentMatcher fileNameMatcher;
	private SegmentMatcher datumMatcher;
	private SegmentMatcher linkStationMatcher;
	private SegmentMatcher numberMatcher;

	public CompassProjectParser(CompassProjectVisitor visitor) {
		this.visitor = Objects.requireNonNull(visitor);
	}
	
	private void reset(Segment data) throws IOException {
		i = 0;
		this.data = data;
		endOfLineMatcher = new SegmentMatcher(data, endOfLineRx);
		fileNameMatcher = new SegmentMatcher(data, fileNameRx);
		datumMatcher = new SegmentMatcher(data, datumRx);
		linkStationMatcher = new SegmentMatcher(data, linkStationRx);
		numberMatcher = new SegmentMatcher(data, numberRx);
	}
	

	public void parse(Segment data) throws IOException, SegmentParseException {
		reset(data);
		parse();
	}
	
	public void parse(InputStream in, Object source) throws IOException, SegmentParseException {
		try (@SuppressWarnings("resource")
		Scanner s = new Scanner(in, "ASCII").useDelimiter("\\A")) {
			String result = s.hasNext() ? s.next() : "";
			reset(new Segment(result, source, 0, 0));
		}
		parse();
	}

	public void parse(Path projectFile) throws IOException, SegmentParseException {
		reset(Segment.readFile(projectFile, Charset.forName("ASCII")));
		parse();
	}

	private void parse() throws SegmentParseException {
		while (i < data.length()) {
			skipWhitespace();
			if (i >= data.length()) break;
			switch (data.charAt(i++)) {
			case '#':
				surveyFile();
				continue;
			case '@':
				location();
				continue;
			case '&':
				datum();
				continue;
			case '%':
				utmConvergence();
				continue;
			case '$':
				utmZone();
				continue;
			case '!':
				flags();
				continue;
			case '/':
				comment();
				continue;
			default:
				// ignore silly ASCII control characters, I've seen 0x1a (SUB) in compass files
				if (data.charAt(i - 1) < 0x20) {
					break;
				}
				throw new SegmentParseException("invalid directive", data.charAtAsSegment(i - 1));
			}
		}
	}
	
	private void skipWhitespace() {
		while (i < data.length() && Character.isWhitespace(data.charAt(i))) i++;
	}
	
	private void skipWhitespaceAndComments() {
		skipWhitespace();
		while (i < data.length() && '/' == data.charAt(i)) {
			i++;
			comment();
			skipWhitespace();
		}
	}
	
	private void nextLine() {
		i = endOfLineMatcher.region(i, data.length()).find()
			? endOfLineMatcher.end()
			: data.length();
	}
	
	private Segment expect(SegmentMatcher matcher, String missingErrorMessage) throws SegmentParseException {
		if (!matcher.region(i,  data.length()).find() || matcher.start() != i) {
			throw new SegmentParseException(missingErrorMessage, data.charAtAsSegment(i));
		}
		i = matcher.end();
		return matcher.group();
	}
	
	private void expect(char c) throws SegmentParseException {
		if (data.charAt(i++) != c) {
			throw new SegmentParseException("expected " + c, data.charAtAsSegment(i - 1));
		}
	}
	
	private Unit<Length> lengthUnit() throws SegmentParseException {
		switch (data.charAt(i++)) {
		case 'f':
		case 'F':
			return Length.feet;
		case 'm':
		case 'M':
			return Length.meters;
		default:
			throw new SegmentParseException("invalid distance unit", data.charAtAsSegment(i - 1));
		}
	}
	
	private double expectNumber(String missingErrorMessage) throws SegmentParseException {
		return Double.parseDouble(expect(numberMatcher, missingErrorMessage).toString());
	}
	
	private void surveyFile() throws SegmentParseException {
		Segment file = expect(fileNameMatcher, "missing file name").trim();
		List<LinkStation> linkStations = null;
	
		while (true) {
			skipWhitespaceAndComments();
			if (i == data.length()) {
				throw new SegmentParseException("missing ; at end of file line", data.charAtAsSegment(i));
			}
			switch (data.charAt(i++)) {
			case ';':
				visitor.file(file, new FileDirective(file.toString(), linkStations));
				return;
			case ',':
				skipWhitespaceAndComments();
				String stationName = expect(linkStationMatcher, "missing station name").toString().trim();
				NEVLocation location = null;
	
				skipWhitespaceAndComments();
				if ('[' == data.charAt(i)) {
					i++;
					skipWhitespaceAndComments();
					Unit<Length> unit = lengthUnit();
					skipWhitespaceAndComments();
					expect(',');
					skipWhitespaceAndComments();
					double easting = expectNumber("missing easting");
					skipWhitespaceAndComments();
					expect(',');
					skipWhitespaceAndComments();
					double northing = expectNumber("missing northing");
					skipWhitespaceAndComments();
					expect(',');
					skipWhitespaceAndComments();
					double elevation = expectNumber("missing elevation");
					skipWhitespaceAndComments();
					expect(']');
					skipWhitespaceAndComments();
					location = new NEVLocation(
						new UnitizedDouble<>(easting, unit),
						new UnitizedDouble<>(northing, unit),
						new UnitizedDouble<>(elevation, unit));
				}
				LinkStation linkStation = new LinkStation(stationName, location);
				if (linkStations == null) linkStations = new ArrayList<>();
				linkStations.add(linkStation);
				break;
			default:
				throw new SegmentParseException("invalid character", data.charAtAsSegment(i - 1));
			}
		}
	}

	private int expectUTMZone() throws SegmentParseException {
		Segment text = expect(numberMatcher, "missing UTM zone");
		int decimalIndex = text.indexOf('.');
		if (decimalIndex >= 0) {
			throw new SegmentParseException("invalid UTM zone", text.substring(decimalIndex));
		}
		int utmZone = Integer.parseInt(text.toString());
		if (utmZone < 1 || utmZone > 60) {
			throw new SegmentParseException("invalid UTM zone", text);
		}
		return utmZone;
	}
	
	private UnitizedDouble<Angle> expectUTMConvergence() throws SegmentParseException {
		double angle = expectNumber("missing UTM convergence");
		return new UnitizedDouble<>(angle, Angle.degrees);
	}

	private void location() throws SegmentParseException {
		skipWhitespace();
		double easting = expectNumber("missing easting");
		skipWhitespace();
		expect(',');
		skipWhitespace();
		double northing = expectNumber("missing northing");
		skipWhitespace();
		expect(',');
		skipWhitespace();
		double elevation = expectNumber("missing elevation");
		skipWhitespace();
		expect(',');
		skipWhitespace();
		int utmZone = expectUTMZone();
		skipWhitespace();
		expect(',');
		skipWhitespace();
		UnitizedDouble<Angle> utmConvergence = expectUTMConvergence();
		expect(';');
		
		visitor.location(new LocationDirective(
			new UnitizedDouble<>(easting, Length.meters),
			new UnitizedDouble<>(northing, Length.meters),
			new UnitizedDouble<>(elevation, Length.meters),
			utmZone,
			utmConvergence));
	}
	
	private void datum() throws SegmentParseException {
		visitor.datum(new DatumDirective(expect(datumMatcher, "missing datum").toString().trim()));
		expect(';');
	}
	
	private void utmConvergence() throws SegmentParseException {
		skipWhitespace();
		visitor.utmConvergence(new UTMConvergenceDirective(expectUTMConvergence()));
		expect(';');
	}
	
	private void utmZone() throws SegmentParseException {
		skipWhitespace();
		visitor.utmZone(new UTMZoneDirective(expectUTMZone()));
		expect(';');
	}
	
	private void flags() throws SegmentParseException {
		int flags = 0;
		while (i < data.length()) {
			switch (data.charAt(i++)) {
			case 'o':
				break;
			case 'O':
				flags |= FlagsDirective.OVERRIDE_LRUDS;
				break;
			case 't':
				break;
			case 'T':
				flags |= FlagsDirective.LRUDS_AT_TO_STATION;
				break;
			case ';':
				visitor.flags(new FlagsDirective(flags));
				return;
			default:
				throw new SegmentParseException("invalid character", data.charAtAsSegment(i - 1));
			}
		}
		throw new SegmentParseException("missing or incomplete flags", data.charAtAsSegment(i));
	}
	
	private void comment() {
		int start = i;
		nextLine();
		Segment comment = data.substring(start, i).trim();
		visitor.comment(new CommentDirective(comment.toString()));
	}
}
