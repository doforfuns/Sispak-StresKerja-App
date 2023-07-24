package com.sistempakarstreskerja;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PenyakitRiwayatAdapter extends RecyclerView.Adapter<PenyakitRiwayatAdapter.ViewHolder> {

    private ArrayList<RiwayatItem> riwayatList;
    private OnItemClickListener mListener;

    public PenyakitRiwayatAdapter(ArrayList<RiwayatItem> riwayatList) {
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.riwayat_list, parent, false);
        return new ViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RiwayatItem riwayatItem = riwayatList.get(position);
        holder.tvListItem.setText(riwayatItem.getNamaPenyakit());
        holder.tvTanggal.setText(riwayatItem.getTanggal());
        holder.tvMetode.setText(riwayatItem.getMetode());
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
