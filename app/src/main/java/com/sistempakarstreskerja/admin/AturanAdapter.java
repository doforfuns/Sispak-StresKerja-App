package com.sistempakarstreskerja.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sistempakarstreskerja.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AturanAdapter extends RecyclerView.Adapter<AturanAdapter.ViewHolder> {

    private ArrayList<HashMap<String, String>> aturanList;
    private Context context;
    private OnItemClickListener itemClickListener;

    public AturanAdapter(ArrayList<HashMap<String, String>> aturanList, Context context) {
        this.aturanList = aturanList;
        this.context = context;
    }

    // Setter for the custom listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.aturan_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String, String> aturan = aturanList.get(position);
        holder.tv_id.setText(aturan.get("id_penyakit"));
        holder.tv_nama_penyakit.setText(aturan.get("nama_penyakit"));
        holder.tv_daftar_gejala.setText(aturan.get("daftar_gejala"));
    }

    @Override
    public int getItemCount() {
        return aturanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_id, tv_nama_penyakit, tv_daftar_gejala;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_id = itemView.findViewById(R.id.id_penyakit);
            tv_nama_penyakit = itemView.findViewById(R.id.nama_penyakit);
            tv_daftar_gejala = itemView.findViewById(R.id.daftar_gejala);

            // Handle item click
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    if (itemClickListener != null) {
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
