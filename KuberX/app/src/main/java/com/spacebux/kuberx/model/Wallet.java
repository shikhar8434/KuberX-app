package com.spacebux.kuberx.model;

import java.io.Serializable;

public class Wallet implements Serializable {
    private String title;
    private Double target;
    private Double weeks;
    private Double current;
    private String id;

    public Wallet() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getCurrent() {
        return current;
    }

    public void setCurrent(Double current) {
        this.current = current;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getTarget() {
        return target;
    }

    public void setTarget(Double target) {
        this.target = target;
    }

    public Double getWeeks() {
        return weeks;
    }

    public void setWeeks(Double weeks) {
        this.weeks = weeks;
    }
}
