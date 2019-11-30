package com.example.mongodemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.mongodemo.model.HintGroup;
import com.example.mongodemo.model.Movie;
import com.example.mongodemo.repository.MovieRepository;

@Service
public class MovieRepoService {
	
	@Autowired
	private MovieRepository movieRepository;
	
	public Page<Movie> findAllPages(int startYear, int endYear, int currentPage, int size) {
		Pageable pageable = PageRequest.of(currentPage, size, new Sort(Sort.Direction.DESC, "releaseYear"));
		
		Page<Movie> pages = movieRepository.getByReleaseYearPagable(startYear, endYear, pageable);
		
		return pages;
	}
	
	public Page<Movie> findAllPages(int startYear, int endYear, Pageable pageable) {
		Pageable pageable1 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.DESC, "releaseYear"));
		
		Page<Movie> pages = movieRepository.getByReleaseYearPagable(startYear, endYear, pageable1);

		return pages;
	}
	
	public Page<Movie> findByTitleLike(String title, Pageable pageable) {
		Pageable pageable1 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.DESC, "releaseYear"));
		
		Page<Movie> pages = movieRepository.findByTitleLike(title, pageable1);

		return pages;
	}
	
	public Movie saveMovie(Movie updatedMovie) {
		Movie dbMovie = movieRepository.findByMovieId(updatedMovie.getId());
		
		dbMovie.setActorName(updatedMovie.getActorName());
		dbMovie.setActressName(updatedMovie.getActressName());
		dbMovie.setMusicDirector(updatedMovie.getMusicDirector());
		dbMovie.setFlimDirector(updatedMovie.getFlimDirector());
		
		return movieRepository.save(dbMovie);
	}
	
	public HintGroup getHints() {
		long totalMovies = movieRepository.count();
		long uniqueActors = 99;
		long uniqueLanguages = 2;
		long uniqueDirectors = 240;
		
		return new HintGroup(totalMovies, uniqueActors, uniqueLanguages, uniqueDirectors);
	}

}
