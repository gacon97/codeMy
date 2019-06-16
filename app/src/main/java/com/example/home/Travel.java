package com.example.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.R;
import com.example.bottomnavigation.FadePageTransformer;

import org.w3c.dom.Text;

import java.util.ArrayList;
//import com.example.bottomnavigation.R;

public class Travel extends AppCompatActivity {
    private Context mContext;
    public ArrayList<String> imageURL;
    private RecyclerView viewImageTravel;
    private TextView textViewInfo;
    private TextView textViewPlace;
    private TextView textViewName;
    private String info;
    private String place;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        imageURL = intent.getStringArrayListExtra("IMAGE_TRAVEL");
        info = intent.getStringExtra("INFORMATION");
        place = intent.getStringExtra("PLACE");
        name = intent.getStringExtra("NAME");

        viewImageTravel = findViewById(R.id.image_travel);
        RecyclerView.LayoutManager layoutCategories = new LinearLayoutManager(getApplicationContext());
        viewImageTravel.setLayoutManager(layoutCategories);

        ImageTravelAdapter imageTravel = new ImageTravelAdapter(getApplicationContext(), imageURL);
        viewImageTravel.setAdapter(imageTravel);
        textViewInfo = findViewById(R.id.information);
//        textViewPlace = findViewById(R.id.place);
        textViewName = findViewById(R.id.name);
        textViewInfo.setText(info);
//        textViewPlace.setText(place);
        textViewName.setText(name);

    }

}
