package org.andork.compass.plot;

public class DatumCommand implements CompassPlotCommand {
	private String datum;

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DatumCommand [datum=").append(datum).append("]");
		return builder.toString();
	}
}
