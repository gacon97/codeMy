package com.example.model;

public class URLjson {
    public  static  final String ip = "http://52.76.86.49/api/";
    public  static  final String rootURL = "http://52.76.86.49/";
    public static final String URL_TRAVLES = ip + "travels";

    public static final String getURLCategoryTravel(String id)
    {
        return ip + "category/" + id + "/travels";
    }
    public static final  String getURLImageTravel(String id)
    {
        return ip + "travel/" + id + "/images";
    }
    public static final String getRootURL()
    {
        return rootURL;
    }
}
