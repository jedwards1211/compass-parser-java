package org.andork.compass;

import org.andork.segment.Segment;

public class CompassParseError {
	public static enum Severity {
		ERROR, WARNING
	}

	private final Severity severity;
	private final String message;
	private final Segment segment;

	public CompassParseError(Severity severity, String message, Segment segment) {
		super();
		this.severity = severity;
		this.message = message;
		this.segment = segment;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CompassParseError other = (CompassParseError) obj;
		if (message == null) {
			if (other.message != null) {
				return false;
			}
		} else if (!message.equals(other.message)) {
			return false;
		}
		if (segment == null) {
			if (other.segment != null) {
				return false;
			}
		} else if (!segment.equals(other.segment)) {
			return false;
		}
		if (severity != other.severity) {
			return false;
		}
		return true;
	}

	public String getMessage() {
		return message;
	}

	public Segment getSegment() {
		return segment;
	}

	public Severity getSeverity() {
		return severity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (message == null ? 0 : message.hashCode());
		result = prime * result + (segment == null ? 0 : segment.hashCode());
		result = prime * result + (severity == null ? 0 : severity.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return severity.toString().toLowerCase() + ": " + message +
				" (in " + segment.source + ", line " + (segment.startLine + 1) +
				", column " + (segment.startCol + 1) + "):\n" +
				segment.underlineInContext();
	}
}
