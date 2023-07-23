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

public class PenyakitAdapter extends RecyclerView.Adapter<PenyakitAdapter.ViewHolder> {

    private ArrayList<HashMap<String, String>> penyakitList;
    private Context context;
    private OnItemClickListener itemClickListener;

    public PenyakitAdapter(ArrayList<HashMap<String, String>> penyakitList, Context context) {
        this.penyakitList = penyakitList;
        this.context = context;
    }

    // Setter for the custom listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.penyakit_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String, String> penyakit = penyakitList.get(position);
        holder.tv_id.setText(penyakit.get("id_penyakit"));
        holder.tv_nama.setText(penyakit.get("nama_penyakit"));
    }

    @Override
    public int getItemCount() {
        return penyakitList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_id, tv_nama;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_id = itemView.findViewById(R.id.id_penyakit);
            tv_nama = itemView.findViewById(R.id.nama_penyakit);

            // Handle item click
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    HashMap<String, String> penyakit = penyakitList.get(position);
                    String idPenyakit = penyakit.get("id_penyakit");
                    Intent intent = new Intent(context, PenyakitEditActivity.class);
                    intent.putExtra("id_penyakit", idPenyakit);
                    context.startActivity(intent);
                }
            });
        }
    }
    // Custom listener interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}

