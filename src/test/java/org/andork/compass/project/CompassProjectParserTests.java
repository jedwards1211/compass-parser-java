package org.andork.compass.project;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class CompassProjectParserTests {
	@Test
	public void basicTest() throws IOException {
		CompassProjectParser parser = new CompassProjectParser();
		CompassProject project = parser.parseProject(getClass().getResourceAsStream("Lechuguilla.mak"),
				Paths.get("here"));

		List<Path> files = project.getDataFiles();
		Assert.assertArrayEquals(
				files.toArray(new Path[files.size()]), new Path[] {
						Paths.get("here", "ENTRANCE.DAT"),
						Paths.get("here", "NEAREAST.DAT"),
						Paths.get("here", "FAREAST.DAT"),
						Paths.get("here", "SOUTH.DAT"),
						Paths.get("here", "FARSOUTH.DAT"),
						Paths.get("here", "NEARWEST.DAT"),
						Paths.get("here", "FARWEST.DAT"),
				});
	}
}
