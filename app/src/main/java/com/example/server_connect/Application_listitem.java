package com.example.server_connect;

public class Application_listitem {

    private String Title;
    private String date;

    private String status;


    public Application_listitem(String title, String date,  String status ) {
        this.Title = title;
        this.date = date;

        this.status=status;

    }

    public String getTitle() {
        return Title;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }


}
