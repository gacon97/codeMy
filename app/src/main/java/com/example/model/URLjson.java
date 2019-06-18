package com.example.model;

import android.util.Log;

public class URLjson {
    public  static  final String ip = "http://2f428630.ngrok.io/api/";
    public  static  final String rootURL = "http://2f428630.ngrok.io/";
    public static final String URL_TRAVLES = ip + "travels";
    public static final String URL_EVENT = ip + "events";
    public static final String getURLCategoryTravel(String id)
    {
        return ip + "category/" + id + "/travels";
    }
    public static final  String getURLImageTravel(String id)
    {
        return ip + "travel/" + id + "/images";
    }
    public static final  String getURLSearch(String keyWord)
    {

        return ip + "search/" + keyWord;
    }
    public static final String getRootURL()
    {
        return rootURL;
    }
}
