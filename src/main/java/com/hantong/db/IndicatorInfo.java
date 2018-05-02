package com.hantong.db;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name="indicator_info")
public class IndicatorInfo {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="device")
    private String device;


    public Set<IndicatorCollection> getIndicatorCollections() {
        if (null == indicatorCollections) {
            indicatorCollections = new HashSet<>();
        }
        return indicatorCollections;
    }

    public void setIndicatorCollections(Set<IndicatorCollection> indicatorCollections) {
        this.indicatorCollections = indicatorCollections;
    }

    @OneToMany(mappedBy = "indicatorInfo",fetch = FetchType.LAZY)
    private Set<IndicatorCollection> indicatorCollections;
}
