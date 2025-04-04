package com.example.upload_file_image;

public class ImageUpload {
    private int id;
    private String username;
    private String avatar;

    // Constructor
    public ImageUpload(int id, String username, String avatar) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar() {
        return avatar;
    }
}

