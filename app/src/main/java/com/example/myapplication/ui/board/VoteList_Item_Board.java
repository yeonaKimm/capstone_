package com.example.myapplication.ui.board;

import android.os.Parcel;
import android.os.Parcelable;

public class VoteList_Item_Board implements Parcelable {
    private String topic;
    private String content;
    private String imageUri;

    public VoteList_Item_Board(String topic, String content, String imageUri) {
        this.topic = topic;
        this.content = content;
        this.imageUri = imageUri;
    }

    protected VoteList_Item_Board(Parcel in) {
        topic = in.readString();
        content = in.readString();
        imageUri = in.readString();
    }

    public static final Creator<VoteList_Item_Board> CREATOR = new Creator<VoteList_Item_Board>() {
        @Override
        public VoteList_Item_Board createFromParcel(Parcel in) {
            return new VoteList_Item_Board(in);
        }

        @Override
        public VoteList_Item_Board[] newArray(int size) {
            return new VoteList_Item_Board[size];
        }
    };

    public String getTopic() {
        return topic;
    }

    public String getContent() {
        return content;
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
        dest.writeString(imageUri);
    }
}
