package com.example.myapplication.ui.recruit;

public class BuyCommentList_Item_Recruit {
    private String promise;
    private String account;
    private String bank;
    private String date;
    private String time;
    private String place;
    private String price;
    private String division;
    private String payment;

    public BuyCommentList_Item_Recruit(String promise, String account, String bank, String date, String time, String place, String price, String division, String payment) {
        this.promise = promise;
        this.account = account;
        this.bank = bank;
        this.date = date;
        this.time = time;
        this.place = place;
        this.price = price;
        this.division = division;
        this.payment = payment;
    }

    public String getPromise() {
        return promise;
    }

    public void setPromise(String promise) {
        this.promise = promise;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}
