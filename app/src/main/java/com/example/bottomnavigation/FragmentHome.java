package com.example.bottomnavigation;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SearchView;


import com.example.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

    private RecyclerView viewListCategories;
    private List<Movie> movieList;
//    private ViewFlipper viewFlipper;
    private ViewPager viewPager;
    private SearchView searchView;
    private Slider slider;
    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 3000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5000;
    GridView grid;
    ArrayList<Category> listCategories;

    public FragmentHome() {
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
        View view = inflater.inflate(R.layout.fragment_fragment_home, container, false);

        viewPager = view.findViewById(R.id.viewpager);

        TabLayout tabLayout = view.findViewById(R.id.tabDots);

        tabLayout.setupWithViewPager(viewPager, true);
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == slider.getCount()) {
                    currentPage = 0;
                }

                viewPager.setCurrentItem(currentPage++, true);
                viewPager.setPageTransformer(true, new FadePageTransformer());
            }
        };

        listCategories = new ArrayList<>();
        listCategories.add(new Category("Đường phố", R.drawable.cate_duong_pho));
        listCategories.add(new Category("Sông", R.drawable.cate_song));
        listCategories.add(new Category("Chùa", R.drawable.cate_chua));
        listCategories.add(new Category("Núi", R.drawable.cate_nui));
        listCategories.add(new Category("Biển", R.drawable.cate_bien));
        listCategories.add(new Category("Hồ", R.drawable.cate_ho));
        listCategories.add(new Category("Hang động", R.drawable.cate_hang_dong));
        listCategories.add(new Category("Kiến trúc", R.drawable.cate_kien_truc));
        listCategories.add(new Category("Sa mạc", R.drawable.cate_sa_mac));
        listCategories.add(new Category("Khách sạn", R.drawable.cate_khach_san));

//        adapter = new CategoryAdapter(getActivity(), listCategories);
        viewListCategories = view.findViewById(R.id.listCategories);
        RecyclerView.LayoutManager layoutCategories = new GridLayoutManager(getActivity(), 5);
        viewListCategories.setLayoutManager(layoutCategories);

        MyAdapter myAdapter = new MyAdapter(getActivity(), listCategories);
        viewListCategories.setAdapter(myAdapter);
        viewListCategories.setItemAnimator(new DefaultItemAnimator());
        viewListCategories.setNestedScrollingEnabled(false);

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        slider = new Slider(getActivity());
        viewPager.setAdapter(slider);

        return view;
    }

}
