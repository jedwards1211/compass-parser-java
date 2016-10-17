package org.andork.compass;

import java.io.File;

public class CompassParseError {
	public static enum Severity {
		ERROR, WARNING
	}

	private Severity severity;
	private String message;
	private File file;
	private int startLine;
	private int startColumn;
	private int endLine;
	private int endColumn;

	public CompassParseError(Severity severity, String message, File file, int line, int column) {
		super();
		this.severity = severity;
		this.message = message;
		this.file = file;
		startLine = endLine = line;
		startColumn = endColumn = column;
	}

	public CompassParseError(Severity severity, String message, File file, int line, int startColumn,
			int endColumn) {
		super();
		this.severity = severity;
		this.message = message;
		this.file = file;
		startLine = line;
		endLine = line;
		this.startColumn = startColumn;
		this.endColumn = endColumn;
	}

	public CompassParseError(Severity severity, String message, File file, int startLine, int startColumn, int endLine,
			int endColumn) {
		super();
		this.severity = severity;
		this.message = message;
		this.file = file;
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
		if (file == null) {
			if (other.file != null) {
				return false;
			}
		} else if (!file.equals(other.file)) {
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

	public File getFile() {
		return file;
	}

	public String getMessage() {
		return message;
	}

	public Severity getSeverity() {
		return severity;
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
		result = prime * result + (file == null ? 0 : file.hashCode());
		result = prime * result + (message == null ? 0 : message.hashCode());
		result = prime * result + (severity == null ? 0 : severity.hashCode());
		result = prime * result + startColumn;
		result = prime * result + startLine;
		return result;
	}
}
