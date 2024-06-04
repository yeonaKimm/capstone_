package com.example.myapplication.ui.safety;

public class Voice_Item_Safety {
    private final String title;
    private final int soundResId;

    public Voice_Item_Safety(String title, int soundResId) {
        this.title = title;
        this.soundResId = soundResId;
    }

    public String getTitle() {
        return title;
    }

    public int getSoundResId() {
        return soundResId;
    }
}
