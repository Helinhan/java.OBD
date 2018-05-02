package com.hantong.service.persistence;

public class PersistenceData {
    public PersistenceData() {

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private int type;
    private String value;
}
