package com.example.home;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.R;
import com.example.controller.ReadJsonDataTravel;
import com.example.model.Category;
import com.example.model.Travel;
import com.example.model.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.util.ArrayList;
//import com.example.bottomnavigation.R;

public class CategoryTravel extends AppCompatActivity {
    private RecyclerView viewListCategoryTravels;
    ArrayList<Travel> listTravels;
    private  Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_travel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        int number = intent.getIntExtra(MyAdapter.ID_TRAVEL, 0);
        TextView textView2 = (TextView) findViewById(R.id.id);
        textView2.setText("" + number);

        getTravelList(URL.getURLCategoryTravel(""+number));

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while(true) {
                        sleep(1000);
                        if(listTravels != null)
                        {
                            runView(listTravels);
                            viewListCategoryTravels = findViewById(R.id.cate_travel);
                            RecyclerView.LayoutManager layoutCategories = new LinearLayoutManager(getApplicationContext());
                            viewListCategoryTravels.setLayoutManager(layoutCategories);

                            CategoryTravelAdapter categoryTravelAdapter = new CategoryTravelAdapter(getApplicationContext(), listTravels);
                            viewListCategoryTravels.setAdapter(categoryTravelAdapter);
                            this.destroy();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();

    }

    public void runView(ArrayList<Travel> listTravels)
    {
        Log.d("DATA-size------", ""+listTravels.size());
    }
    public void getTravelList(String url) {
        ReadJsonDataTravel readJson = new ReadJsonDataTravel();
        readJson.execute(url);

    }

    class ReadJsonDataTravel extends AsyncTask<String, Void, String> {

//        public ArrayList<Travel> travelList = new ArrayList<>();

        @Override
        public String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();
            try {
                java.net.URL url = new java.net.URL(strings[0]);
                Log.d("ReadJson", "URL done");
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
            Log.d("ReadJson", "Chay load");
            Log.d("ReadJson", s);
            listTravels = new ArrayList<>();


            try {
                JSONObject obj = new JSONObject(s);
                JSONArray array = obj.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    int id = array.getJSONObject(i).getInt("id");
                    String name = array.getJSONObject(i).getString("name");
                    String place = array.getJSONObject(i).getString("place");
                    String feature = array.getJSONObject(i).getString("feature");
                    String category_id = array.getJSONObject(i).getString("category_id");
                    String lat = array.getJSONObject(i).getString("lat");
                    String lng = array.getJSONObject(i).getString("lng");
                    String created_at = array.getJSONObject(i).getString("created_at");
                    String updated_at = array.getJSONObject(i).getString("updated_at");
                    String deleted_at = array.getJSONObject(i).getString("deleted_at");

                    Travel travel = new Travel(id, name, place, feature, category_id, Double.parseDouble(lat), Double.parseDouble(lng), created_at, updated_at, deleted_at);
                    listTravels.add(travel);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
//            CategoryTravel categoryTravel = new CategoryTravel();
//            categoryTravel.runView(travelList);
        }

    }
}
