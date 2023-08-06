package com.sistempakarstreskerja;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HasilDiagnosisAdapter extends RecyclerView.Adapter<HasilDiagnosisAdapter.ViewHolder> {

    private Context context;
    private List<HasilDiagnosis> hasilDiagnosisList;

    public HasilDiagnosisAdapter(Context context, List<HasilDiagnosis> hasilDiagnosisList) {
        this.context = context;
        this.hasilDiagnosisList = hasilDiagnosisList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_hasil_diagnosa, parent, false);
        return new ViewHolder(view);
    }

//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        HasilDiagnosis hasilDiagnosis = hasilDiagnosisList.get(position);
//        holder.textViewIdPenyakit.setText(hasilDiagnosis.getIdPenyakit());
//        holder.textViewNamaPenyakit.setText(hasilDiagnosis.getNamaPenyakit());
//        holder.textViewNilai.setText(hasilDiagnosis.getNilai());
//    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HasilDiagnosis hasilDiagnosis = hasilDiagnosisList.get(position);
        holder.textViewNamaPenyakit.setText(hasilDiagnosis.getNamaPenyakit() + " (" + hasilDiagnosis.getNilai() + "%)");
    }


    @Override
    public int getItemCount() {
        return hasilDiagnosisList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewIdPenyakit, textViewNamaPenyakit, textViewNilai;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewIdPenyakit = itemView.findViewById(R.id.tv_id_penyakit);
            textViewNamaPenyakit = itemView.findViewById(R.id.tv_nama_penyakit);
            textViewNilai = itemView.findViewById(R.id.tv_nilai_cf);
        }
    }
}

