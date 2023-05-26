package com.example.campusdelivery.classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusdelivery.R;

import java.util.ArrayList;

public class CompAdapter extends RecyclerView.Adapter<CompAdapter.MyViewHolder> {
    private ArrayList<com.example.campusdelivery.classes.Support> mList;

    public void setmList(ArrayList<com.example.campusdelivery.classes.Support> mList) {
        this.mList = mList;
    }

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView date;
        public TextView username;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListener
                listener) {
            super(itemView);
            title = itemView.findViewById(R.id.vcomp_title);
            date = itemView.findViewById(R.id.vcom_date);
            username = itemView.findViewById(R.id.vcom_user);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public CompAdapter(ArrayList<com.example.campusdelivery.classes.Support> aList) {
        mList = aList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comp_view, viewGroup, false);
        MyViewHolder evh = new MyViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        com.example.campusdelivery.classes.Support temp = mList.get(i);
        viewHolder.title.setText(temp.getSupp_title());
        viewHolder.date.setText(temp.getSupp_date());
        viewHolder.username.setText("From: "+temp.getUsername());

    }
    @Override
    public int getItemCount() {
        if (mList == null)
            return 0;
        else
            return mList.size();
    }
}
