package org.andork.compass.plot;

public class BeginSectionCommand implements CompassPlotCommand {
	private String sectionName;

	public BeginSectionCommand(String sectionName) {
		super();
		this.sectionName = sectionName;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
}
