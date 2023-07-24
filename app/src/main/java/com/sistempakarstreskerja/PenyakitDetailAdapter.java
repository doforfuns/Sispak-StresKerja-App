package com.sistempakarstreskerja;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PenyakitDetailAdapter extends RecyclerView.Adapter<PenyakitDetailAdapter.ViewHolder> {

    private ArrayList<String> symptomsList;
    private ArrayList<String> nilaiCfList;
    private ArrayList<String> idAturanList;

    public PenyakitDetailAdapter(ArrayList<String> symptomsList, ArrayList<String> nilaiCfList, ArrayList<String> idAturanList) {
        this.symptomsList = symptomsList;
        this.nilaiCfList = nilaiCfList;
        this.idAturanList = idAturanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_gejala_diagnosa, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String symptomNumber = "Gejala " + (position + 1); // Create a numbered list
        String symptom = symptomsList.get(position);
        String nilaiCf = nilaiCfList.get(position);
        String id_aturan = idAturanList.get(position);

        holder.tvSymptomNumber.setText(symptomNumber);
        holder.tvListItem.setText(symptom);
        holder.tvNilaiCf.setText("Nilai CF : " + nilaiCf);
        holder.tvid_aturan.setText("ID : " + id_aturan);
    }

    @Override
    public int getItemCount() {
        return symptomsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSymptomNumber;
        TextView tvListItem;
        TextView tvNilaiCf;
        TextView tvid_aturan;

        ViewHolder(View itemView) {
            super(itemView);
            tvSymptomNumber = itemView.findViewById(R.id.tv_symptom_number);
            tvListItem = itemView.findViewById(R.id.tv_list_gejala);
            tvNilaiCf = itemView.findViewById(R.id.tv_nilai_cf);
            tvid_aturan = itemView.findViewById(R.id.tv_id_aturan);
        }
    }
}

