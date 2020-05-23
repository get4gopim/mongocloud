package com.example.mongodemo.repository;

import com.example.mongodemo.model.BackerKitNumber;
import com.example.mongodemo.model.ContributionNumber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContributionNumberRepository extends MongoRepository<ContributionNumber, String> {

    List<ContributionNumber> findByContributionNumberStartsWith(String contributionNumber);

    Page<ContributionNumber> findByContributionNumberStartsWith(String contributionNumber, Pageable pageable);

    long count();
}
