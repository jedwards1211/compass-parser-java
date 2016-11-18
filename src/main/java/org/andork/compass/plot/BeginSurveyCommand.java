package org.andork.compass.plot;

import java.util.Date;

public class BeginSurveyCommand implements CompassPlotCommand {
	private String surveyName;
	private Date date;
	private String comment;

	public String getComment() {
		return comment;
	}

	public Date getDate() {
		return date;
	}

	public String getSurveyName() {
		return surveyName;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BeginSurveyCommand [surveyName=").append(surveyName).append(", date=").append(date)
				.append(", comment=").append(comment).append("]");
		return builder.toString();
	}
}