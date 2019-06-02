package com.example.model;

public class URLjson {
    public  static  final String ip = "http://3ad4adda.ngrok.io/api/";
    public  static  final String rootURL = "http://3ad4adda.ngrok.io/";
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
