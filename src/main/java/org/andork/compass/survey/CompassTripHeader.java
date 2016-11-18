package org.andork.compass.survey;

import java.math.BigDecimal;
import java.util.Date;

import org.andork.compass.AzimuthUnit;
import org.andork.compass.InclinationUnit;
import org.andork.compass.LengthUnit;
import org.andork.compass.LrudAssociation;
import org.andork.compass.LrudItem;

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
	private BigDecimal declination = BigDecimal.ZERO;
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
	private BigDecimal lengthCorrection = BigDecimal.ZERO;
	/**
	 * Correction added to the frontsight azimuth of each shot in this trip, in
	 * degrees.
	 */
	private BigDecimal frontsightAzimuthCorrection = BigDecimal.ZERO;
	/**
	 * Correction added to the frontsight inclination of each shot in this trip,
	 * in degrees.
	 */
	private BigDecimal frontsightInclinationCorrection = BigDecimal.ZERO;
	/**
	 * Correction added to the backsight azimuth of each shot in this trip, in
	 * degrees.
	 */
	private BigDecimal backsightAzimuthCorrection = BigDecimal.ZERO;
	/**
	 * Correction added to the backsight inclination of each shot in this trip,
	 * in degrees.
	 */
	private BigDecimal backsightInclinationCorrection = BigDecimal.ZERO;

	public AzimuthUnit getAzimuthUnit() {
		return azimuthUnit;
	}

	public BigDecimal getBacksightAzimuthCorrection() {
		return backsightAzimuthCorrection;
	}

	public BigDecimal getBacksightInclinationCorrection() {
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

	public BigDecimal getDeclination() {
		return declination;
	}

	public BigDecimal getFrontsightAzimuthCorrection() {
		return frontsightAzimuthCorrection;
	}

	public BigDecimal getFrontsightInclinationCorrection() {
		return frontsightInclinationCorrection;
	}

	public InclinationUnit getInclinationUnit() {
		return inclinationUnit;
	}

	public BigDecimal getLengthCorrection() {
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

	public void setBacksightAzimuthCorrection(BigDecimal backsightAzimuthCorrection) {
		this.backsightAzimuthCorrection = backsightAzimuthCorrection;
	}

	public void setBacksightInclinationCorrection(BigDecimal backsightInclinationCorrection) {
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

	public void setDeclination(BigDecimal declination) {
		this.declination = declination;
	}

	public void setFrontsightAzimuthCorrection(BigDecimal frontsightAzimuthCorrection) {
		this.frontsightAzimuthCorrection = frontsightAzimuthCorrection;
	}

	public void setFrontsightInclinationCorrection(BigDecimal frontsightInclinationCorrection) {
		this.frontsightInclinationCorrection = frontsightInclinationCorrection;
	}

	public void setHasBacksights(boolean hasBacksights) {
		this.hasBacksights = hasBacksights;
	}

	public void setInclinationUnit(InclinationUnit inclinationUnit) {
		this.inclinationUnit = inclinationUnit;
	}

	public void setLengthCorrection(BigDecimal lengthCorrection) {
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
