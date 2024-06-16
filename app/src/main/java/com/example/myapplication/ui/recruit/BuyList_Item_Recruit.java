package com.example.myapplication.ui.recruit;

import android.os.Parcel;
import android.os.Parcelable;

public class BuyList_Item_Recruit implements Parcelable {
    private String topic;
    private String content;
    private int price;
    private int people;
    private String imageUri;

    public BuyList_Item_Recruit(String topic, String content, int price, int people, String imageUri) {
        this.topic = topic;
        this.content = content;
        this.price = price;
        this.people = people;
        this.imageUri = imageUri;
    }

    protected BuyList_Item_Recruit(Parcel in) {
        topic = in.readString();
        content = in.readString();
        price = in.readInt();
        people = in.readInt();
        imageUri = in.readString();
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

    public String getContent() {
        return content;
    }

    public int getPrice() {
        return price;
    }

    public int getPeople() {
        return people;
    }
    public String getImageUri() {
        return this.imageUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(topic);
        dest.writeString(content);
        dest.writeInt(price);
        dest.writeInt(people);
        dest.writeString(imageUri);
    }
}
