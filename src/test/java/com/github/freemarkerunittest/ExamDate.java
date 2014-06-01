package com.github.freemarkerunittest;

import java.util.Date;

public class ExamDate {
	
	private boolean deskAvailable;
	private boolean extraFee;

	private Date date;

	public boolean getDeskAvailable() {
		return deskAvailable;
	}

	public void setDeskAvailable(boolean deskAvailable) {
		this.deskAvailable = deskAvailable;
	}

	public boolean getExtraFee() {
		return extraFee;
	}

	public void setExtraFee(boolean extraFee) {
		this.extraFee = extraFee;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
