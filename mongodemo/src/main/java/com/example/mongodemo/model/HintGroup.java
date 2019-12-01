package com.example.mongodemo.model;

public class HintGroup {

	private long totalMovies;
	private long uniqueActors;
	private long uniqueLanguages;
	private long uniqueDirectors;
	
	private long totalEnglishMovies;
	private long totalTamilMovies;
	private long totalTeluguMovies;

	public HintGroup(long totalMovies, long totalEnglishMovies, long totalTamilMovies, long totalTeluguMovies) {
		super();
		this.totalMovies = totalMovies;
		this.totalEnglishMovies = totalEnglishMovies;
		this.totalTamilMovies = totalTamilMovies;
		this.totalTeluguMovies = totalTeluguMovies;
	}

	public long getTotalMovies() {
		return totalMovies;
	}

	public long getUniqueActors() {
		return uniqueActors;
	}

	public long getUniqueLanguages() {
		return uniqueLanguages;
	}

	public long getUniqueDirectors() {
		return uniqueDirectors;
	}

	public long getTotalEnglishMovies() {
		return totalEnglishMovies;
	}

	public long getTotalTamilMovies() {
		return totalTamilMovies;
	}

	public long getTotalTeluguMovies() {
		return totalTeluguMovies;
	}
	
	

}
