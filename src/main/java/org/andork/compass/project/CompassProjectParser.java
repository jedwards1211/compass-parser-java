package org.andork.compass.project;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompassProjectParser {
	public static Pattern dataFileLinePattern = Pattern.compile("^\\s*#([^;]+);");

	public CompassProject parseProject(Path projectFile) throws IOException {
		try (FileInputStream in = new FileInputStream(projectFile.toFile())) {
			return parseProject(in, projectFile.getParent());
		}
	}

	public CompassProject parseProject(InputStream in, Path directory) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			CompassProject project = new CompassProject();
			project.setDataFiles(new ArrayList<>());

			String line;
			while ((line = reader.readLine()) != null) {
				Matcher m = dataFileLinePattern.matcher(line);
				if (!m.find()) {
					continue;
				}
				project.getDataFiles().add(directory != null
						? directory.resolve(Paths.get(m.group(1)))
						: Paths.get(m.group(1)));
			}

			return project;
		}
	}
}
