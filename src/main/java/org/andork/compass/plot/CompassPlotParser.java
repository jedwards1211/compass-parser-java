package org.andork.compass.plot;

import static org.andork.segment.SegmentParser.missingOrInvalid;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.andork.compass.CompassParseError;
import org.andork.compass.CompassParseError.Severity;
import org.andork.segment.Segment;
import org.andork.segment.SegmentParseException;
import org.andork.segment.SegmentParser;

public class CompassPlotParser {
	private static final Pattern UINT_10 = Pattern.compile("[1-9]\\d*");
	private final List<CompassParseError> errors = new ArrayList<>();
	private final List<CompassPlotCommand> commands = new ArrayList<>();

	public Date date(SegmentParser p) throws SegmentParseException {
		int start = p.getIndex();
		int month = uint10(p, "month", 1, 12);
		p.whitespace("missing whitespace before day");
		int day = uint10(p, "day", 1, 31);
		p.whitespace("missing whitespace before year");
		int year = uint10(p, "year");
		Calendar cal = Calendar.getInstance();
		cal.setLenient(false);
		try {
			cal.set(year, month - 1, day);
		} catch (Exception ex) {
			throw new SegmentParseException("invalid date", p.getSegment().substring(start, p.getIndex()));
		}
		return cal.getTime();
	}

	public List<CompassParseError> getErrors() {
		return errors;
	}

	public List<CompassPlotCommand> getCommands() {
		return commands;
	}

	private BigDecimal lrudMeasurement(SegmentParser p, String which) {
		try {
			BigDecimal value = p.bigDecimal(missingOrInvalid(which));
			return value.compareTo(BigDecimal.ZERO) < 0 ? null : value;
		} catch (SegmentParseException e) {
			errors.add(new CompassParseError(e));
			p.advanceToWhitespace();
			return null;
		}
	}

	private int uint10(SegmentParser p, String what) throws SegmentParseException {
		Segment segment = p.match(UINT_10, missingOrInvalid(what));
		final int value;
		try {
			value = Integer.parseInt(segment.toString());
		} catch (Exception ex) {
			throw new SegmentParseException("invalid " + what, ex, segment);
		}
		return value;
	}

	private int uint10(SegmentParser p, String what, int min, int max) throws SegmentParseException {
		Segment segment = p.match(UINT_10, missingOrInvalid(what));
		final int value;
		try {
			value = Integer.parseInt(segment.toString());
		} catch (Exception ex) {
			throw new SegmentParseException("invalid " + what, ex, segment);
		}
		if (value < min || value > max) {
			throw new SegmentParseException(what + " must be between " + min + " and " + max, segment);
		}
		return value;
	}

	public List<CompassPlotCommand> parsePlot(Path path) throws IOException {
		return parsePlot(new FileReader(path.toFile()), path);
	}

	public List<CompassPlotCommand> parsePlot(InputStream in, Object source) throws IOException {
		return parsePlot(new InputStreamReader(in), source);
	}

	public List<CompassPlotCommand> parsePlot(Reader reader, Object source) throws IOException {
		List<CompassPlotCommand> commands = new ArrayList<>();
		BufferedReader r = new BufferedReader(reader);
		String text;
		int line = 0;
		while ((text = r.readLine()) != null) {
			if (text.trim().isEmpty()) continue;
			try {
				CompassPlotCommand command = parseCommand(new SegmentParser(new Segment(text, source, line, 0)));
				if (command != null) {
					commands.add(command);
				}
			} catch (SegmentParseException e) {
				errors.add(new CompassParseError(e));
			}
			line++;
		}
		this.commands.addAll(commands);
		return commands;
	}

	public CompassPlotCommand parseCommand(SegmentParser p) throws SegmentParseException {
		switch (p.character("missing command")) {
		case 'D':
		case 'M':
			return parseDrawSurveyCommand(p.move(-1));
		case 'N':
			return parseBeginSurveyCommand(p.move(-1));
		case 'F':
			return parseBeginFeatureCommand(p.move(-1));
		case 'S':
			return parseBeginSectionCommand(p.move(-1));
		case 'L':
			return parseFeatureCommand(p.move(-1));
		case 'X':
			return parseSurveyBoundsCommand(p.move(-1));
		case 'Z':
			return parseCaveBoundsCommand(p.move(-1));
		case 'O':
			return parseDatumCommand(p.move(-1));
		case 'G':
			return parseUtmZoneCommand(p.move(-1));
		default:
			// ignore for now; there are plenty of undocumented features in the
			// format...
			return null;
		// throw new SegmentParseException("invalid command",
		// p.move(-1).getSegment().charAtAsSegment(p.getIndex()));
		}
	}

	public BeginFeatureCommand parseBeginFeatureCommand(SegmentParser p) throws SegmentParseException {
		p.character('F', missingOrInvalid("command (expected F)"));
		BeginFeatureCommand command = new BeginFeatureCommand();
		command.setFeatureName(p.nonwhitespace(missingOrInvalid("feature name")).toString());
		if (p.atEnd()) {
			return command;
		}
		p.whitespace("missing whitespace before next command");
		if (p.atEnd()) {
			return command;
		}
		p.character('R', missingOrInvalid("command (expected R)"));
		p.whitespace("missing whitespace before min value");
		command.setMinValue(p.bigDecimal(missingOrInvalid("min value")));
		p.whitespace("missing whitespace before max value");
		command.setMaxValue(p.bigDecimal(missingOrInvalid("max value")));
		return command;
	}

	public BeginSectionCommand parseBeginSectionCommand(SegmentParser p) throws SegmentParseException {
		p.character('S', missingOrInvalid("command (expected S)"));
		return new BeginSectionCommand(p.match("[^\r\n]+", "missing section name").toString());
	}

	public BeginSurveyCommand parseBeginSurveyCommand(SegmentParser p) throws SegmentParseException {
		p.character('N', missingOrInvalid("command (expected N)"));
		BeginSurveyCommand command = new BeginSurveyCommand();
		command.setSurveyName(p.nonwhitespace("missing survey name").toString());
		if (p.atEnd()) {
			return command;
		}
		while (!p.atEnd()) {
			p.whitespace("missing whitespace before next command");
			if (p.atEnd()) {
				break;
			}
			switch (p.character("missing command (expected D or C)")) {
			case 'D':
				p.whitespace("missing whitespace before date");
				command.setDate(date(p));
				break;
			case 'C':
				command.setComment(p.rest().toString().trim());
				break;
			default:
				throw new SegmentParseException("invalid command (expected D or C)",
						p.move(-1).getSegment().charAtAsSegment(p.getIndex()));
			}
		}
		return command;
	}

	public void parseBoundsCommand(SegmentParser p, BoundsCommand command) throws SegmentParseException {
		p.whitespace("missing whitespace before min northing");
		command.getLowerBound().setNorthing(p.bigDecimal(missingOrInvalid("min northing")));
		p.whitespace("missing whitespace before max northing");
		command.getUpperBound().setNorthing(p.bigDecimal(missingOrInvalid("max northing")));
		p.whitespace("missing whitespace before min easting");
		command.getLowerBound().setEasting(p.bigDecimal(missingOrInvalid("min easting")));
		p.whitespace("missing whitespace before max easting");
		command.getUpperBound().setEasting(p.bigDecimal(missingOrInvalid("max easting")));
		p.whitespace("missing whitespace before min vertical");
		command.getLowerBound().setVertical(p.bigDecimal(missingOrInvalid("min vertical")));
		p.whitespace("missing whitespace before max vertical");
		command.getUpperBound().setVertical(p.bigDecimal(missingOrInvalid("max vertical")));
	}

	public CaveBoundsCommand parseCaveBoundsCommand(SegmentParser p) throws SegmentParseException {
		p.character('Z', missingOrInvalid("command (expected Z)"));
		CaveBoundsCommand command = new CaveBoundsCommand();
		parseBoundsCommand(p, command);
		if (p.atEnd()) {
			return command;
		}
		p.whitespace("missing whitespace before command");
		if (p.atEnd()) {
			return command;
		}
		p.character('I', missingOrInvalid("command (expected I)"));
		p.whitespace("missing whitespace before distance to farthest station");
		int start = p.getIndex();
		command.setDistanceToFarthestStation(p.bigDecimal(missingOrInvalid("distance to farthest station")));
		if (command.getDistanceToFarthestStation().compareTo(BigDecimal.ZERO) < 0) {
			errors.add(new CompassParseError(Severity.WARNING, "distance to farthest station is negative",
					p.getSegment().substring(start, p.getIndex())));
		}
		return command;
	}

	public SurveyBoundsCommand parseSurveyBoundsCommand(SegmentParser p) throws SegmentParseException {
		p.character('X', missingOrInvalid("command (expected X)"));
		SurveyBoundsCommand command = new SurveyBoundsCommand();
		parseBoundsCommand(p, command);
		return command;
	}

	public DrawSurveyCommand parseDrawSurveyCommand(SegmentParser p) throws SegmentParseException {
		DrawOperation op;
		switch (p.character("missing command (expected M or D)")) {
		case 'M':
			op = DrawOperation.MOVE_TO;
			break;
		case 'D':
			op = DrawOperation.LINE_TO;
			break;
		default:
			throw new SegmentParseException("invalid command (expected M or D)",
					p.move(-1).getSegment().charAtAsSegment(p.getIndex()));
		}
		DrawSurveyCommand command = new DrawSurveyCommand(op);

		p.whitespace("missing whitespace before northing");
		command.getLocation().setNorthing(p.bigDecimal(missingOrInvalid("northing")));
		p.whitespace("missing whitespace before easting");
		command.getLocation().setEasting(p.bigDecimal(missingOrInvalid("easting")));
		p.whitespace("missing whitespace before vertical");
		command.getLocation().setVertical(p.bigDecimal(missingOrInvalid("vertical")));

		while (!p.atEnd()) {
			p.whitespace("missing whitespace before next command");
			if (p.atEnd()) {
				break;
			}
			switch (p.match("[SPI]", missingOrInvalid("command (expected S, P, or I)")).charAt(0)) {
			case 'S':
				command.setStationName(p.nonwhitespace("missing station name").toString());
				break;
			case 'P':
				p.whitespace("missing whitespace before left");
				command.setLeft(lrudMeasurement(p, "left"));
				p.whitespace("missing whitespace before right");
				command.setRight(lrudMeasurement(p, "right"));
				p.whitespace("missing whitespace before up");
				command.setUp(lrudMeasurement(p, "up"));
				p.whitespace("missing whitespace before down");
				command.setDown(lrudMeasurement(p, "down"));
				break;
			case 'I':
				p.whitespace("missing whitespace before distance from entrance");
				int start = p.getIndex();
				command.setDistanceFromEntrance(p.bigDecimal(missingOrInvalid("distance from entrance")));
				if (command.getDistanceFromEntrance().compareTo(BigDecimal.ZERO) < 0) {
					errors.add(new CompassParseError(
							Severity.WARNING, "distance from entrance is negative",
							p.getSegment().substring(start, p.getIndex())));
				}
				// return for now; I've seen an extra undocumented "FL" that
				// comes after this point
				return command;
			default:
				break;
			}
		}

		return command;
	}

	public FeatureCommand parseFeatureCommand(SegmentParser p) throws SegmentParseException {
		p.character('L', missingOrInvalid("command (expected L)"));
		FeatureCommand command = new FeatureCommand();

		p.whitespace("missing whitespace before northing");
		command.getLocation().setNorthing(p.bigDecimal(missingOrInvalid("northing")));
		p.whitespace("missing whitespace before easting");
		command.getLocation().setEasting(p.bigDecimal(missingOrInvalid("easting")));
		p.whitespace("missing whitespace before vertical");
		command.getLocation().setVertical(p.bigDecimal(missingOrInvalid("vertical")));

		while (!p.atEnd()) {
			p.whitespace("missing whitespace before next command");
			if (p.atEnd()) {
				return command;
			}
			switch (p.match("[SPV]", missingOrInvalid("command (expected S, P, or V)")).charAt(0)) {
			case 'S':
				command.setStationName(p.nonwhitespace("missing station name").toString());
				break;
			case 'P':
				p.whitespace("missing whitespace before left");
				command.setLeft(lrudMeasurement(p, "left"));
				p.whitespace("missing whitespace before right");
				command.setRight(lrudMeasurement(p, "right"));
				p.whitespace("missing whitespace before up");
				command.setUp(lrudMeasurement(p, "up"));
				p.whitespace("missing whitespace before down");
				command.setDown(lrudMeasurement(p, "down"));
				break;
			case 'V':
				p.whitespace("missing whitespace before value");
				command.setValue(p.bigDecimal(missingOrInvalid("value")));
				break;
			default:
				break;
			}
		}

		return command;
	}

	public DatumCommand parseDatumCommand(SegmentParser p) throws SegmentParseException {
		p.character('O', missingOrInvalid("command (expected O)"));
		DatumCommand command = new DatumCommand();
		command.setDatum(p.nonwhitespace(missingOrInvalid("datum")).toString());
		return command;
	}

	public UtmZoneCommand parseUtmZoneCommand(SegmentParser p) throws SegmentParseException {
		p.character('G', missingOrInvalid("command (expected G)"));
		UtmZoneCommand command = new UtmZoneCommand();
		command.setUtmZone(p.nonwhitespace(missingOrInvalid("utmzone")).toString());
		return command;
	}
}
