package com.example.mongodemo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.mongodemo.model.Movie;

public interface MovieRepository extends MongoRepository<Movie, String>, CustomMovieRepository {

	List<Movie> findByReleaseYearBetween(int startYear, int endYear);
	
	@Query("{'releaseYear' : { $gte: ?0, $lte: ?1 } }")                 
	List<Movie> getByReleaseYear(int startYear, int endYear, Sort sort);
	
	List<Movie> findByTitleLike(String title);
	
	@Query("{'id' : ?0 }") 
	Movie findByMovieId(String title);
	
	long count();
	
	@Query("{'language' : ?0 }")
	List<String> findDistinctByLanguage(String language);
	
	@Query("{'releaseYear' : { $gte: ?0, $lte: ?1 } }")                 
	Page<Movie> getByReleaseYearPagable(int startYear, int endYear, Pageable pageable);
	
	Page<Movie> findByTitleLike(String title, Pageable pageable);
	
	@Query("{'language' : ?0 }")
    List<String> findDistinctLanguage1(String language);
}
