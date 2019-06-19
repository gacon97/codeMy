package com.example.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.R;

import java.util.ArrayList;

public class ImageEventAdapter extends RecyclerView.Adapter<ImageEventViewHolder> {
    private Context mContext;
    private ArrayList<String> imageEvents;

    public ImageEventAdapter(Context mContext, ArrayList<String> imageEvents) {
        this.mContext = mContext;
        this.imageEvents = imageEvents;
        for (int i = 0; i < imageEvents.size(); i++) {
            Log.d("ReadJson", "++++++++++++++++++++++++++++");
            Log.d("url image", imageEvents.get(i));
        }


    }
    @NonNull
    @Override
    public ImageEventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_travel, viewGroup, false);
        return new ImageEventViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageEventViewHolder holder, int i) {
        Glide.with(mContext)
            .load(imageEvents.get(i))
            .into(holder.getImage());

//        holder.setItemClickListener(new ItemClickListener() {
//            @Override
//            public void onClick(View view, int position, boolean isLongClick) {
//                if (isLongClick)
//                    Toast.makeText(mContext, "Long Click: " + listEvents.get(position).getName(), Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(mContext, " click " + listEvents.get(position).getName(), Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent(mContext, com.example.home.Event.class);
////                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putStringArrayListExtra("IMAGE_TRAVEL", listEvents.get(position).getImageUrl());
//                mContext.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return imageEvents.size();
    }
}
class ImageEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
{
    ImageView mImage;


    public ImageEventViewHolder(@NonNull View itemView) {
        super(itemView);
        mImage = itemView.findViewById(R.id.image_travel_detail);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
    public ImageView getImage() {
        return this.mImage;
    }

}
