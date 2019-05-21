package com.example.bottomnavigation;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.R;
//import com.example.travelevent.CompassActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private int idevent;
    private GoogleMap mMap;
    private UiSettings mUiSettings;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int CHOOSE_IMAGE = 101;
    private static final String TAG = "MapsActivity";
    int in_out = 0;
    private FusedLocationProviderClient fusedLocationClient;
    private ArrayList<Travel> travelList;
    private String URL = "http://3.82.158.167/api/travels";
    private FloatingActionButton discover_fab, fab_one, fab_two, fab_three, fab_four, fab_five;
    private Button clearButton, login_logout;
    private TextView text_fab_1, text_fab_2, text_fab_3, text_fab_4, text_fab_5;
    private TextView title_travel, title_comment, text_like;
    private BottomSheetBehavior bottomSheetBehavior;
    private Marker markerClicked;
    private ImageView like_image, upload_image, imageUpload1, imageUpload2, imageUpload3, imageUpload4, imageUpload5;
    Animation move_down_f1, move_down_f2, move_down_f3, move_down_f4, move_down_f5;
    Animation move_back_f1, move_back_f2, move_back_f3, move_back_f4, move_back_f5;
    private boolean discoverButton = false;
    Uri uriProfileImage;
    String profileImageUrl;
    private FirebaseAuth mAuth;
    private int current_id_travel;
    private int count_image = 1;
    private int like = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
//        Bundle bundle = this.getIntent().getExtras();
//        if (bundle != null) {
//            user = (User) bundle.getSerializable("user");
//        }
        idevent=this.getIntent().getIntExtra("travelid",0);
        getPermissionLocation();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null)
        {
            in_out=1;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Map is not ready! Access your permission", Toast.LENGTH_SHORT).show();
        } else {
            loadMap();
            prepareElementView();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        initMap();
    }

    public void initMap() {
        mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                this, R.raw.map_custom_style));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                collapseBottomSheet();
                markerClicked = marker;
                count_image=1;
                return false;
            }
        });
    }



    public void addMarker(LatLng location, String str) {
        mMap.addMarker(new MarkerOptions().position(location).title(str));
    }

    public void getPermissionLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
    }

    public void loadMapStyle(int i) {
        mMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                this, i));
    }


    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    public void loadMap() {
        Toast.makeText(this, "Map is ready!", Toast.LENGTH_SHORT).show();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadMap();
                } else {
                    Toast.makeText(this, "Map is not ready! Access your permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void getCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Map is not ready! Access your permission", Toast.LENGTH_SHORT).show();
        } else {
            fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Geocoder geocoder = new Geocoder(getApplicationContext());
                            try {
                                List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                String str = addressList.get(0).getSubLocality() + ", ";
                                str += addressList.get(0).getCountryName();
                                LatLng marker = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 13));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        }
    }

    public void getTravelList(String url) {
        ReadJsonDataTravel readJson = new ReadJsonDataTravel();
        readJson.execute(url);
    }

    public class ReadJsonDataTravel extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();
            try {
                java.net.URL url = new URL(strings[0]);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("ReadJson", "Chay load");
            Log.d("ReadJson", s);
            travelList = new ArrayList<>();

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
                    travelList.add(travel);
                }
                for (int i = 0; i < travelList.size(); i++) {
                    Log.d("ReadJson", "--------------------------");
                    Log.d("ReadJson", travelList.get(i).getName());
                    LatLng location = new LatLng(travelList.get(i).getLat(), travelList.get(i).getLng());
                    addMarker(location, travelList.get(i).getName());

                    ReadJsonImageTravel readImageJson = new ReadJsonImageTravel(travelList.get(i));
                    readImageJson.execute("http://3.82.158.167/api/travel/" + travelList.get(i).getId() + "/images");
                    ReadJsonEventTravel readEventJson = new ReadJsonEventTravel(travelList.get(i));
                    readEventJson.execute("http://3.82.158.167/api/events/" + travelList.get(i).getId());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class ReadJsonImageTravel extends AsyncTask<String, Void, String> {
        private Travel t;

        public ReadJsonImageTravel(Travel t) {
            this.t = t;
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();
            try {
                java.net.URL url = new URL(strings[0]);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("ReadJson", "Chay load");
            Log.d("ReadJson", s);

            try {
                JSONObject obj = new JSONObject(s);
                JSONArray array = obj.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    String urlImage = "http://3.82.158.167" + array.getJSONObject(i).getString("image");
                    Log.d("ReadJson", "--------------------------");
                    Log.d("ReadJson", urlImage);
                    Log.d("ReadJson", t.getName());
                    t.addUrlImage(urlImage);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class ReadJsonEventTravel extends AsyncTask<String, Void, String> {
        private Travel t;

        public ReadJsonEventTravel(Travel t) {
            this.t = t;
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();
            try {
                java.net.URL url = new URL(strings[0]);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("ReadJson", "Chay load");
            Log.d("ReadJson", s);

            try {
                JSONObject obj = new JSONObject(s);
                JSONObject o = obj.getJSONObject("data");


                String name = o.getString("name");
                String topic = o.getString("topic");
                String start_time = o.getString("start_time");
                String end_time = o.getString("end_time");
                String content = o.getString("content");

                t.setContent_envent(content);
                t.setEnd_time_event(end_time);
                t.setStart_time_event(start_time);
                t.setTop_pic_event(topic);
                t.setName_event(name);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void setElementView() {
        discover_fab = (FloatingActionButton) findViewById(R.id.fab);
        fab_one = (FloatingActionButton) findViewById(R.id.fab_one);
        fab_two = (FloatingActionButton) findViewById(R.id.fab_two);
        fab_three = (FloatingActionButton) findViewById(R.id.fab_three);
        fab_four = (FloatingActionButton) findViewById(R.id.fab_four);
        fab_five = (FloatingActionButton) findViewById(R.id.fab_five);

        imageUpload1 = (ImageView) findViewById(R.id.image_upload1);
        imageUpload2 = (ImageView) findViewById(R.id.image_upload2);
        imageUpload3 = (ImageView) findViewById(R.id.image_upload3);
        imageUpload4 = (ImageView) findViewById(R.id.image_upload4);
        imageUpload5 = (ImageView) findViewById(R.id.image_upload5);

        text_fab_1 = (TextView) findViewById(R.id.text_fab_one);
        text_fab_2 = (TextView) findViewById(R.id.text_fab_two);
        text_fab_3 = (TextView) findViewById(R.id.text_fab_three);
        text_fab_4 = (TextView) findViewById(R.id.text_fab_four);
        text_fab_5 = (TextView) findViewById(R.id.text_fab_five);
        text_like = (TextView) findViewById(R.id.text_like);

        like_image = (ImageView) findViewById(R.id.imageLike);
        upload_image = (ImageView) findViewById(R.id.image_post);

        title_travel = (TextView) findViewById(R.id.title_travel);
        title_comment = (TextView) findViewById(R.id.title_comment);

        clearButton = (Button) findViewById(R.id.clearButton);
        login_logout = (Button) findViewById(R.id.login_logout);
        if (in_out ==0)
        {
            login_logout.setText("Login");
        }
        else
        {
            login_logout.setText("Logout");
        }

        View bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
    }

    public void prepareEventElement() {
        discover_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (discoverButton == false) {
                    discoverButton = true;
                    turnOnFab();
                    moveDownParamsFab();
                    showTextFab();
                } else {
                    discoverButton = false;
                    moveBackParamsFab();
                    hideFab();
                }
            }
        });

        fab_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapsActivity.this, "Đang tải dữ liệu du lịch", Toast.LENGTH_SHORT).show();
                getTravelList(URL);
            }
        });

        fab_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapsActivity.this, "Đang lấy vị trí gps hien tai", Toast.LENGTH_SHORT).show();
                loadMapStyle(R.raw.map_bus_style);
//                ReadJsonNhung nhung = new ReadJsonNhung();
//                nhung.execute("http://3.82.158.167/api/nhung/place");
            }
        });

        fab_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapsActivity.this, "Đang lấy vị trí bệnh viện", Toast.LENGTH_SHORT).show();
                loadMapStyle(R.raw.map_medical_style);
            }
        });

        fab_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapsActivity.this, "Đang lấy vị trí trường học", Toast.LENGTH_SHORT).show();
                loadMapStyle(R.raw.map_school_style);
            }
        });

        fab_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapsActivity.this, "Đang lấy vị trí cơ quan chính phủ", Toast.LENGTH_SHORT).show();
                loadMapStyle(R.raw.map_goverment_style);
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearMap();
            }
        });

        login_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (in_out == 0)
                {
                    Intent intent = new Intent(MapsActivity.this, LoginHomeActivity.class);
                    startActivity(intent);
                }
                else
                {
                    FirebaseAuth.getInstance().signOut();
                    in_out = 0;
                    login_logout.setText("Login");
                    Toast.makeText(MapsActivity.this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                }
            }
        });

        title_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser()==null) {
                    Intent intent = new Intent(MapsActivity.this, LoginHomeActivity.class);
                    startActivity(intent);
                }
                else
                {
                    showImageChooser();
                }
            }
        });

        like_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (like == 0)
                {
                    like =1;
                    like_image.setImageResource(R.drawable.ic_thumb_up_blue_24dp);
                    text_like.setText("1");
                }
                else {
                    like =0;
                    like_image.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                    text_like.setText("0");
                }
            }
        });

        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {   //User use
                switch (i) {
                    case BottomSheetBehavior.STATE_COLLAPSED: //Chua mo rong
                        title_travel.setText("Xem chi tiết");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:  //Dang mo rong
                        title_travel.setText(markerClicked.getTitle());
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:  //Da mo rong
                        ImageView imageTravel1 = (ImageView) findViewById(R.id.imageTravel1);
                        ImageView imageTravel2 = (ImageView) findViewById(R.id.imageTravel2);
                        ImageView imageTravel3 = (ImageView) findViewById(R.id.imageTravel3);
                        ImageView imageTravel4 = (ImageView) findViewById(R.id.imageTravel4);
                        ImageView imageTravel5 = (ImageView) findViewById(R.id.imageTravel5);

                        TextView discrible_text = (TextView) findViewById(R.id.discrible_travel);
                        TextView discrible_event = (TextView) findViewById(R.id.event_content);
                        TextView start_time = (TextView) findViewById(R.id.event_start_time);
                        TextView end_time = (TextView) findViewById(R.id.event_end_time);
                        for (int n = 0; n < travelList.size(); n++) {
                            if (markerClicked.getTitle().equals(travelList.get(n).getName()) == true) {
                                Glide.
                                    with(MapsActivity.this)
                                    .load(travelList.get(n).getImageUrl().get(0))
                                    .into(imageTravel1);
                                Glide.
                                    with(MapsActivity.this)
                                    .load(travelList.get(n).getImageUrl().get(1))
                                    .into(imageTravel2);
                                Glide.
                                    with(MapsActivity.this)
                                    .load(travelList.get(n).getImageUrl().get(2))
                                    .into(imageTravel3);
                                Glide.
                                    with(MapsActivity.this)
                                    .load(travelList.get(n).getImageUrl().get(3))
                                    .into(imageTravel4);
                                Glide.
                                    with(MapsActivity.this)
                                    .load(travelList.get(n).getImageUrl().get(4))
                                    .into(imageTravel5);
                                discrible_text.setText(travelList.get(n).getFeature());

                                discrible_event.setText(travelList.get(n).getContent_envent());
                                start_time.setText("Thời gian bắt đầu: " + travelList.get(n).getStart_time_event());
                                end_time.setText("Thời gian kết thúc: " + travelList.get(n).getEnd_time_event());
                                current_id_travel = travelList.get(n).getId();
                                GetImageUpload(current_id_travel+"/"+1+".jpg", imageUpload1);
                                GetImageUpload(current_id_travel+"/"+2+".jpg", imageUpload2);
                                GetImageUpload(current_id_travel+"/"+3+".jpg", imageUpload3);
                                GetImageUpload(current_id_travel+"/"+4+".jpg", imageUpload4);
                                GetImageUpload(current_id_travel+"/"+5+".jpg", imageUpload5);
                                break;
                            }
                        }
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:    //An
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:  //Giao diem Dang mo rong -> chua da mo rong
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {    //Event use

            }
        });
    }

    public void prepareElementView() {
        setElementView();
        setAnimation();
        prepareEventElement();
        hideFab();
        hideBottomSheet();
    }

    public void hideFab() {
        fab_one.hide();
        fab_two.hide();
        fab_three.hide();
        fab_four.hide();
        fab_five.hide();

        text_fab_1.setVisibility(text_fab_1.INVISIBLE);
        text_fab_2.setVisibility(text_fab_2.INVISIBLE);
        text_fab_3.setVisibility(text_fab_3.INVISIBLE);
        text_fab_4.setVisibility(text_fab_4.INVISIBLE);
        text_fab_5.setVisibility(text_fab_5.INVISIBLE);
    }

    public void turnOnFab() {
        fab_one.show();
        fab_two.show();
        fab_three.show();
        fab_four.show();
        fab_five.show();
    }

    public void showTextFab() {
        text_fab_1.setVisibility(text_fab_1.VISIBLE);
        text_fab_2.setVisibility(text_fab_2.VISIBLE);
        text_fab_3.setVisibility(text_fab_3.VISIBLE);
        text_fab_4.setVisibility(text_fab_4.VISIBLE);
        text_fab_5.setVisibility(text_fab_5.VISIBLE);
    }

    public void setAnimation() {
        move_down_f1 = AnimationUtils.loadAnimation(this, R.anim.move_down_f1);
        move_down_f2 = AnimationUtils.loadAnimation(this, R.anim.move_down_f2);
        move_down_f3 = AnimationUtils.loadAnimation(this, R.anim.move_down_f3);
        move_down_f4 = AnimationUtils.loadAnimation(this, R.anim.move_down_f4);
        move_down_f5 = AnimationUtils.loadAnimation(this, R.anim.move_down_f5);

        move_back_f1 = AnimationUtils.loadAnimation(this, R.anim.move_back_f1);
        move_back_f2 = AnimationUtils.loadAnimation(this, R.anim.move_back_f2);
        move_back_f3 = AnimationUtils.loadAnimation(this, R.anim.move_back_f3);
        move_back_f4 = AnimationUtils.loadAnimation(this, R.anim.move_back_f4);
        move_back_f5 = AnimationUtils.loadAnimation(this, R.anim.move_back_f5);
    }

    public void moveDownParamsFab() {
        FrameLayout.LayoutParams paramsFabOne = (FrameLayout.LayoutParams) fab_one.getLayoutParams();
        paramsFabOne.topMargin = (int) (discover_fab.getWidth() * 1.5);
        fab_one.setLayoutParams(paramsFabOne);
        fab_one.startAnimation(move_down_f1);

        FrameLayout.LayoutParams paramsFabTwo = (FrameLayout.LayoutParams) fab_two.getLayoutParams();
        paramsFabTwo.topMargin = (int) (discover_fab.getWidth() * 1.5 * 2);
        fab_two.setLayoutParams(paramsFabTwo);
        fab_two.startAnimation(move_down_f2);

        FrameLayout.LayoutParams paramsFabThree = (FrameLayout.LayoutParams) fab_three.getLayoutParams();
        paramsFabThree.topMargin = (int) (discover_fab.getWidth() * 1.5 * 3);
        fab_three.setLayoutParams(paramsFabThree);
        fab_three.startAnimation(move_down_f3);

        FrameLayout.LayoutParams paramsFabFour = (FrameLayout.LayoutParams) fab_four.getLayoutParams();
        paramsFabFour.topMargin = (int) (discover_fab.getWidth() * 1.5 * 4);
        fab_four.setLayoutParams(paramsFabFour);
        fab_four.startAnimation(move_down_f4);

        FrameLayout.LayoutParams paramsFabFive = (FrameLayout.LayoutParams) fab_five.getLayoutParams();
        paramsFabFive.topMargin = (int) (discover_fab.getWidth() * 1.5 * 5);
        fab_five.setLayoutParams(paramsFabFive);
        fab_five.startAnimation(move_down_f5);

        FrameLayout.LayoutParams paramsTextFabOne = (FrameLayout.LayoutParams) text_fab_1.getLayoutParams();
        paramsTextFabOne.topMargin = (int) (discover_fab.getWidth() * 1.5);
        text_fab_1.setLayoutParams(paramsTextFabOne);
        text_fab_1.startAnimation(move_down_f1);

        FrameLayout.LayoutParams paramsTextFabTwo = (FrameLayout.LayoutParams) text_fab_2.getLayoutParams();
        paramsTextFabTwo.topMargin = (int) (discover_fab.getWidth() * 1.5 * 2);
        text_fab_2.setLayoutParams(paramsTextFabTwo);
        text_fab_2.startAnimation(move_down_f2);

        FrameLayout.LayoutParams paramsTextFabThree = (FrameLayout.LayoutParams) text_fab_3.getLayoutParams();
        paramsTextFabThree.topMargin = (int) (discover_fab.getWidth() * 1.5 * 3);
        text_fab_3.setLayoutParams(paramsTextFabThree);
        text_fab_3.startAnimation(move_down_f3);

        FrameLayout.LayoutParams paramsTextFabFour = (FrameLayout.LayoutParams) text_fab_4.getLayoutParams();
        paramsTextFabFour.topMargin = (int) (discover_fab.getWidth() * 1.5 * 4);
        text_fab_4.setLayoutParams(paramsTextFabFour);
        text_fab_4.startAnimation(move_down_f4);

        FrameLayout.LayoutParams paramsTextFabFive = (FrameLayout.LayoutParams) text_fab_5.getLayoutParams();
        paramsTextFabFive.topMargin = (int) (discover_fab.getWidth() * 1.5 * 5);
        text_fab_5.setLayoutParams(paramsTextFabFive);
        text_fab_5.startAnimation(move_down_f5);
    }

    public void moveBackParamsFab() {
        FrameLayout.LayoutParams paramsFabOne = (FrameLayout.LayoutParams) fab_one.getLayoutParams();
        paramsFabOne.topMargin -= (int) (discover_fab.getWidth() * 1.5);
        fab_one.setLayoutParams(paramsFabOne);
        fab_one.startAnimation(move_back_f1);

        FrameLayout.LayoutParams paramsFabTwo = (FrameLayout.LayoutParams) fab_two.getLayoutParams();
        paramsFabTwo.topMargin = -(int) (discover_fab.getWidth() * 1.5 * 2);
        fab_two.setLayoutParams(paramsFabTwo);
        fab_two.startAnimation(move_back_f2);

        FrameLayout.LayoutParams paramsFabThree = (FrameLayout.LayoutParams) fab_three.getLayoutParams();
        paramsFabThree.topMargin = -(int) (discover_fab.getWidth() * 1.5 * 3);
        fab_three.setLayoutParams(paramsFabThree);
        fab_three.startAnimation(move_back_f3);

        FrameLayout.LayoutParams paramsFabFour = (FrameLayout.LayoutParams) fab_four.getLayoutParams();
        paramsFabFour.topMargin = -(int) (discover_fab.getWidth() * 1.5 * 4);
        fab_four.setLayoutParams(paramsFabFour);
        fab_four.startAnimation(move_back_f4);

        FrameLayout.LayoutParams paramsFabFive = (FrameLayout.LayoutParams) fab_five.getLayoutParams();
        paramsFabFive.topMargin = -(int) (discover_fab.getWidth() * 1.5 * 5);
        fab_five.setLayoutParams(paramsFabFive);
        fab_five.startAnimation(move_back_f5);

        FrameLayout.LayoutParams paramsTextFabOne = (FrameLayout.LayoutParams) text_fab_1.getLayoutParams();
        paramsTextFabOne.topMargin = -(int) (discover_fab.getWidth() * 1.5);
        text_fab_1.setLayoutParams(paramsTextFabOne);
        text_fab_1.startAnimation(move_back_f1);

        FrameLayout.LayoutParams paramsTextFabTwo = (FrameLayout.LayoutParams) text_fab_2.getLayoutParams();
        paramsTextFabTwo.topMargin = -(int) (discover_fab.getWidth() * 1.5 * 2);
        text_fab_2.setLayoutParams(paramsTextFabTwo);
        text_fab_2.startAnimation(move_back_f2);

        FrameLayout.LayoutParams paramsTextFabThree = (FrameLayout.LayoutParams) text_fab_3.getLayoutParams();
        paramsTextFabThree.topMargin = -(int) (discover_fab.getWidth() * 1.5 * 3);
        text_fab_3.setLayoutParams(paramsTextFabThree);
        text_fab_3.startAnimation(move_back_f3);

        FrameLayout.LayoutParams paramsTextFabFour = (FrameLayout.LayoutParams) text_fab_4.getLayoutParams();
        paramsTextFabFour.topMargin = -(int) (discover_fab.getWidth() * 1.5 * 4);
        text_fab_4.setLayoutParams(paramsTextFabFour);
        text_fab_4.startAnimation(move_back_f4);

        FrameLayout.LayoutParams paramsTextFabFive = (FrameLayout.LayoutParams) text_fab_5.getLayoutParams();
        paramsTextFabFive.topMargin = -(int) (discover_fab.getWidth() * 1.5 * 5);
        text_fab_5.setLayoutParams(paramsTextFabFive);
        text_fab_5.startAnimation(move_back_f5);
    }

    public void clearMap() {
        mMap.clear();
        loadMapStyle(R.raw.map_custom_style);
    }

    public void expandBottomSheet() {
        bottomSheetBehavior.setState(bottomSheetBehavior.STATE_EXPANDED);
    }

    public void collapseBottomSheet() {
        bottomSheetBehavior.setState(bottomSheetBehavior.STATE_COLLAPSED);
    }

    public void hideBottomSheet() {
        bottomSheetBehavior.setState(bottomSheetBehavior.STATE_HIDDEN);
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh upload"), CHOOSE_IMAGE);
    }

    private void uploadImageToFirebaseStorage(int idTravel, int count_image) {
        StorageReference profileImageRef =
            FirebaseStorage.getInstance().getReference(idTravel+"/"+count_image+".jpg");

        if (uriProfileImage != null) {
            profileImageRef.putFile(uriProfileImage)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        profileImageUrl = taskSnapshot.getDownloadUrl().toString();
                        Toast.makeText(MapsActivity.this, "Tải ảnh lên thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MapsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        }
    }

    public void GetImageUpload(String url, ImageView image)
    {
        //-------------------------
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference islandRef = storageRef.child(url);
        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                image.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                upload_image.setImageBitmap(bitmap);
                uploadImageToFirebaseStorage(current_id_travel, count_image);
                count_image++;
                if (count_image>5)
                {
                    count_image =1;
                }

                GetImageUpload(current_id_travel+"/"+1+".jpg", imageUpload1);
                GetImageUpload(current_id_travel+"/"+2+".jpg", imageUpload2);
                GetImageUpload(current_id_travel+"/"+3+".jpg", imageUpload3);
                GetImageUpload(current_id_travel+"/"+4+".jpg", imageUpload4);
                GetImageUpload(current_id_travel+"/"+5+".jpg", imageUpload5);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class ReadJsonDataTravelEvent extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();
            try {
                java.net.URL url = new URL(strings[0]);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("ReadJson", "Chay load");
            Log.d("ReadJson", s);
            travelList = new ArrayList<>();

            try {
                JSONObject obj = new JSONObject(s);
                JSONArray array = obj.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    if (array.getJSONObject(i).getInt("id") == idevent) {
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
                        travelList.add(travel);
                    }
                }
                for (int i = 0; i < travelList.size(); i++) {
                    Log.d("ReadJson", "--------------------------");
                    Log.d("ReadJson", travelList.get(i).getName());
                    LatLng location = new LatLng(travelList.get(i).getLat(), travelList.get(i).getLng());
                    addMarker(location, travelList.get(i).getName());
                    ReadJsonImageTravel readImageJson = new ReadJsonImageTravel(travelList.get(i));
                    readImageJson.execute("http://3.82.158.167/api/travel/" + travelList.get(i).getId() + "/images");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
//    public static Intent getStartIntent(Context context) {
//        Intent intent = new Intent(context, CompassActivity.class);
//        return intent;
//    }

//    public class ReadJsonNhung extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... strings) {
//            StringBuilder content = new StringBuilder();
//            try {
//                java.net.URL url = new URL(strings[0]);
//                Log.d("ReadJson", "URL done");
//                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                String line = "";
//                while ((line = bufferedReader.readLine()) != null) {
//                    content.append(line);
//                }
//                bufferedReader.close();
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return content.toString();
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            Log.d("ReadJson", "Chay load");
//            Log.d("ReadJson", s);
//
//            try {
//                JSONObject obj = new JSONObject(s);
//                //JSONObject o = obj.getJSONObject("");
//
//
//                double lat = obj.getDouble("lat");
//                double lng = obj.getDouble("lng");
//
//                LatLng location = new LatLng(lat, lng);
//                addMarker(location, "Vi tri hien tai");
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }

}
