package com.example.controller;

import android.os.AsyncTask;
import android.util.Log;

import com.example.model.Event;
import com.example.model.Travel;
import com.example.model.URLjson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class ReadJsonImageEvent extends AsyncTask<String, Void, String> {
    private Event event;

    public ReadJsonImageEvent(Event t)
    {
        this.event = t;
    }

    @Override
    protected String doInBackground(String... strings) {
        StringBuilder content = new StringBuilder();
        try {
            java.net.URL url = new URL(strings[0]);
            Log.d("ReadJson", "URLjson done");
            InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
            bufferedReader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    @Override
    public void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("image event", "Chay load");
        Log.e("image event", s);

        try {
            JSONObject obj = new JSONObject(s);
            JSONArray array = obj.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                String urlImage = URLjson.getRootURL() + array.getJSONObject(i).getString("image");
//                Log.e("image event", "--------------------------");
//                Log.e("image event", urlImage);
//                Log.e("image event", event.getName());
                event.addUrlImage(urlImage);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
