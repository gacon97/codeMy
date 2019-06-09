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
import com.example.R;
import com.example.bottomnavigation.ItemClickListener;
import com.example.model.Travel;

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
            Log.d("Name travel", listTravels.get(i).getName());
            Log.d("ReadJson", "++++++++++++++++++++++++++++");
            Log.d("Size imgae travel", "" + listTravels.get(i).getSizeImage());
            Log.d("ReadJson", "++++++++++++++++++++++++++++");
            Log.d("First image", "" + listTravels.get(i).getImageUrl().get(0));

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
        Glide.with(mContext)
            .load(listTravels.get(i).getImageUrl().get(0))
            .into(holder.getImage());
        holder.mName.setText(listTravels.get(i).getName());
        holder.mPlace.setText(listTravels.get(i).getPlace());
        holder.mFeature.setText(listTravels.get(i).getFeature());


    }

    @Override
    public int getItemCount() {
        return listTravels.size();
    }
}
class CategoryTravelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    ImageView mImage;
    TextView mName;
    TextView mPlace;
    TextView mFeature;
    private ItemClickListener itemClickListener; // Khai báo interface
    public CategoryTravelViewHolder(@NonNull View itemView) {
        super(itemView);

        mImage = itemView.findViewById(R.id.imageView);
        mName = itemView.findViewById(R.id.txtName);
        mPlace = itemView.findViewById(R.id.txtPlace);
        mFeature = itemView.findViewById(R.id.txtFeature);

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
