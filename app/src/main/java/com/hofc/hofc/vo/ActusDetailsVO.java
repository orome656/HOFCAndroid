package com.hofc.hofc.vo;

import java.util.Date;

/**
 * Value Object to store Actus Details
 *  - Title
 *  - Date
 *  - Content
 * Created by maladota on 09/12/2014.
 */
public class ActusDetailsVO {

    private String title;
    private Date date;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
