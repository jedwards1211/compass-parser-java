package org.andork.compass.plot;

import static org.andork.segment.SegmentParser.missingOrInvalid;

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

	public DrawSurveyCommand parseDrawSurveyCommand(SegmentParser p) throws SegmentParseException {
		DrawOperation op;
		switch (p.match("[MD]", missingOrInvalid("command (expected M or D)")).charAt(0)) {
		case 'M':
			op = DrawOperation.MOVE_TO;
			break;
		case 'D':
			op = DrawOperation.LINE_TO;
			break;
		default:
			throw new RuntimeException("how can this happen");
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
				break;
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
}
