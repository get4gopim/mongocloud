package com.example.mongodemo.repository;

import com.example.mongodemo.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieNonBlockingRepository extends ReactiveCrudRepository<Movie, String>, CustomMovieRepository {

    Flux<Movie> findByReleaseYearBetween(int startYear, int endYear);

    @Query("{'releaseYear' : { $gte: ?0, $lte: ?1 } }")
    Flux<Movie> getByReleaseYear(int startYear, int endYear, Sort sort);

    Flux<Movie> findByTitleLike(String title);

    @Query("{'id' : ?0 }")
    Mono<Movie> findByMovieId(String title);

    Mono<Long> count();

    @Query("{'language' : ?0 }")
    Flux<String> findDistinctByLanguage(String language);

    @Query("{'releaseYear' : { $gte: ?0, $lte: ?1 } }")
    Flux<Movie> getByReleaseYearPagable(int startYear, int endYear, Pageable pageable);

    Flux<Movie> findByTitleLike(String title, Pageable pageable);

    @Query("{'language' : ?0 }")
    Flux<String> findDistinctLanguage1(String language);
}
