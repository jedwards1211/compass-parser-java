package org.andork.compass.plot;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.andork.compass.CompassParseError;
import org.andork.compass.CompassParseError.Severity;
import org.andork.segment.SegmentParseException;
import org.andork.segment.SegmentParser;

public class CompassPlotParser {
	private final List<CompassParseError> errors = new ArrayList<>();

	public List<CompassParseError> getErrors() {
		return errors;
	}

	private BigDecimal lrudMeasurement(SegmentParser p, String name) {
		try {
			BigDecimal value = p.bigDecimal("invalid " + name);
			return value.compareTo(BigDecimal.ZERO) < 0 ? null : value;
		} catch (SegmentParseException e) {
			errors.add(new CompassParseError(e));
			p.advanceToWhitespace();
			return null;
		}
	}

	public DrawSurveyCommand parseDrawSurveyCommand(SegmentParser p) throws SegmentParseException {
		DrawOperation op;
		switch (p.character("missing command (M or D)")) {
		case 'M':
			op = DrawOperation.MOVE_TO;
			break;
		case 'D':
			op = DrawOperation.LINE_TO;
			break;
		default:
			p.move(-1).throwError("invalid command: " + p.charAtIndex());
			return null;
		}
		DrawSurveyCommand command = new DrawSurveyCommand(op);

		p.whitespace("missing whitespace before northing");
		command.getLocation().setNorthing(p.bigDecimal("invalid northing"));
		p.whitespace("missing whitespace before easting");
		command.getLocation().setEasting(p.bigDecimal("invalid easting"));
		p.whitespace("missing whitespace before vertical");
		command.getLocation().setVertical(p.bigDecimal("invalid vertical"));

		while (!p.atEnd()) {
			p.whitespace("missing whitespace before next command");
			if (p.atEnd()) {
				break;
			}
			switch (p.character("expected command (S, P, or I)")) {
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
				command.setDistanceFromEntrance(p.bigDecimal("invalid distance from entrance"));
				if (command.getDistanceFromEntrance().compareTo(BigDecimal.ZERO) < 0) {
					errors.add(new CompassParseError(
							Severity.WARNING, "distance from entrance is negative",
							p.getSegment().substring(start, p.getIndex())));
				}
				break;
			default:
				p.move(-1).throwError("unknown command: " + p.charAtIndex());
			}
		}

		return command;
	}

	public FeatureCommand parseFeatureCommand(SegmentParser p) throws SegmentParseException {
		p.character('L', "command should be L");
		FeatureCommand command = new FeatureCommand();

		p.whitespace("missing whitespace before northing");
		command.getLocation().setNorthing(p.bigDecimal("invalid northing"));
		p.whitespace("missing whitespace before easting");
		command.getLocation().setEasting(p.bigDecimal("invalid easting"));
		p.whitespace("missing whitespace before vertical");
		command.getLocation().setVertical(p.bigDecimal("invalid vertical"));

		while (!p.atEnd()) {
			p.whitespace("missing whitespace before next command");
			if (p.atEnd()) {
				return command;
			}
			switch (p.character("expected command (S, P, or V)")) {
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
				command.setValue(p.bigDecimal("invalid value"));
				break;
			default:
				p.move(-1).throwError("unknown command: " + p.charAtIndex());
			}
		}

		return command;
	}
}
