package com.example.mongodemo.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.mongodemo.model.Movie;
import com.example.mongodemo.model.MovieList;

@Component
public class MovieService {
	
	private static final Logger LOGGER = Logger.getLogger(MovieService.class);
	
	private static final String LOCAL_SAVE_REST_URL = "http://localhost:8080/demo/movies";
	
	@Autowired
	private RestTemplate restTemplate;

	@Async
	public void asyncMethodWithVoidReturnType(String language, int startYear, int endYear) {
		LOGGER.info("Execute method asynchronously: " + Thread.currentThread().getName());
		
		for (int year = startYear; year <= endYear; year++) {
			LOGGER.info ("Year " + year + " process yet to begin...");
			wikiMovieExtractorTask(language, year);
		}
	}
	
	public MovieList wikiMovieExtractorTask(String language, int year) {
		MovieList movieList = null;
		try {
			int tableId = 2;
			if (language.equalsIgnoreCase("tamil")) {
				tableId = 1;
			}
			movieList = listMovieNames(tableId, year, language);
			while (movieList.getMovieList().isEmpty()) {
				tableId++;
				movieList = listMovieNames(tableId, year, language);
				
				if (tableId == 4) {
					break;
				}
			}
		} catch (Exception ex) {
			LOGGER.error("Error in parsing year: " + year, ex);
		}
		return movieList;
	}
	
	
	public MovieList listMovieNames(int tableId, int year, String language) throws Exception {
		LOGGER.debug("listMovieNames = " + tableId + "; year = " + year);
		
		String moviesLinkUrl = "https://en.wikipedia.org/wiki/" + year + "_in_film";
		if (language.equalsIgnoreCase("tamil")) {
			moviesLinkUrl = "https://en.wikipedia.org/wiki/List_of_Tamil_films_of_" + year;
		}		
		Document doc = Jsoup.connect(moviesLinkUrl).timeout(5*1000).get();
		
		Element table = doc.select("div#mw-content-text table").get(tableId);
		Elements rows = table.select("tr");
		
		MovieList movieList = new MovieList();
		
		LOGGER.debug("tableId = " + tableId + "; size = " + rows.size() + "; link: " + moviesLinkUrl);
		
		/*if (rows.size() <= 2) {
			table = doc.select("div#mw-content-text table").get(3);
			rows = table.select("tr");
			LOGGER.debug("revised size = " + rows.size());
		}*/	
		
		LOGGER.debug("rows = " + rows.size());
		
		List<Movie> data = new ArrayList<>();
		for (Element row : rows) {
			Elements link = row.select("td i a");
			String movieLink = link.attr("abs:href");

			if (link.text().length() > 0) {
				LOGGER.debug(link.text() + " (" + movieLink + ") ");
				LOGGER.debug(" ---------------------------------------------------- ");
				
				LOGGER.info ("Movie [" + link.text() + "] extract process yet to begin...");
				Movie movie = wikiMovieInfoTask(movieLink, year);
				data.add(movie);
				
				LOGGER.debug("\n");
			}
		}
		
		movieList.setData(data);
		
		return movieList;
	}
	
	public Movie wikiMovieInfoTask(String movieLink, int year) {
		Movie movie = null;
		try {
			movie = getMovieDetails (movieLink, year);
			LOGGER.info("movie: " + movie);
			saveMovie (movie);
		} catch (Exception ex) {
			LOGGER.error("Error in parsing Movie Info: " + movieLink, ex);
		}
		return movie;
	}
	
	public void saveMovie(Movie movie) {
		try {
			restTemplate.postForLocation(LOCAL_SAVE_REST_URL, movie);
		} catch (Exception e) {
			LOGGER.error("saveMovie error: " + e.getMessage());
		}
	}
	
	private Date getDate(String releaseDate, String format) throws Exception {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(releaseDate);
		} catch (ParseException ex) {
			
		}
		return null;
	}
	
	private int getYear(Date releaseDate) throws Exception {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(releaseDate);
		return calendar.get(Calendar.YEAR);
	}

	public Movie getMovieDetails(String movieLink, int year) throws Exception {
		Movie movie = new Movie();
		Document doc = Jsoup.connect(movieLink).timeout(5*1000).get();
		Element table = doc.select("div#mw-content-text table").get(0);
		
		int i = 0;
		for (Element row : table.select("tr")) {
			Elements th = row.select("th");			
			Elements td = row.select("td");
			
			if (i == 0) {
				LOGGER.debug("Title = " + th.text());
				LOGGER.debug("Year = " + year);
				movie.setReleaseYear(year);
				movie.setTitle(th.text());
			}			
			
			if (i == 1) {
				Elements img = td.select("img");
				LOGGER.debug("imgUrl = " + img.attr("abs:src"));
				movie.setImageUrl(img.attr("abs:src"));
			}
			
			if (th.text().length() > 0 && td.text().length() > 0) {
				
				String director = getContent("Directed by", th, td);
				if (director != null) {
					movie.setFlimDirector( director );
				}
				
				String music= getContent("Music by", th, td);
				if (music != null) {
					movie.setMusicDirector(music);
				}
				
				String language = getContent("Language", th, td);
				if (language != null) {
					movie.setLanguage(language);
				}
				
				//getContent("Release dates", th, td);
				
				getReleaseDate(movie, row, th);
				
				if (th.text().trim().equalsIgnoreCase("Starring")) {
					//LOGGER.debug("*** " + th.text() + " ****");					
					Elements starringDiv = row.select("div ul li");
					if (starringDiv.size() == 0) {
						starringDiv = row.select("a");
					}
					//LOGGER.debug("starringDiv = " + starringDiv);
					
					if (starringDiv.size() >= 2) {
						Element actor = starringDiv.get(0);
						Element actress = starringDiv.get(1);
						
						movie.setActorName(actor.text());
						movie.setActressName(actress.text());
						
						LOGGER.debug("Actor = " + actor.text());
						LOGGER.debug("Actress = " + actress.text());
					}
				} /*else {
					LOGGER.debug(th.text() + " :: " + td.text());
				}*/				
				
			}
			
			i++;
		}
		return movie;
	}

	private void getReleaseDate(Movie movie, Element row, Elements th) {
		if (th.text().trim().equalsIgnoreCase("Release dates")) {
			Elements starringDiv = row.select("div ul li");
			String relDate = "";
			Date dt = null;
			
			//LOGGER.debug("starringDiv = " + starringDiv);
			
			try {
				if (starringDiv != null && !starringDiv.isEmpty()) {
					Element releaseDate = starringDiv.get(0);
					relDate = releaseDate.text();
					relDate = relDate.substring( relDate.indexOf("(")+1, relDate.indexOf(")") );
					dt = getDate (relDate, "yyyy-MM-dd");
				} else {
					starringDiv = row.select("td");
					relDate = starringDiv.text();
					if (relDate.indexOf("(") > 0 && relDate.indexOf(")") > 0) {
						relDate = relDate.substring( relDate.indexOf("(")+1, relDate.indexOf(")") );
						dt = getDate (relDate, "yyyy-MM-dd");
					} else {
						dt = getDate (relDate, "dd MMMM yyyy");
					}
				}
				
				if (dt == null) {
					dt = getDate (relDate, "MMMM dd, yyyy");
				}
				LOGGER.debug("date = " + dt);
				
				if (dt != null) {
					movie.setReleaseDate(dt.getTime());
					movie.setReleaseYear(getYear(dt));
				}
			} catch (Exception ex) {
				LOGGER.error("Release Date Parse Error: ", ex);
			}
			
			LOGGER.debug("releaseDate = " + relDate);
		}
	}

	private String getContent(String header, Elements th, Elements td) {
		String content = null;
		if (th.text().equalsIgnoreCase(header)) {
			LOGGER.debug(th.text() + " = " + td.text());
			content = td.text();
		}
		return content;
	}
}
