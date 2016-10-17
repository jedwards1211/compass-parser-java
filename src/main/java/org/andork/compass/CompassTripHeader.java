package org.andork.compass;

import java.util.Date;

public class CompassTripHeader {
	private String caveName;
	private String surveyName;
	private Date date;
	private String comment;
	private String team;
	private double declination = 0;
	private DistanceUnit lengthUnit;
	private DistanceUnit lrudUnit;
	private InclinationUnit inclinationUnit;
	private final LrudMeasurement[] lrudOrder = {
			LrudMeasurement.LEFT,
			LrudMeasurement.RIGHT,
			LrudMeasurement.UP,
			LrudMeasurement.DOWN,
	};
	private final ShotMeasurement[] shotMeasurementOrder = {
			ShotMeasurement.DISTANCE,
			ShotMeasurement.AZIMUTH,
			ShotMeasurement.INCLINATION,
	};
	private boolean hasBacksights;
	private StationSide lrudAssociation;
	private double lengthCorrection;
	private double frontsightAzimuthCorrection;
	private double frontsightInclinationCorrection;
	private double backsightAzimuthCorrection;
	private double backsightInclinationCorrection;

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

	public DistanceUnit getLengthUnit() {
		return lengthUnit;
	}

	public StationSide getLrudAssociation() {
		return lrudAssociation;
	}

	public LrudMeasurement[] getLrudOrder() {
		return lrudOrder;
	}

	public DistanceUnit getLrudUnit() {
		return lrudUnit;
	}

	public ShotMeasurement[] getShotMeasurementOrder() {
		return shotMeasurementOrder;
	}

	public String getSurveyName() {
		return surveyName;
	}

	public String getTeam() {
		return team;
	}

	public boolean isHasBacksights() {
		return hasBacksights;
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

	public void setLengthUnit(DistanceUnit lengthUnit) {
		this.lengthUnit = lengthUnit;
	}

	public void setLrudAssociation(StationSide lrudAssociation) {
		this.lrudAssociation = lrudAssociation;
	}

	public void setLrudUnit(DistanceUnit lrudUnit) {
		this.lrudUnit = lrudUnit;
	}

	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}

	public void setTeam(String team) {
		this.team = team;
	}

}
