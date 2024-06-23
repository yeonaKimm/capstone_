package com.example.myapplication.ui.recruit;

import android.os.Parcel;
import android.os.Parcelable;

public class BuyList_Item_Recruit implements Parcelable {
    private int id; // id 필드 추가
    private String topic;
    private String content;
    private int price;
    private int people;
    private String imageUri;
    private boolean isClosed;
    private String userId;

    public BuyList_Item_Recruit(int id, String topic, String content, int price, int people, String imageUri, boolean isClosed, String userId) {
        this.id = id;
        this.topic = topic;
        this.content = content;
        this.price = price;
        this.people = people;
        this.imageUri = imageUri;
        this.isClosed = isClosed;
        this.userId = userId;
    }

    protected BuyList_Item_Recruit(Parcel in) {
        id = in.readInt();
        topic = in.readString();
        content = in.readString();
        price = in.readInt();
        people = in.readInt();
        imageUri = in.readString();
        isClosed = in.readByte() != 0;
        userId = in.readString();
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

    public int getId() {
        return id;
    }

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
        return imageUri;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(topic);
        dest.writeString(content);
        dest.writeInt(price);
        dest.writeInt(people);
        dest.writeString(imageUri);
        dest.writeByte((byte) (isClosed ? 1 : 0));
        dest.writeString(userId);
    }

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

        // 추가된 단일 인수 생성자
        public BuyCommentList_Item_Recruit(String promise) {
            this.promise = promise;
        }

        // Getter and Setter methods
    }

}
