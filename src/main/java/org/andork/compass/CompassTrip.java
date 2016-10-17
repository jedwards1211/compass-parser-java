package org.andork.compass;

import java.util.List;

public class CompassTrip {
	private CompassTripHeader header;
	private List<CompassShot> shots;

	public CompassTripHeader getHeader() {
		return header;
	}

	public List<CompassShot> getShots() {
		return shots;
	}

	public void setHeader(CompassTripHeader header) {
		this.header = header;
	}

	public void setShots(List<CompassShot> shots) {
		this.shots = shots;
	}
}
