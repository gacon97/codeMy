package com.example.bottomnavigation;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import com.example.bottomnavigation.Movie;
import com.example.bottomnavigation.MyApplication;
import com.example.bottomnavigation.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome extends Fragment {

    private static final String TAG = FragmentHome.class.getSimpleName();
    private static final String URL = "https://api.androidhive.info/json/movies_2017.json";


    private RecyclerView recyclerView;
    private List<Movie> movieList;
    private StoreAdapter mAdapter;
    private ViewFlipper viewFlipper;
    private GestureDetector mGestureDetector;


    public FragmentHome() {
        // Required empty public constructor
    }

    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        movieList = new ArrayList<>();
        mAdapter = new StoreAdapter(getActivity(), movieList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        int images[] = {R.drawable.slide1, R.drawable.slide2, R.drawable.slide3};

        viewFlipper = view.findViewById(R.id.v_flipper);
        viewFlipper.canScrollHorizontally(0);

        for(int image: images)
        {
            imageFlipper(image);
        }

        fetchStoreItems();
        CustomGestureDetector customGestureDetector = new CustomGestureDetector();
        mGestureDetector = new GestureDetector(getActivity(), customGestureDetector);

        return view;
    }

    private void fetchStoreItems() {
        JsonArrayRequest request = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getActivity(), "Couldn't fetch the store items! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<Movie> items = new Gson().fromJson(response.toString(), new TypeToken<List<Movie>>() {
                        }.getType());

                        movieList.clear();
                        movieList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyViewHolder> {
        private Context context;
        private List<Movie> movieList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name, price;
            public ImageView thumbnail;

            public MyViewHolder(View view) {
                super(view);   //gọi đến contructor của viewHolder truyền vào view
                name = view.findViewById(R.id.title);
                price = view.findViewById(R.id.price);
                thumbnail = view.findViewById(R.id.thumbnail);
            }
        }


        public StoreAdapter(Context context, List<Movie> movieList) {
            this.context = context;
            this.movieList = movieList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Movie movie = movieList.get(position);
            holder.name.setText(movie.getTitle());
            holder.price.setText(movie.getPrice());

            Glide.with(context)
                    .load(movie.getImage())
                    .into(holder.thumbnail);
        }

        @Override
        public int getItemCount() {
            return movieList.size();
        }
    }
    public void imageFlipper(int image)
    {
        ImageView imageView = new ImageView(getActivity());

        imageView.setBackgroundResource(image);
        viewFlipper.addView(imageView);
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(3000);

        viewFlipper.setInAnimation(getActivity(), android.R.anim.fade_in);
        viewFlipper.setOutAnimation(getActivity(), android.R.anim.fade_out);

    }

    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            // Swipe left (next)
            if (e1.getX() > e2.getX()) {
                viewFlipper.showNext();
            }

            // Swipe right (previous)
            if (e1.getX() < e2.getX()) {
                viewFlipper.showPrevious();
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);

        return onTouchEvent(event);
    }

}
