package com.example.mongodemo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "backer_kit_number")
public class BackerKitNumber {

    @Id
    private String id;

    private String backerNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBackerNumber() {
        return backerNumber;
    }

    public void setBackerNumber(String backerNumber) {
        this.backerNumber = backerNumber;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BackerKitNumber{");
        sb.append("id='").append(id).append('\'');
        sb.append(", backerNumber='").append(backerNumber).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
