package com.example.mongodemo.model;

public class SearchItems {

	private int startYear;
	private int endYear;

	private String title;

	public SearchItems() {

	}

	public SearchItems(String title) {
		this.title = title;
	}

	public SearchItems(int startYear, int endYear) {
		super();
		this.startYear = startYear;
		this.endYear = endYear;
	}

	public int getStartYear() {
		return startYear;
	}

	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}

	public int getEndYear() {
		return endYear;
	}

	public void setEndYear(int endYear) {
		this.endYear = endYear;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
