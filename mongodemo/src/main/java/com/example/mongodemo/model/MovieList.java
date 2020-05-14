package com.example.mongodemo.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="MovieList")
public class MovieList {

	@XmlElement(name="Movie")
	private List<Movie> data;

	public List<Movie> getMovieList() {
		if (this.data == null) new ArrayList<>();
		return this.data;
	}

	public void setData(List<Movie> data) {
		this.data = data;
	}

}
