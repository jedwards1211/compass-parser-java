package org.andork.compass.project;

import java.util.Objects;

public class CommentDirective implements CompassProjectDirective {
	public final String comment;

	public CommentDirective(String comment) {
		this.comment = Objects.requireNonNull(comment);
	}
	
	public String toString() {
		return "/ " + comment;
	}
}
