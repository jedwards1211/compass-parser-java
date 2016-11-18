package org.andork.compass.plot;

import java.util.ArrayList;
import java.util.List;

import org.andork.compass.CompassParseError;
import org.andork.compass.CompassParseError.Severity;
import org.andork.compass.LineParser;

public class CompassPlotParser {
	private final List<CompassParseError> errors = new ArrayList<>();

	public List<CompassParseError> getErrors() {
		return errors;
	}

	private double lrudMeasurement(LineParser p, String name) {
		try {
			double value = p.doubleLiteral("invalid " + name);
			return value < 0 ? Double.NaN : value;
		} catch (CompassParseError e) {
			errors.add(e);
			p.advanceToWhitespace();
			return Double.NaN;
		}
	}

	public DrawSurveyCommand parseDrawSurveyCommand(LineParser p) throws CompassParseError {
		DrawOperation op;
		switch (p.charAtIndex()) {
		case 'M':
			op = DrawOperation.MOVE_TO;
			break;
		case 'D':
			op = DrawOperation.LINE_TO;
			break;
		default:
			p.throwError("Invalid command: " + p.charAtIndex() + "; expected D or M");
			return null;
		}
		p.advance(1);
		DrawSurveyCommand command = new DrawSurveyCommand(op);

		p.whitespace("missing whitespace before northing");
		command.getLocation().setNorthing(p.doubleLiteral("invalid northing"));
		p.whitespace("missing whitespace before easting");
		command.getLocation().setEasting(p.doubleLiteral("invalid easting"));
		p.whitespace("missing whitespace before vertical");
		command.getLocation().setVertical(p.doubleLiteral("invalid vertical"));

		while (!p.atEnd()) {
			p.whitespace("missing whitespace before next command");
			if (p.atEnd()) {
				return command;
			}
			switch (p.charAtIndex()) {
			case 'S':
				p.advance(1);
				command.setStationName(p.nonwhitespace("missing station name").toString());
				break;
			case 'P':
				p.advance(1);
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
				p.advance(1);
				p.whitespace("missing whitespace before distance from entrance");
				int start = p.getIndex();
				command.setDistanceFromEntrance(p.doubleLiteral("invalid distance from entrance"));
				if (command.getDistanceFromEntrance() < 0) {
					errors.add(new CompassParseError(
							Severity.WARNING, "distance from entrance is negative",
							p.getSegment().substring(start, p.getIndex())));
				}
				break;
			default:
				p.throwError("unknown command: " + p.charAtIndex());
			}
		}

		return command;
	}

	public FeatureCommand parseFeatureCommand(LineParser p) throws CompassParseError {
		p.character('L', "command should be L");
		FeatureCommand command = new FeatureCommand();

		p.whitespace("missing whitespace before northing");
		command.getLocation().setNorthing(p.doubleLiteral("invalid northing"));
		p.whitespace("missing whitespace before easting");
		command.getLocation().setEasting(p.doubleLiteral("invalid easting"));
		p.whitespace("missing whitespace before vertical");
		command.getLocation().setVertical(p.doubleLiteral("invalid vertical"));

		while (!p.atEnd()) {
			p.whitespace("missing whitespace before next command");
			if (p.atEnd()) {
				return command;
			}
			switch (p.charAtIndex()) {
			case 'S':
				p.advance(1);
				command.setStationName(p.nonwhitespace("missing station name").toString());
				break;
			case 'P':
				p.advance(1);
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
				p.advance(1);
				p.whitespace("missing whitespace before value");
				command.setValue(p.doubleLiteral("invalid value"));
				break;
			default:
				p.throwError("unknown command: " + p.charAtIndex());
			}
		}

		return command;
	}
}
