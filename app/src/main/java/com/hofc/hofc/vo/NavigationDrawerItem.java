package com.hofc.hofc.vo;

/**
 * Created by Fixe on 28/02/2015.
 */
public class NavigationDrawerItem {

    private String title;
    private int imageId;

    public NavigationDrawerItem(String text, int imageId) {
        this.title = text;
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
