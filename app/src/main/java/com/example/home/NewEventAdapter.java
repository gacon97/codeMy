package com.example.home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.R;
import com.example.bottomnavigation.ItemClickListener;
import com.example.model.Event;

import java.util.ArrayList;

public class NewEventAdapter extends RecyclerView.Adapter<NewEventViewHolder> {
    private ArrayList<Event> newEvent;
    private Context mContext;

    public NewEventAdapter(ArrayList<Event> newEvent, Context mContext) {
        this.newEvent = newEvent;
        this.mContext = mContext;

        for (int i = 0; i < newEvent.size(); i++) {
            Log.d("ReadJson", "++++++++++++++++++++++++++++");
            Log.d("Name travel", newEvent.get(i).getName());
            Log.d("ReadJson", "++++++++++++++++++++++++++++");
            Log.d("Size imgae travel", "" + newEvent.get(i).getSizeImage());
            Log.d("ReadJson", "++++++++++++++++++++++++++++");
//            Log.d("First image", "" + listTravels.get(i).getImageUrl().get(0));

        }
    }

    @NonNull
    @Override
    public NewEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new NewEventViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewEventViewHolder holder, int position) {
//        holder.mImage.setImageResource(newEvent.get(position).getImage());
        Glide.with(mContext)
            .load(newEvent.get(position).getImageUrl().get(0))
            .into(holder.getImage());
        holder.mName.setText(newEvent.get(position).getName());
        holder.mContent.setText(newEvent.get(position).getContent());
        holder.mStartTime.setText(newEvent.get(position).getStart_time());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(isLongClick)
                    Toast.makeText(mContext, "Long Click: "+newEvent.get(position).getName(), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(mContext, " click "+ newEvent.get(position).getName(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(mContext, EventDetail.class);
                    intent.putExtra("ID_EVENT", newEvent.get(position).getId());
                    intent.putExtra("NAME_EVENT", newEvent.get(position).getName());
                    intent.putExtra("CONTENT_EVENT", newEvent.get(position).getContent());
                    intent.putExtra("START_TIME_EVENT", newEvent.get(position).getStart_time());
                    intent.putExtra("END_TIME_EVENT", newEvent.get(position).getEnd_time());
                    intent.putStringArrayListExtra("IMAGE_EVENT", newEvent.get(position).getImageUrl());

                    mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newEvent.size();
    }
}
class NewEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

    ImageView mImage;
    TextView mContent;
    TextView mStartTime;
    TextView mName;
    private ItemClickListener itemClickListener; // Khai báo interface

    NewEventViewHolder(View itemView) {
        super(itemView);

        mImage = itemView.findViewById(R.id.imageViewEvent);
        mName = itemView.findViewById(R.id.txtNameEvent);
        mContent = itemView.findViewById(R.id.txtContent);
        mStartTime = itemView.findViewById(R.id.txtStartTime);

        itemView.setOnClickListener(this); // Mấu chốt ở đây , set sự kiên onClick cho View
        itemView.setOnLongClickListener(this); // Mấu chốt ở đây , set sự kiên onLongClick cho View
    }
    //Tạo setter cho biến itemClickListenenr
    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false); // Gọi interface , false là vì đây là onClick
    }

    @Override
    public boolean onLongClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),true); // Gọi interface , true là vì đây là onLongClick
        return true;    }

    public ImageView getImage() {
        return this.mImage;
    }
}
