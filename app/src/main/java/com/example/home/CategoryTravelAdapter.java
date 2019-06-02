package com.example.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.R;
import com.example.bottomnavigation.ItemClickListener;
import com.example.model.Travel;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class CategoryTravelAdapter extends RecyclerView.Adapter<CategoryTravelViewHolder> {
    private Context mContext;
    private ArrayList<Travel> listTravels;

    public CategoryTravelAdapter(Context mContext, ArrayList<Travel> listTravels) {
        this.mContext = mContext;
        this.listTravels = listTravels;
//
        for (int i = 0; i < listTravels.size(); i++) {
            Log.d("ReadJson", "++++++++++++++++++++++++++++");
            Log.d("ReadJson", listTravels.get(i).getName());
            Log.d("ReadJson", "++++++++++++++++++++++++++++");
            Log.d("ReadJson", "" + listTravels.get(i).getSizeImage());

        }


    }

    @NonNull
    @Override
    public CategoryTravelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_travel, parent, false);
        return new CategoryTravelViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryTravelViewHolder holder, int i) {
//        holder.mImage.setImageResource(listTravels.get(i).);
//        Glide.with(mContext)
//            .load(listTravels.get(i).firstImageTravel())
//            .into(holder.getImage());
        holder.mTitle.setText(listTravels.get(i).getName());

    }

    @Override
    public int getItemCount() {
        return listTravels.size();
    }
}
class CategoryTravelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    ImageView mImage;
    TextView mTitle;
    private ItemClickListener itemClickListener; // Khai báo interface
    public CategoryTravelViewHolder(@NonNull View itemView) {
        super(itemView);

        mImage = itemView.findViewById(R.id.imageView);
        mTitle = itemView.findViewById(R.id.txtCategory);

        itemView.setOnClickListener(this); // Mấu chốt ở đây , set sự kiên onClick cho View
        itemView.setOnLongClickListener(this); // Mấu chốt ở đây , set sự kiên onLongClick cho View
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
    public ImageView getImage(){ return this.mImage;}
}
