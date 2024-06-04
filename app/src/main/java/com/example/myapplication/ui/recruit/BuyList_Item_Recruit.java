package com.example.myapplication.ui.recruit;

import android.os.Parcel;
import android.os.Parcelable;

public class BuyList_Item_Recruit implements Parcelable {
    private String topic;
    //private String content;
    private int price;
    private int people;

    public BuyList_Item_Recruit(String topic, int price, int people) {
        this.topic = topic;
        //this.content = content;
        this.price = price;
        this.people = people;
    }

    protected BuyList_Item_Recruit(Parcel in) {
        topic = in.readString();
        //content = in.readString();
        price = in.readInt();
        people = in.readInt();
    }

    public static final Creator<BuyList_Item_Recruit> CREATOR = new Creator<BuyList_Item_Recruit>() {
        @Override
        public BuyList_Item_Recruit createFromParcel(Parcel in) {
            return new BuyList_Item_Recruit(in);
        }

        @Override
        public BuyList_Item_Recruit[] newArray(int size) {
            return new BuyList_Item_Recruit[size];
        }
    };

    public String getTopic() {
        return topic;
    }

    //public String getContent() {
      //  return content;
    //}

    public int getPrice() {
        return price;
    }

    public int getPeople() {
        return people;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(topic);
        //dest.writeString(content);
        dest.writeInt(price);
        dest.writeInt(people);
    }
}

