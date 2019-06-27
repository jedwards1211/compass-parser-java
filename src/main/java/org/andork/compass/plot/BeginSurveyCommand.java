package org.andork.compass.plot;

import java.util.Calendar;
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
		StringBuilder builder = new StringBuilder("N");
		builder.append(surveyName.substring(0, Math.min(12, surveyName.length())));
		builder.append("\tD");
		if (date != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			builder.append(' ').append(c.get(Calendar.MONTH) + 1);
			builder.append(' ').append(c.get(Calendar.DAY_OF_MONTH));
			builder.append(' ').append(c.get(Calendar.YEAR));
		} else {
			builder.append(" 1 1 1");
		}
		if (comment != null) {
			builder.append("\tC").append(comment.substring(0, Math.min(80, comment.length())));
		}
		return builder.toString();
	}
}
