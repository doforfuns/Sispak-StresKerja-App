package com.sistempakarstreskerja.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sistempakarstreskerja.R;

import java.util.ArrayList;

public class PenyakitRiwayatAdminAdapter extends RecyclerView.Adapter<PenyakitRiwayatAdminAdapter.ViewHolder> {

    private ArrayList<RiwayatItemAdmin> riwayatList;
    private OnItemClickListener mListener;

    public PenyakitRiwayatAdminAdapter(ArrayList<RiwayatItemAdmin> riwayatList) {
        this.riwayatList = riwayatList;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.riwayat_list_admin, parent, false);
        return new ViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RiwayatItemAdmin riwayatItemAdmin = riwayatList.get(position);
        holder.tvListItem.setText(riwayatItemAdmin.getNamaPenyakit());
        holder.tvTanggal.setText(riwayatItemAdmin.getTanggal());
        holder.tvMetode.setText(riwayatItemAdmin.getMetode());
    }

    @Override
    public int getItemCount() {
        return riwayatList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvListItem;
        TextView tvTanggal;
        TextView tvMetode;

        ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvListItem = itemView.findViewById(R.id.tv_hasil);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
            tvMetode = itemView.findViewById(R.id.tv_metode);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(v, position);
                    }
                }
            });
        }
    }
}
