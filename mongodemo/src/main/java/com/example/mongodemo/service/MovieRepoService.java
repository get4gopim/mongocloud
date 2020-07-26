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

import java.util.Arrays;
import java.util.List;

@Service
public class MovieRepoService {
	
	@Autowired
	private MovieRepository movieRepository;
	
	public Page<Movie> findAllPages(int startYear, int endYear, int currentPage, int size) {
		String[] properties = { "releaseYear" };
		Sort sort = Sort.by(Sort.Direction.DESC, properties);
		Pageable pageable = PageRequest.of(currentPage, size, sort);
		
		Page<Movie> pages = movieRepository.getByReleaseYearPagable(startYear, endYear, pageable);
		
		return pages;
	}
	
	public Page<Movie> findAllPages(int startYear, int endYear, Pageable pageable) {
		String[] properties = { "releaseYear" };
		Sort sort = Sort.by(Sort.Direction.DESC, properties);
		Pageable pageable1 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		
		Page<Movie> pages = movieRepository.getByReleaseYearPagable(startYear, endYear, pageable1);

		return pages;
	}
	
	public Page<Movie> findByTitleLike(String title, Pageable pageable) {
		String[] properties = { "releaseYear" };
		Sort sort = Sort.by(Sort.Direction.DESC, properties);
		Pageable pageable1 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		
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
		long totalEnglishMovies = movieRepository.countByLanguage("English");
		long totalTamilMovies = movieRepository.countByLanguage("Tamil");
		long totalTeluguMovies = movieRepository.countByLanguage("Telugu");
		
		return new HintGroup(totalMovies, totalEnglishMovies, totalTamilMovies, totalTeluguMovies);
	}

}
