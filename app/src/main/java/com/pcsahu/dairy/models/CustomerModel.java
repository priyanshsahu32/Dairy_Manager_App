package com.pcsahu.dairy.models;

import java.util.ArrayList;
import java.util.Date;

public class CustomerModel {

    private String sellerPn, Name, phNo, qnt,id;
    private Integer rate;
    private Date startDate;
    private ArrayList<Extras> extra;
    private ArrayList<Date> noSupply;

    public CustomerModel(String sellerPn, String Name, String phNo, String qnt, Integer rate, Date startDate, ArrayList<Extras> extra, ArrayList<Date> noSupply,String id) {
        this.sellerPn = sellerPn;
        this.Name = Name;
        this.phNo = phNo;
        this.qnt = qnt;
        this.rate = rate;
        this.startDate = startDate;
        this.extra = extra;
        this.noSupply = noSupply;
        this.id = id;
    }

    public String getSellerPn() {
        return sellerPn;
    }

    public void setSellerPn(String sellerPn) {
        this.sellerPn = sellerPn;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPhNo() {
        return phNo;
    }

    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }

    public String getQnt() {
        return qnt;
    }

    public void setQnt(String qnt) {
        this.qnt = qnt;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public ArrayList<Extras> getExtra() {
        return extra;
    }

    public void setExtra(ArrayList<Extras> extra) {
        this.extra = extra;
    }

    public ArrayList<Date> getNoSupply() {
        return noSupply;
    }

    public void setNoSupply(ArrayList<Date> noSupply) {
        this.noSupply = noSupply;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
