package org.andork.compass.project;

import org.andork.segment.Segment;

public interface CompassProjectVisitor {
	public void file(Segment name, FileDirective file);
	
	public void location(LocationDirective location);

	public void utmConvergence(UTMConvergenceDirective utmConvergence);

	public void utmZone(UTMZoneDirective utmZone);

	public void datum(DatumDirective datum);
	
	public void flags(FlagsDirective flags);

	public void comment(CommentDirective comment);
}
