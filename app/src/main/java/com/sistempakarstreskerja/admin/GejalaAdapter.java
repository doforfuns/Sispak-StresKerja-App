package com.sistempakarstreskerja.admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sistempakarstreskerja.R;

import java.util.ArrayList;
import java.util.HashMap;

public class GejalaAdapter extends RecyclerView.Adapter<GejalaAdapter.ViewHolder> {

    private ArrayList<HashMap<String, String>> gejalaList;
    private Context context;
    private OnItemClickListener itemClickListener; // Custom listener

    // Constructor
    public GejalaAdapter(ArrayList<HashMap<String, String>> gejalaList, Context context) {
        this.gejalaList = gejalaList;
        this.context = context;
    }

    // Setter for the custom listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gejala_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String, String> gejala = gejalaList.get(position);
        holder.tv_id.setText(gejala.get("id_gejala"));
        holder.tv_nama.setText(gejala.get("nama_gejala"));
    }

    @Override
    public int getItemCount() {
        return gejalaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_id, tv_nama;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_id = itemView.findViewById(R.id.id_gejala);
            tv_nama = itemView.findViewById(R.id.nama_gejala);

            // Handle item click
            itemView.setOnClickListener(view -> {
                if (itemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        itemClickListener.onItemClick(view, position);
                    }
                }
            });
        }
    }

    // Custom listener interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
