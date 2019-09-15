package com.example.mongodemo.endpoint;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mongodemo.model.Movie;
import com.example.mongodemo.repository.MovieRepository;

@RestController
@RequestMapping("/demo")
public class MongoController {
	
	@Autowired
	private MovieRepository repository;

	@GetMapping("/movies")
	public List<Movie> getAllMovies() {
		return repository.findAll();
	}
	
	@GetMapping("/hello/{name}")
	public String sayHello(@PathVariable("name") String name) {
		return "Hello !" + name;
	}
}
