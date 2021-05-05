package org.andork.compass.survey;

import java.util.Date;

import org.andork.compass.AzimuthUnit;
import org.andork.compass.InclinationUnit;
import org.andork.compass.LengthUnit;
import org.andork.compass.LrudAssociation;
import org.andork.compass.LrudItem;
import org.andork.unit.Angle;
import org.andork.unit.Length;
import org.andork.unit.UnitizedDouble;

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
	 * The humans or killer robots who performed this survey. Unfortunately compass
	 * has no standard format for delineating surveyor names.
	 */
	private String team;
	/**
	 * Magnetic declination, in degrees
	 */
	private UnitizedDouble<Angle> declination = Angle.degrees(0);
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

	private final LrudItem[] lrudOrder = { LrudItem.LEFT, LrudItem.RIGHT, LrudItem.UP, LrudItem.DOWN, };
	private ShotItem[] shotMeasurementOrder =
		{ ShotItem.LENGTH, ShotItem.FRONTSIGHT_AZIMUTH, ShotItem.FRONTSIGHT_INCLINATION, };
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
	private UnitizedDouble<Length> lengthCorrection = Length.feet(0);
	/**
	 * Correction added to the frontsight azimuth of each shot in this trip, in
	 * degrees.
	 */
	private UnitizedDouble<Angle> frontsightAzimuthCorrection = Angle.degrees(0);
	/**
	 * Correction added to the frontsight inclination of each shot in this trip, in
	 * degrees.
	 */
	private UnitizedDouble<Angle> frontsightInclinationCorrection = Angle.degrees(0);
	/**
	 * Correction added to the backsight azimuth of each shot in this trip, in
	 * degrees.
	 */
	private UnitizedDouble<Angle> backsightAzimuthCorrection = Angle.degrees(0);
	/**
	 * Correction added to the backsight inclination of each shot in this trip, in
	 * degrees.
	 */
	private UnitizedDouble<Angle> backsightInclinationCorrection = Angle.degrees(0);

	public AzimuthUnit getAzimuthUnit() {
		return azimuthUnit;
	}

	public UnitizedDouble<Angle> getBacksightAzimuthCorrection() {
		return backsightAzimuthCorrection;
	}

	public UnitizedDouble<Angle> getBacksightInclinationCorrection() {
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

	public UnitizedDouble<Angle> getDeclination() {
		return declination;
	}

	public UnitizedDouble<Angle> getFrontsightAzimuthCorrection() {
		return frontsightAzimuthCorrection;
	}

	public UnitizedDouble<Angle> getFrontsightInclinationCorrection() {
		return frontsightInclinationCorrection;
	}

	public InclinationUnit getInclinationUnit() {
		return inclinationUnit;
	}

	public UnitizedDouble<Length> getLengthCorrection() {
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

	public void setBacksightAzimuthCorrection(UnitizedDouble<Angle> backsightAzimuthCorrection) {
		this.backsightAzimuthCorrection = backsightAzimuthCorrection;
	}

	public void setBacksightInclinationCorrection(UnitizedDouble<Angle> backsightInclinationCorrection) {
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

	public void setDeclination(UnitizedDouble<Angle> declination) {
		this.declination = declination;
	}

	public void setFrontsightAzimuthCorrection(UnitizedDouble<Angle> frontsightAzimuthCorrection) {
		this.frontsightAzimuthCorrection = frontsightAzimuthCorrection;
	}

	public void setFrontsightInclinationCorrection(UnitizedDouble<Angle> frontsightInclinationCorrection) {
		this.frontsightInclinationCorrection = frontsightInclinationCorrection;
	}

	public void setHasBacksights(boolean hasBacksights) {
		this.hasBacksights = hasBacksights;
	}

	public void setInclinationUnit(InclinationUnit inclinationUnit) {
		this.inclinationUnit = inclinationUnit;
	}

	public void setLengthCorrection(UnitizedDouble<Length> lengthCorrection) {
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

	public void setShotMeasurementOrder(ShotItem[] order) {
		this.shotMeasurementOrder = order;
	}
}
