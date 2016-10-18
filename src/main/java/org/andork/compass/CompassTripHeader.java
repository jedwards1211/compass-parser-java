package org.andork.compass;

import java.util.Date;

/**
 * Information about a trip and measurement settings that apply to all shots in
 * the trip.
 */
public class CompassTripHeader {
	/**
	 * The name of the cave this trip was in.
	 */
	private String caveName;
	/**
	 * The name of this survey.
	 */
	private String surveyName;
	/**
	 * The date of this trip.
	 */
	private Date date;
	/**
	 * More details about this survey.
	 */
	private String comment;
	/**
	 * The humans or killer robots who performed this survey. Unfortunately
	 * compass has no standard format for delineating surveyor names.
	 */
	private String team;
	/**
	 * Magnetic declination, in degrees
	 */
	private double declination = 0;
	/**
	 * Display for shot lengths
	 */
	private LengthUnit lengthUnit = LengthUnit.DECIMAL_FEET;
	/**
	 * Display unit for shot LRUDs
	 */
	private LengthUnit lrudUnit = LengthUnit.DECIMAL_FEET;
	/**
	 * Display unit for shot azimuths
	 */
	private AzimuthUnit azimuthUnit = AzimuthUnit.DEGREES;
	/**
	 * Display unit for shot inclinations
	 */
	private InclinationUnit inclinationUnit = InclinationUnit.DEGREES;

	private final LrudItem[] lrudOrder = {
			LrudItem.LEFT,
			LrudItem.RIGHT,
			LrudItem.UP,
			LrudItem.DOWN,
	};
	private final ShotItem[] shotMeasurementOrder = {
			ShotItem.LENGTH,
			ShotItem.AZIMUTH,
			ShotItem.INCLINATION,
	};
	/**
	 * Whether shots in this trip have backsights.
	 */
	private boolean hasBacksights = true;
	/**
	 * Whether LRUDs in this trip are associated with the from or to station of
	 * shots.
	 */
	private LrudAssociation lrudAssociation = LrudAssociation.FROM;
	/**
	 * Correction added to the length of each shot in this trip, in feet.
	 */
	private double lengthCorrection = 0;
	/**
	 * Correction added to the frontsight azimuth of each shot in this trip, in
	 * degrees.
	 */
	private double frontsightAzimuthCorrection = 0;
	/**
	 * Correction added to the frontsight inclination of each shot in this trip,
	 * in degrees.
	 */
	private double frontsightInclinationCorrection = 0;
	/**
	 * Correction added to the backsight azimuth of each shot in this trip, in
	 * degrees.
	 */
	private double backsightAzimuthCorrection = 0;
	/**
	 * Correction added to the backsight inclination of each shot in this trip,
	 * in degrees.
	 */
	private double backsightInclinationCorrection = 0;

	public AzimuthUnit getAzimuthUnit() {
		return azimuthUnit;
	}

	public double getBacksightAzimuthCorrection() {
		return backsightAzimuthCorrection;
	}

	public double getBacksightInclinationCorrection() {
		return backsightInclinationCorrection;
	}

	public String getCaveName() {
		return caveName;
	}

	public String getComment() {
		return comment;
	}

	public Date getDate() {
		return date;
	}

	public double getDeclination() {
		return declination;
	}

	public double getFrontsightAzimuthCorrection() {
		return frontsightAzimuthCorrection;
	}

	public double getFrontsightInclinationCorrection() {
		return frontsightInclinationCorrection;
	}

	public InclinationUnit getInclinationUnit() {
		return inclinationUnit;
	}

	public double getLengthCorrection() {
		return lengthCorrection;
	}

	public LengthUnit getLengthUnit() {
		return lengthUnit;
	}

	public LrudAssociation getLrudAssociation() {
		return lrudAssociation;
	}

	public LrudItem[] getLrudOrder() {
		return lrudOrder;
	}

	public LengthUnit getLrudUnit() {
		return lrudUnit;
	}

	public ShotItem[] getShotMeasurementOrder() {
		return shotMeasurementOrder;
	}

	public String getSurveyName() {
		return surveyName;
	}

	public String getTeam() {
		return team;
	}

	public boolean hasBacksights() {
		return hasBacksights;
	}

	public void setAzimuthUnit(AzimuthUnit azimuthUnit) {
		this.azimuthUnit = azimuthUnit;
	}

	public void setBacksightAzimuthCorrection(double backsightAzimuthCorrection) {
		this.backsightAzimuthCorrection = backsightAzimuthCorrection;
	}

	public void setBacksightInclinationCorrection(double backsightInclinationCorrection) {
		this.backsightInclinationCorrection = backsightInclinationCorrection;
	}

	public void setCaveName(String caveName) {
		this.caveName = caveName;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setDeclination(double declination) {
		this.declination = declination;
	}

	public void setFrontsightAzimuthCorrection(double frontsightAzimuthCorrection) {
		this.frontsightAzimuthCorrection = frontsightAzimuthCorrection;
	}

	public void setFrontsightInclinationCorrection(double frontsightInclinationCorrection) {
		this.frontsightInclinationCorrection = frontsightInclinationCorrection;
	}

	public void setHasBacksights(boolean hasBacksights) {
		this.hasBacksights = hasBacksights;
	}

	public void setInclinationUnit(InclinationUnit inclinationUnit) {
		this.inclinationUnit = inclinationUnit;
	}

	public void setLengthCorrection(double lengthCorrection) {
		this.lengthCorrection = lengthCorrection;
	}

	public void setLengthUnit(LengthUnit lengthUnit) {
		this.lengthUnit = lengthUnit;
	}

	public void setLrudAssociation(LrudAssociation lrudAssociation) {
		this.lrudAssociation = lrudAssociation;
	}

	public void setLrudUnit(LengthUnit lrudUnit) {
		this.lrudUnit = lrudUnit;
	}

	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}

	public void setTeam(String team) {
		this.team = team;
	}

}
