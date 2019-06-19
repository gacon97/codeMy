package com.example.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.R;

import java.util.ArrayList;

public class EventDetail extends AppCompatActivity {
    public ArrayList<String> imageURL;
    public int id;
    public String name;
    public String content;
    public String startTime;
    public String endTime;
    public TextView txtName;
    public TextView txtStartTime;
    public TextView txtEndTime;
    public TextView txtContent;
    private RecyclerView viewImageEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        imageURL = intent.getStringArrayListExtra("IMAGE_EVENT");
        name = intent.getStringExtra("NAME_EVENT");
        content = intent.getStringExtra("CONTENT_EVENT");
        startTime = intent.getStringExtra("START_TIME_EVENT");
        endTime = intent.getStringExtra("END_TIME_EVENT");

        viewImageEvent = findViewById(R.id.image_event);
        RecyclerView.LayoutManager layoutCategories = new LinearLayoutManager(getApplicationContext());
        viewImageEvent.setLayoutManager(layoutCategories);

        ImageEventAdapter imageEvent = new ImageEventAdapter(getApplicationContext(), imageURL);
        viewImageEvent.setAdapter(imageEvent);
        txtName = findViewById(R.id.nameEvent);
        txtContent = findViewById(R.id.contentEvent);
        txtStartTime = findViewById(R.id.startTime);
        txtEndTime = findViewById(R.id.endTime);
        txtName.setText(name);
        txtContent.setText(content);
        txtStartTime.setText(startTime);
        txtEndTime.setText(endTime);
    }

}
