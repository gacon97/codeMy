package com.example.home;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.HttpResponse;
import com.example.R;
import com.example.bottomnavigation.FadePageTransformer;
import com.example.bottomnavigation.ItemClickListener;
import com.example.controller.RequestHandler;
import com.example.model.URLjson;
import com.example.travelevent.Constants;
import com.google.android.gms.ads.internal.gmsg.HttpClient;
import com.squareup.picasso.Request;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
//import com.example.bottomnavigation.R;

public class Travel extends AppCompatActivity{
    public Context mContext;
    public ArrayList<String> imageURL;
    private RecyclerView viewImageTravel;
    private TextView textViewInfo;
    private TextView textViewPlace;
    private TextView textViewName;
    private String info;
    private String place;
    private String name;
    private int id;
    private Button buttonChoose;
    private ImageView choose;
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
        id = (int) intent.getIntExtra("ID",0);
        Log.e("ID_TRAVEL_IMAGE---", ""+id);

        viewImageTravel = findViewById(R.id.image_travel);
        RecyclerView.LayoutManager layoutCategories = new LinearLayoutManager(getApplicationContext());
        viewImageTravel.setLayoutManager(layoutCategories);

        ImageTravelAdapter imageTravel = new ImageTravelAdapter(getApplicationContext(), imageURL);
        viewImageTravel.setAdapter(imageTravel);
        textViewInfo = findViewById(R.id.information);
        textViewName = findViewById(R.id.name);
        textViewInfo.setText(info);
        textViewName.setText(name);

        choose = findViewById(R.id.choose);
        choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent intent = new Intent(Travel.this, Upload.class);
                intent.putExtra("ID_TRAVEL_IMAGE", id);
//                Log.e("ID_TRAVEL_IMAGE", id);
                startActivity(intent);
            }
        });

    }
}
