package com.example.myapplication.ui.recruit;

import android.os.Parcel;
import android.os.Parcelable;

public class TaxiList_Item_Recruit implements Parcelable {
    private String date;
    private String time;
    private int people;
    private String startLocation;
    private String endLocation;

    public TaxiList_Item_Recruit(String date, String time, int people, String startLocation, String endLocation) {
        this.date = date;
        this.time = time;
        this.people = people;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    protected TaxiList_Item_Recruit(Parcel in) {
        date = in.readString();
        time = in.readString();
        people = in.readInt();
        startLocation = in.readString();
        endLocation = in.readString();
    }

    public static final Creator<TaxiList_Item_Recruit> CREATOR = new Creator<TaxiList_Item_Recruit>() {
        @Override
        public TaxiList_Item_Recruit createFromParcel(Parcel in) {
            return new TaxiList_Item_Recruit(in);
        }

        @Override
        public TaxiList_Item_Recruit[] newArray(int size) {
            return new TaxiList_Item_Recruit[size];
        }
    };

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getPeople() {
        return people;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(time);
        dest.writeInt(people);
        dest.writeString(startLocation);
        dest.writeString(endLocation);
    }
}
