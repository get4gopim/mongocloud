package com.example.mongodemo.model;

public class HintGroup {

	private long totalMovies;
	private long uniqueActors;
	private long uniqueLanguages;
	private long uniqueDirectors;

	public HintGroup(long totalMovies, long uniqueActors, long uniqueLanguages, long uniqueDirectors) {
		super();
		this.totalMovies = totalMovies;
		this.uniqueActors = uniqueActors;
		this.uniqueLanguages = uniqueLanguages;
		this.uniqueDirectors = uniqueDirectors;
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

}
