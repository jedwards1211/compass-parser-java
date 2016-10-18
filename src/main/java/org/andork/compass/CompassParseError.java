package org.andork.compass;

public class CompassParseError {
	public static enum Severity {
		ERROR, WARNING
	}

	private Segment segment;
	private Severity severity;
	private String message;
	private String source;
	private int startLine;
	private int startColumn;
	private int endLine;
	private int endColumn;

	public CompassParseError(Severity severity, String message, Segment segment) {
		super();
		this.severity = severity;
		this.message = message;
		this.segment = segment;
		source = segment.source.toString();
		startLine = segment.startLine;
		startColumn = segment.startCol;
		endLine = segment.endLine;
		endColumn = segment.endCol;
	}

	public CompassParseError(Severity severity, String message, String source, int line, int column) {
		super();
		this.severity = severity;
		this.message = message;
		this.source = source;
		startLine = endLine = line;
		startColumn = endColumn = column;
	}

	public CompassParseError(Severity severity, String message, String source, int line, int startColumn,
			int endColumn) {
		super();
		this.severity = severity;
		this.message = message;
		this.source = source;
		startLine = line;
		endLine = line;
		this.startColumn = startColumn;
		this.endColumn = endColumn;
	}

	public CompassParseError(Severity severity, String message, String source, int startLine, int startColumn,
			int endLine,
			int endColumn) {
		super();
		this.severity = severity;
		this.message = message;
		this.source = source;
		this.startLine = startLine;
		this.startColumn = startColumn;
		this.endLine = endLine;
		this.endColumn = endColumn;
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
		if (endColumn != other.endColumn) {
			return false;
		}
		if (endLine != other.endLine) {
			return false;
		}
		if (source == null) {
			if (other.source != null) {
				return false;
			}
		} else if (!source.equals(other.source)) {
			return false;
		}
		if (message == null) {
			if (other.message != null) {
				return false;
			}
		} else if (!message.equals(other.message)) {
			return false;
		}
		if (severity != other.severity) {
			return false;
		}
		if (startColumn != other.startColumn) {
			return false;
		}
		if (startLine != other.startLine) {
			return false;
		}
		return true;
	}

	public int getEndColumn() {
		return endColumn;
	}

	public int getEndLine() {
		return endLine;
	}

	public String getMessage() {
		return message;
	}

	public Severity getSeverity() {
		return severity;
	}

	public String getSource() {
		return source;
	}

	public int getStartColumn() {
		return startColumn;
	}

	public int getStartLine() {
		return startLine;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + endColumn;
		result = prime * result + endLine;
		result = prime * result + (source == null ? 0 : source.hashCode());
		result = prime * result + (message == null ? 0 : message.hashCode());
		result = prime * result + (severity == null ? 0 : severity.hashCode());
		result = prime * result + startColumn;
		result = prime * result + startLine;
		return result;
	}

	@Override
	public String toString() {
		return "CompassParseError [severity=" + severity + ", message=" + message + ", file=" + source + ", startLine="
				+ startLine + ", startColumn=" + startColumn + ", endLine=" + endLine + ", endColumn=" + endColumn
				+ "]";
	}

}
