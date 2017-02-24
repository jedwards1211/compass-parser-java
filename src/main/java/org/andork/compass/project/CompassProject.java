package org.andork.compass.project;

import java.nio.file.Path;
import java.util.List;

public class CompassProject {
	private List<Path> dataFiles;

	public List<Path> getDataFiles() {
		return dataFiles;
	}

	public void setDataFiles(List<Path> dataFiles) {
		this.dataFiles = dataFiles;
	}
}
