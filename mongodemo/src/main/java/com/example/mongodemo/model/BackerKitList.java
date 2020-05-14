package com.example.mongodemo.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="BackerKitList")
public class BackerKitList {

    @XmlElement(name="BackerKitNumber")
    private List<BackerKitNumber> data;

    public List<BackerKitNumber> getBackersList() {
        if (this.data == null) data = new ArrayList<>();
        return this.data;
    }

    public void setData(List<BackerKitNumber> data) {
        this.data = data;
    }
}
