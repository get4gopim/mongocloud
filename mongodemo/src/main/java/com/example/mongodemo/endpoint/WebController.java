package com.example.mongodemo.endpoint;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.mongodemo.model.Movie;
import com.example.mongodemo.model.SearchItems;
import com.example.mongodemo.repository.MovieRepository;

@Controller
public class WebController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebController.class);
	
	@Value("${welcome.message}")
	private String message;

	@Autowired
	private MovieRepository repository;

	@GetMapping("/")
	public String startHere(Model model) {
		int startYear = 2018;
		int endYear = 2019;

		LOGGER.info("Search by from: {} to: {}", startYear, endYear);
		
		List<Movie> movies = repository.getByReleaseYear(startYear, endYear, new Sort(Sort.Direction.DESC, "releaseYear"));

		model.addAttribute("movies", movies);
		model.addAttribute("searchItems", new SearchItems(startYear, endYear));

		return "welcome"; // view
	}
	
	@PostMapping("/")
	public String searchByYear(Model model, SearchItems searchItem) {
		int startYear = 2018;
		int endYear = 2019;

		if (searchItem != null) {
			startYear = searchItem.getStartYear();
			endYear = searchItem.getEndYear();
		}

		LOGGER.info("Search by from: {} to: {}", startYear, endYear);
		
		List<Movie> movies = repository.getByReleaseYear(startYear, endYear, new Sort(Sort.Direction.DESC, "releaseYear"));

		model.addAttribute("movies", movies);

		return "welcome"; // view
	}
	
	@GetMapping("/showByTitle")
	public String showByTitle(Model model) {
		String title = "Captain";

		LOGGER.info("Search by title: {}", title);
		
		List<Movie> movies = repository.findByTitleLike(title);

		model.addAttribute("movies", movies);
		model.addAttribute("searchItems", new SearchItems(title));

		return "searchTitle"; // view
	}
	
	@PostMapping("/showByTitle")
	public String searchByTitle(Model model, SearchItems searchItem) {
		String title = "";
		if (searchItem != null) {
			title = searchItem.getTitle();
		}

		LOGGER.info("Search by title: {}", title);
		
		List<Movie> movies = repository.findByTitleLike(title);

		model.addAttribute("movies", movies);
		//model.addAttribute("searchItems", searchItem);

		return "searchTitle"; // view
	}

	@GetMapping("/hello")
	public String mainWithParam(@RequestParam(name = "name", required = false, defaultValue = "") String name,
			Model model) {

		model.addAttribute("message", name);

		return "index"; // view
	}
}
