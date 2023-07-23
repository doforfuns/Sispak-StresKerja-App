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

public class PenggunaAdapter extends RecyclerView.Adapter<PenggunaAdapter.ViewHolder> {

    private ArrayList<HashMap<String, String>> penggunaList;
    private Context context;

    public PenggunaAdapter(ArrayList<HashMap<String, String>> penggunaList, Context context) {
        this.penggunaList = penggunaList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pengguna_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String, String> pengguna = penggunaList.get(position);
        holder.tv_id.setText(pengguna.get("id_pengguna"));
        holder.tv_nama.setText(pengguna.get("nama_lengkap"));
    }

    @Override
    public int getItemCount() {
        return penggunaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_id, tv_nama;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_id = itemView.findViewById(R.id.id_pengguna);
            tv_nama = itemView.findViewById(R.id.nama_lengkap);

            // Handle item click
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    HashMap<String, String> pengguna = penggunaList.get(position);
                    String idPengguna = pengguna.get("id_pengguna");
                    Intent intent = new Intent(context, PenggunaEditActivity.class);
                    intent.putExtra("id_pengguna", idPengguna);
                    context.startActivity(intent);
                }
            });
        }
    }
}
