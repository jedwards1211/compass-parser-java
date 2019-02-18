package org.andork.compass.project;

public class FlagsDirective implements CompassProjectDirective {
	public final int flags;
	
	public static final int OVERRIDE_LRUDS = 0x1;
	public static final int LRUDS_AT_TO_STATION = 0x2;

	public FlagsDirective(int flags) {
		this.flags = flags;
	}
	
	public boolean isOverrideLruds() {
		return (flags & OVERRIDE_LRUDS) != 0;
	}
	
	public boolean isLrudsAtToStation() {
		return (flags & LRUDS_AT_TO_STATION) != 0;
	}
	
	public String toString() {
		return "!" + (isOverrideLruds() ? 'O' : 'o') + (isLrudsAtToStation() ? 'T' : 't') + ';';
	}
}
