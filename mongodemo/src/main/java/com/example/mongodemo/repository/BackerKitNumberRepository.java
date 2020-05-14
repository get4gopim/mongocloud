package com.example.mongodemo.repository;

import com.example.mongodemo.model.BackerKitNumber;
import com.example.mongodemo.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BackerKitNumberRepository extends MongoRepository<BackerKitNumber, String> {

    List<BackerKitNumber> findByBackerNumberLike(String backerNumber);

    Page<BackerKitNumber> findByBackerNumberLike(String backerNumber, Pageable pageable);

    long count();
}
