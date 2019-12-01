package com.example.mongodemo.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.example.mongodemo.model.Movie;

public class CustomMovieRepositoryImpl implements CustomMovieRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomMovieRepositoryImpl.class);
	
	@Autowired
    MongoTemplate mongoTemplate;
	
	@Override
	public int countByLanguage(final String language) {
		int count = 0;
		
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("language").is(language));
			List<Movie> movies = mongoTemplate.find(query, Movie.class);
			
			LOGGER.debug("movies: {}", query.toString());
			
			count = movies.size();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return count;
	}
	
	/*
	 * public long distinctLanguage() { Query query = new Query();
	 * query.addCriteria(Criteria.where("language").is("d1"));
	 * 
	 * List<String> list = mongoTemplate.getCollection("demo") .distinct("language",
	 * String.class); }
	 */

}
