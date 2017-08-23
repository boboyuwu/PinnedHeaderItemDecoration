package com.pipikou.simple;

/**
 * Created by boboyuwu on 2017/8/22.
 */

public class SimpleBean {

    public static final int TYPE_TITLE=1;
    public static final int TYPE_NROMAL=2;
    public static final int TYPE_HEADER=3;

    private int type;
    private String title;
    private String imgUrl;
    private String text;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
