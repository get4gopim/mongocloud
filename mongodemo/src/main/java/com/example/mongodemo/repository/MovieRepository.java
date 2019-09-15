package com.example.mongodemo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.mongodemo.model.Movie;

public interface MovieRepository extends MongoRepository<Movie, String> {

	
}
