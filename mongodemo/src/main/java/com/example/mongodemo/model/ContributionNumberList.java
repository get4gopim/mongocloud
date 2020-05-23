package com.example.mongodemo.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="ContributionNumberList")
public class ContributionNumberList {

    @XmlElement(name="ContributionNumber")
    private List<ContributionNumber> data;

    public List<ContributionNumber> getBackersList() {
        if (this.data == null) data = new ArrayList<>();
        return this.data;
    }

    public void setData(List<ContributionNumber> data) {
        this.data = data;
    }
}
