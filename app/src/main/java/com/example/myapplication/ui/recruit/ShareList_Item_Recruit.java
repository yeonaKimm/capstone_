package com.example.myapplication.ui.recruit;

import android.os.Parcel;
import android.os.Parcelable;

public class ShareList_Item_Recruit implements Parcelable {
    private String topic;
    private String content;
    private String place;
    private int quantity;
    private String imageUri;

    public ShareList_Item_Recruit(String topic, String content, String place, int quantity, String imageUri) {
        this.topic = topic;
        this.content = content;
        this.place = place;
        this.quantity = quantity;
        this.imageUri = imageUri;
    }

    protected ShareList_Item_Recruit(Parcel in) {
        topic = in.readString();
        content = in.readString();
        place = in.readString();
        quantity = in.readInt();
        imageUri = in.readString();
    }

    public static final Creator<ShareList_Item_Recruit> CREATOR = new Creator<ShareList_Item_Recruit>() {
        @Override
        public ShareList_Item_Recruit createFromParcel(Parcel in) {
            return new ShareList_Item_Recruit(in);
        }

        @Override
        public ShareList_Item_Recruit[] newArray(int size) {
            return new ShareList_Item_Recruit[size];
        }
    };

    public String getTopic() {
        return topic;
    }

    public String getContent() {
        return content;
    }

    public String getPlace() {
        return place;
    }

    public int getQuantity() {
        return quantity;
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
        dest.writeString(place);
        dest.writeInt(quantity);
        dest.writeString(imageUri);
    }
}
