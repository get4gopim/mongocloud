package com.example.mongodemo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "doc_movies")
public class Movie {
	
	@Id
	private String id;
	
	private String title;
	
	private String type;
	
	private boolean isAvailable;
	
	private String actorName;
	
	private String actressName;
	
	private String musicDirector;
	
	private String flimDirector;
	
	private String imageUrl;
	
	private long releaseDate;
	
	private int releaseYear;
	
	private String language;
	
	public Movie() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public String getActorName() {
		return actorName;
	}

	public void setActorName(String actorName) {
		this.actorName = actorName;
	}

	public String getActressName() {
		return actressName;
	}

	public void setActressName(String actressName) {
		this.actressName = actressName;
	}

	public String getMusicDirector() {
		return musicDirector;
	}

	public void setMusicDirector(String musicDirector) {
		this.musicDirector = musicDirector;
	}

	public String getFlimDirector() {
		return flimDirector;
	}

	public void setFlimDirector(String flimDirector) {
		this.flimDirector = flimDirector;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public long getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(long releaseDate) {
		this.releaseDate = releaseDate;
	}

	public int getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(int releaseYear) {
		this.releaseYear = releaseYear;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Movie [id=");
		builder.append(id);
		builder.append(", title=");
		builder.append(title);
		builder.append(", type=");
		builder.append(type);
		builder.append(", isAvailable=");
		builder.append(isAvailable);
		builder.append(", actorName=");
		builder.append(actorName);
		builder.append(", actressName=");
		builder.append(actressName);
		builder.append(", musicDirector=");
		builder.append(musicDirector);
		builder.append(", flimDirector=");
		builder.append(flimDirector);
		builder.append(", imageUrl=");
		builder.append(imageUrl);
		builder.append(", releaseDate=");
		builder.append(releaseDate);
		builder.append(", releaseYear=");
		builder.append(releaseYear);
		builder.append(", language=");
		builder.append(language);
		builder.append("]");
		return builder.toString();
	}

}
