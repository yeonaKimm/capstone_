package com.example.myapplication.ui.recruit;

public class TaxiList_Item_Recruit {
    private String date;
    private String time;
    private int people;

    public TaxiList_Item_Recruit(String date, String time, int people) {
        this.date = date;
        this.time = time;
        this.people = people;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getPeople() {
        return people;
    }

}
