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
    private int individualPrice; // 추가된 필드

    public BuyList_Item_Recruit(int id, String topic, String content, int price, int people, String imageUri, boolean isClosed, String userId, long timestamp) {
        this.id = id;
        this.topic = topic;
        this.content = content;
        this.price = price;
        this.people = people;
        this.imageUri = imageUri;
        this.isClosed = isClosed;
        this.userId = userId;
        this.individualPrice = price / (people + 1); // 각자 값을 계산하여 저장
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
        individualPrice = in.readInt(); // 추가된 필드
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

    public int getIndividualPrice() { // 추가된 getter 메서드
        return individualPrice;
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
        dest.writeInt(individualPrice); // 추가된 필드
    }
}
