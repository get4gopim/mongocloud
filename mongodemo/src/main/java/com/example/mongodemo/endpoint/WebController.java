package com.example.mongodemo.endpoint;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.mongodemo.model.Movie;
import com.example.mongodemo.model.SearchItems;
import com.example.mongodemo.repository.MovieRepository;
import com.example.mongodemo.service.MovieRepoService;

@Controller
public class WebController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebController.class);
	
	@Value("${welcome.message}")
	private String message;

	@Autowired
	private MovieRepository repository;
	
	@Autowired
	private MovieRepoService service;

	@GetMapping("/")
	public String startHere(Model model, @PageableDefault(size = 10) Pageable pageable, 
			@RequestParam(value = "startYear", defaultValue = "2018", required = false) int startYear, 
			@RequestParam(value = "endYear", defaultValue = "2019", required = false) int endYear) {

		LOGGER.info("Search by from: {} to: {}", startYear, endYear);
		
		Page<Movie> pages = service.findAllPages(startYear, endYear, pageable);

		model.addAttribute("page", pages);
		model.addAttribute("searchItems", new SearchItems(startYear, endYear));
		model.addAttribute("hints", service.getHints());
		
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
	public String showByTitle(Model model, @PageableDefault(size = 10) Pageable pageable, 
			@RequestParam(value = "title", defaultValue = "Captain", required = false) String title) {

		LOGGER.info("Search by title: {}", title);
		
		Page<Movie> pages = repository.findByTitleLike(title, pageable);

		model.addAttribute("page", pages);
		model.addAttribute("searchItems", new SearchItems(title));
		model.addAttribute("hints", service.getHints());

		return "searchTitle"; // view
	}
	
	@GetMapping("/editItem")
	public String editItem(Model model, 
			@RequestParam(value = "id", required = true) String id) {

		LOGGER.info("Edit by id: {}", id);
		
		Movie movie = repository.findByMovieId(id);

		model.addAttribute("movieItem", movie);

		return "add"; // view
	}
	
	@PostMapping("/add")
	public String saveMovie(Model model, Movie movie) {
		LOGGER.debug("saveMovie: {}", movie.toString());
		
		if (movie.getId() != null) {
			movie = service.saveMovie(movie);
		}

		return "redirect:/";
	}
	
	@GetMapping("/delete")
	public String deleteById(Model model, 
			@PageableDefault(size = 10) Pageable pageable,
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "page", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "size", defaultValue = "10", required = false) int size,
			@RequestParam(value = "startYear", defaultValue = "2018", required = false) int startYear, 
			@RequestParam(value = "endYear", defaultValue = "2019", required = false) int endYear) {

		LOGGER.info("Search by from: {} to: {}", startYear, endYear);
		LOGGER.info("Delete by id: {}", id);
		
		repository.deleteById(id);
		
		String redirectUrl = String.format("page=%d&size=%d&startYear=%d&endYear=%d", pageNumber, size, startYear, endYear);

		LOGGER.info("redirectUrl: /?{}", redirectUrl);
		
		return "redirect:/?" + redirectUrl;
	}

	@GetMapping("/hello")
	public String mainWithParam(@RequestParam(name = "name", required = false, defaultValue = "") String name,
			Model model) {

		model.addAttribute("message", name);

		return "index"; // view
	}
}
