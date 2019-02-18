package org.andork.compass.project;

import java.util.Objects;

import org.andork.compass.NEVLocation;

public class LinkStation {
	public final String name;
	public final NEVLocation location;

	public LinkStation(String name, NEVLocation location) {
		super();
		this.name = Objects.requireNonNull(name);
		this.location = location;
	}
}
