package com.example.server_connect;

public class ListItem {

    private String head;
    private String desc;

    private String em;
    private String imageUrl;
    private String id;
    private String bio;

    public ListItem(String head, String desc, String imageUrl, String em,String id,  String bio ) {
        this.head = head;
        this.desc = desc;

        this.em=em;
        this.imageUrl=imageUrl;
        this.id=id;
        this.bio=bio;
    }

    public String getHead() {
        return head;
    }

    public String getDesc() {
        return desc;
    }

    public String getEm() {
        return em;
    }

    public String getid() {
        return id;
    }

    public String getBio() {
        return bio;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
