package com.example.mongodemo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

public class CustomMovieRepositoryImpl implements CustomMovieRepository {

	@Autowired
    MongoTemplate mongoTemplate;
	
	@Override
	public int getDistinctActorsCount() {
		int count = 0;
		
		try {
			//Query query = new Query(
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return count;
	}

}
