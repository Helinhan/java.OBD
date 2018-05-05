package com.hantong.model;

import java.util.List;

public class ServiceConfigField {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }
    public List<ServiceConfigField> getParam() {
        return param;
    }

    public void setParam(List<ServiceConfigField> param) {
        this.param = param;
    }

    private String  name;
    private String  title;
    private String  description;
    private String  range;
    private List<ServiceConfigField> param;
}
