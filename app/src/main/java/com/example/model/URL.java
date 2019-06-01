package com.example.model;

public class URL {
    public  static  final String ip = "http://08a7743b.ngrok.io/api/";
    public static final String URL_TRAVLES = ip + "travels";

    public static final String getURLCategoryTravel(String id)
    {
        return ip + "category/" + id + "/travels";
    }
}
