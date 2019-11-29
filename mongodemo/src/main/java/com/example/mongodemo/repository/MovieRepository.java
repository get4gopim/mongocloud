package com.example.mongodemo.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.mongodemo.model.Movie;

public interface MovieRepository extends MongoRepository<Movie, String> {

	List<Movie> findByReleaseYearBetween(int startYear, int endYear);
	
	@Query("{'releaseYear' : { $gte: ?0, $lte: ?1 } }")                 
	List<Movie> getByReleaseYear(int startYear, int endYear, Sort sort);
	
	List<Movie> findByTitleLike(String title);
	
}
