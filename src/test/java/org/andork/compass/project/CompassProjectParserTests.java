package org.andork.compass.project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.andork.segment.Segment;
import org.andork.segment.SegmentParseException;
import org.andork.unit.Length;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CompassProjectParserTests {
	final List<CompassProjectDirective> directives = new ArrayList<>();

	@Before
	public void reset() {
		directives.clear();
	}
	
	private class TestVisitor implements CompassProjectVisitor {
		@Override
		public void file(Segment name, FileDirective file) {
			directives.add(file);
		}

		@Override
		public void location(LocationDirective location) {
			directives.add(location);
		}

		@Override
		public void utmConvergence(UTMConvergenceDirective utmConvergence) {
			directives.add(utmConvergence);
			
		}

		@Override
		public void utmZone(UTMZoneDirective utmZone) {
			directives.add(utmZone);
			
		}

		@Override
		public void datum(DatumDirective datum) {
			directives.add(datum);
		}

		@Override
		public void comment(CommentDirective comment) {
			directives.add(comment);
		}

		@Override
		public void flags(FlagsDirective flags) {
			directives.add(flags);
		}
	}
	
	CompassProjectParser parser = new CompassProjectParser(new TestVisitor());
	
	@Test
	public void testSurveyFile() throws IOException, SegmentParseException {
		parser.parse(getClass().getResourceAsStream("testSurveyFile.mak"), "testSurveyFile.mak");
		FileDirective file = (FileDirective) directives.stream().filter(d -> d instanceof FileDirective).findFirst().get();
		Assert.assertEquals("FULFORD.DAT", file.file);
		Assert.assertEquals(3, file.linkStations.size());
		Assert.assertEquals("A", file.linkStations.get(0).name);
		Assert.assertEquals("B", file.linkStations.get(1).name);
		Assert.assertEquals("C", file.linkStations.get(2).name);
		Assert.assertEquals(Length.feet(1.1), file.linkStations.get(0).location.easting);
		Assert.assertEquals(Length.feet(2.2), file.linkStations.get(0).location.northing);
		Assert.assertEquals(Length.feet(3.3), file.linkStations.get(0).location.elevation);
		Assert.assertEquals(Length.meters(4.4), file.linkStations.get(2).location.easting);
		Assert.assertEquals(Length.meters(5.5), file.linkStations.get(2).location.northing);
		Assert.assertEquals(Length.meters(6.6), file.linkStations.get(2).location.elevation);
	}
	
	@Test
	public void testParseLechuguilla() throws IOException, SegmentParseException {
		parser.parse(getClass().getResourceAsStream("Lechuguilla.mak"), "Lechuguilla.mak");
		Assert.assertArrayEquals(
			new String[] {
				"@546866.900,3561472.900,1414.100,13,-0.260;",
				"&North American 1983;",
				"!ot;",
				"/ ",
				"%-0.260;",
				"#ENTRANCE.DAT;",
				"/ ",
				"#NEAREAST.DAT;",
				"/ ",
				"#FAREAST.DAT;",
				"/ ",
				"#SOUTH.DAT;",
				"/ ",
				"#FARSOUTH.DAT;",
				"/ ",
				"#NEARWEST.DAT;",
				"/ ",
				"#FARWEST.DAT;"
			},
			directives.stream().map(d -> d.toString()).toArray());
	}
}
