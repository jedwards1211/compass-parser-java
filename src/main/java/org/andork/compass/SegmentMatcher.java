package org.andork.compass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SegmentMatcher {
	private final Segment segment;
	private final Matcher matcher;

	public SegmentMatcher(Segment segment, Pattern pattern) {
		this.segment = segment;
		matcher = pattern.matcher(segment.toString());
	}

	public int end() {
		return matcher.end();
	}

	public int end(int group) {
		return matcher.end(group);
	}

	public int end(String name) {
		return matcher.end(name);
	}

	public boolean find() {
		return matcher.find();
	}

	public boolean find(int start) {
		return matcher.find(start);
	}

	public Segment group() {
		return segment.substring(matcher.start(), matcher.end());
	}

	public Segment group(int group) {
		int start = matcher.start(group);
		int end = matcher.end(group);

		if (start < 0 || end < 0) {
			return null;
		}

		return segment.substring(start, end);
	}

	public int groupCount() {
		return matcher.groupCount();
	}

	public boolean hasAnchoringBounds() {
		return matcher.hasAnchoringBounds();
	}

	public boolean hasTransparentBounds() {
		return matcher.hasTransparentBounds();
	}

	public boolean hitEnd() {
		return matcher.hitEnd();
	}

	public boolean lookingAt() {
		return matcher.lookingAt();
	}

	public boolean matches() {
		return matcher.matches();
	}

	public Pattern pattern() {
		return matcher.pattern();
	}

	public SegmentMatcher region(int start, int end) {
		matcher.region(start, end);
		return this;
	}

	public int regionEnd() {
		return matcher.regionEnd();
	}

	public int regionStart() {
		return matcher.regionStart();
	}

	public boolean requireEnd() {
		return matcher.requireEnd();
	}

	public SegmentMatcher reset() {
		matcher.reset();
		return this;
	}

	public Segment segment() {
		return segment;
	}

	public int start() {
		return matcher.start();
	}

	public int start(int group) {
		return matcher.start(group);
	}

	public SegmentMatcher useAnchoringBounds(boolean b) {
		matcher.useAnchoringBounds(b);
		return this;
	}

	public SegmentMatcher usePattern(Pattern newPattern) {
		matcher.usePattern(newPattern);
		return this;
	}

	public SegmentMatcher useTransparentBounds(boolean b) {
		matcher.useTransparentBounds(b);
		return this;
	}

}
