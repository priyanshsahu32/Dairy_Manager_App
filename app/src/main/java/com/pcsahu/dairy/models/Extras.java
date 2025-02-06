package com.pcsahu.dairy.models;

import java.util.Date;

public class Extras {

    private String ExtraQnt;
    private Date ExtraDate;

    public Extras(String qnt, Date date) {
        this.ExtraQnt = qnt;
        this.ExtraDate = date;
    }

    public String getExtraQnt() {
        return ExtraQnt;
    }

    public void setExtraQnt(String extraQnt) {
        this.ExtraQnt = extraQnt;
    }

    public Date getExtraDate() {
        return ExtraDate;
    }

    public void setExtraDate(Date extraDate) {
        this.ExtraDate = extraDate;
    }
}
