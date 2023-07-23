// AturanViewAdapter.java
package com.sistempakarstreskerja.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sistempakarstreskerja.R;

import java.util.ArrayList;

public class AturanViewAdapter extends RecyclerView.Adapter<AturanViewAdapter.ViewHolder> {

    private ArrayList<String> symptomsList;
    private ArrayList<String> nilaiCfList;
    private ArrayList<String> idAturanList;
    private OnItemClickListener itemClickListener;

    public AturanViewAdapter(ArrayList<String> symptomsList, ArrayList<String> nilaiCfList, ArrayList<String> idAturanList) {
        this.symptomsList = symptomsList;
        this.nilaiCfList = nilaiCfList;
        this.idAturanList = idAturanList;
    }

    public interface OnItemClickListener {
        void onItemClick(String selectedGejala, String selectedNilaiCf, String selectedIdAturan, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_gejala_cf, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String symptom = symptomsList.get(position);
        String nilaiCf = nilaiCfList.get(position);
        String id_aturan = idAturanList.get(position);

        holder.tvListItem.setText(symptom);
        holder.tvNilaiCf.setText("Nilai CF : " + nilaiCf);
        holder.tvid_aturan.setText("ID : " + id_aturan);
    }

    @Override
    public int getItemCount() {
        return symptomsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvListItem;
        TextView tvNilaiCf;
        TextView tvid_aturan;

        ViewHolder(View itemView) {
            super(itemView);
            tvListItem = itemView.findViewById(R.id.tv_list_gejala);
            tvNilaiCf = itemView.findViewById(R.id.tv_nilai_cf);
            tvid_aturan = itemView.findViewById(R.id.tv_id_aturan);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (itemClickListener != null && position != RecyclerView.NO_POSITION) {
                    String selectedGejala = symptomsList.get(position);
                    String selectedNilaiCf = nilaiCfList.get(position);
                    String selectedIdAturan = idAturanList.get(position);
                    itemClickListener.onItemClick(selectedGejala, selectedNilaiCf, selectedIdAturan, position);
                }
            });
        }
    }
}
