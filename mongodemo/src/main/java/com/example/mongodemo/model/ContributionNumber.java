package com.example.mongodemo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "contribution_number")
public class ContributionNumber {

    @Id
    private String id;

    private String contributionNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContributionNumber() {
        return contributionNumber;
    }

    public void setContributionNumber(String contributionNumber) {
        this.contributionNumber = contributionNumber;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ContributionNumber{");
        sb.append("id='").append(id).append('\'');
        sb.append(", contributionNumber='").append(contributionNumber).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
