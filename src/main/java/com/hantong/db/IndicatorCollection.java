package com.hantong.db;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;


@Entity
@Table(name="indicator_collection")
public class IndicatorCollection {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public IndicatorInfo getIndicatorInfo() {
        return indicatorInfo;
    }

    @JsonBackReference
    public void setIndicatorInfo(IndicatorInfo indicatorInfo) {
        this.indicatorInfo = indicatorInfo;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name="value")
    private String value;

    @Column(name="timestamp")
    private Long timestamp;

    @ManyToOne(optional = false)
    private IndicatorInfo  indicatorInfo;
}
