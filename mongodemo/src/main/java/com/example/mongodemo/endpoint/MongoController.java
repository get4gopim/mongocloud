package com.example.mongodemo.endpoint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.mongodemo.model.Movie;
import com.example.mongodemo.repository.MovieRepository;
import com.example.mongodemo.service.MovieService;

@RestController
@RequestMapping("/demo")
public class MongoController {
	
	@Autowired
	private MovieRepository repository;
	
	@Autowired
	private MovieService service;

	@GetMapping("/moviesAll")
	public List<Movie> getAllMovies() {
		return repository.findAll();
	}
	
	@GetMapping("/movies")
	public List<Movie> getAllMovies(@RequestHeader(name = "X-Start-Year", required = false, defaultValue = "2019") int startYear, 
			@RequestHeader(name = "X-End-Year", required = false, defaultValue = "2019") int endYear) {
		return repository.getByReleaseYear(startYear, endYear);
	}
	
	@GetMapping("/hello/{name}")
	public String sayHello(@PathVariable("name") String name) {
		return "Hello !" + name;
	}
	
	@PostMapping("/movies")
	public Movie addMovie(@RequestBody Movie movie) {
		return repository.save(movie);
	}
	
	@GetMapping("/movies/extract")
	@ResponseStatus(HttpStatus.OK)
	public void extractMovies(@RequestHeader(name = "X-Start-Year", required = false, defaultValue = "2019") int startYear, 
			@RequestHeader(name = "X-End-Year", required = false, defaultValue = "2019") int endYear,
			@RequestHeader(name = "X-Language", required = false, defaultValue = "english") String language) {
		
		service.asyncMethodWithVoidReturnType(language, startYear, endYear);
	}
	
}
