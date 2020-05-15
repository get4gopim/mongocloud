package com.example.mongodemo.endpoint;

import com.example.mongodemo.model.BackerKitNumber;
import com.example.mongodemo.model.Movie;
import com.example.mongodemo.model.SearchItems;
import com.example.mongodemo.repository.BackerKitNumberRepository;
import com.example.mongodemo.repository.MovieRepository;
import com.example.mongodemo.service.ExcelService;
import com.example.mongodemo.service.MovieRepoService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class WebController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebController.class);
	
	@Value("${welcome.message}")
	private String message;

	@Autowired
	private MovieRepository repository;
	
	@Autowired
	private MovieRepoService service;

	@Autowired
	private BackerKitNumberRepository backerKitNumberRepository;

	@Autowired
	private ExcelService excelService;

    @GetMapping("/mobileView")
    public String mobileView(Model model, @PageableDefault(size = 10) Pageable pageable,
                            @RequestParam(value = "startYear", defaultValue = "2017", required = false) int startYear,
                            @RequestParam(value = "endYear", defaultValue = "2020", required = false) int endYear) {

        LOGGER.debug("Search by from: {} to: {}", startYear, endYear);

        Page<Movie> pages = service.findAllPages(startYear, endYear, pageable);

        model.addAttribute("page", pages);
        model.addAttribute("searchItems", new SearchItems(startYear, endYear));
        model.addAttribute("hints", service.getHints());

        return "mobileView"; // view
    }

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

    @GetMapping("/editMobileView")
    public String editMobileView(Model model,
                           @RequestParam(value = "id", required = true) String id) {

        LOGGER.info("Edit by id: {}", id);

        Movie movie = repository.findByMovieId(id);

        model.addAttribute("movieItem", movie);

        return "addMobileView"; // view
    }

    @PostMapping("/addMobileView")
    public String saveMobileViewMovie(Model model, Movie movie) {
        LOGGER.debug("saveMovie: {}", movie.toString());

        if (movie.getId() != null) {
            movie = service.saveMovie(movie);
        }

        return "redirect:/mobileView";
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

    @GetMapping("/deleteMobileView")
    public String deleteMobileView(Model model,
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

        return "redirect:/mobileView?" + redirectUrl;
    }

	@GetMapping("/bootview")
	public String bootView(Model model) {
		
		return "bootview"; // view
	}
	
	@GetMapping("/index")
	public String indexView(Model model) {
		
		List<Movie> movies = repository.getByReleaseYear(2018, 2019, new Sort(Sort.Direction.DESC, "releaseYear"));

		model.addAttribute("movies", movies);
		
		return "index"; // view
	}

	@GetMapping("/excel")
	public String excelView(Model model, @PageableDefault(size = 300) Pageable pageable,
							@RequestParam(value = "title", defaultValue = "155887", required = false) String backerId) {
		try {
			Page<BackerKitNumber> pages = backerKitNumberRepository.findByBackerNumberLike(backerId, pageable);

			LOGGER.debug("Total Elements: {} - {}", pages.getTotalElements(), pages.getNumberOfElements());

			model.addAttribute("page", pages);
			model.addAttribute("searchItems", new SearchItems(backerId));
			model.addAttribute("hints", excelService.getHints());
		} catch (Exception ex) {
			LOGGER.error("Exception in reading Excel", ex);
		}
		return "excel"; // view
	}

	@GetMapping("/excel/{backerNumber}")
	public String showGuestList(Model model, @PageableDefault(size = 500) Pageable pageable,
								@PathVariable("backerNumber") String backerNumber) {

		model.addAttribute("page", backerKitNumberRepository.findByBackerNumberLike(backerNumber, pageable));
		model.addAttribute("hints", excelService.getHints());

		return "results :: resultsList";
	}
}
