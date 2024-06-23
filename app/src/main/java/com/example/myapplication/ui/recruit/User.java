package com.example.myapplication.ui.recruit;

public class User {
    private String id;
    private String email;
    private String name;
    private String gender;
    private String ageRange;
    private String birthyear;
    private String nickname;
    private String profilePicture;
    private String rankId;

    public User(String id, String email, String name, String gender, String ageRange, String birthyear, String nickname, String profilePicture, String rankId) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.ageRange = ageRange;
        this.birthyear = birthyear;
        this.nickname = nickname;
        this.profilePicture = profilePicture;
        this.rankId = rankId;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public String getBirthyear() {
        return birthyear;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getRankId() {
        return rankId;
    }
}
