package com.sistempakarstreskerja;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import java.util.ArrayList;

public class DiagnosaCFAdapter extends RecyclerView.Adapter<DiagnosaCFAdapter.ViewHolder> {

    private ArrayList<DiagnosaCFGejala> diagnosaList;
    private Context context;

    public DiagnosaCFAdapter(ArrayList<DiagnosaCFGejala> diagnosaList, Context context) {
        this.diagnosaList = diagnosaList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diagnosa_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DiagnosaCFGejala gejala = diagnosaList.get(position);
        holder.tvKode.setText(gejala.getKodeGejala());
        holder.tvPertanyaan.setText(gejala.getKodeGejala() + " - " + "Apakah " + gejala.getNamaGejala().toLowerCase() + "?");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.gejala_options, R.layout.dropdown_item_diagnosa);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerGejala.setAdapter(adapter);

        // Set selected value for spinner
        holder.spinnerGejala.setText(gejala.getSelectedAnswer(), false);

        // Handle item selection for MaterialAutoCompleteTextView
        holder.spinnerGejala.setOnItemClickListener((parent, view, pos, id) -> {
            String selectedValue = parent.getItemAtPosition(pos).toString();
            gejala.setSelectedAnswer(selectedValue);
        });
    }

    @Override
    public int getItemCount() {
        return diagnosaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvKode, tvPertanyaan;
        MaterialAutoCompleteTextView spinnerGejala;

        public ViewHolder(View itemView) {
            super(itemView);
            tvKode = itemView.findViewById(R.id.kode);
            tvPertanyaan = itemView.findViewById(R.id.text_pertanyaan);
            spinnerGejala = itemView.findViewById(R.id.spinner_gejala);
        }
    }

    public ArrayList<DiagnosaCFGejala> getDiagnosaList() {
        return diagnosaList;
    }

    public String getSelectedAnswer(int position) {
        DiagnosaCFGejala gejala = diagnosaList.get(position);
        return gejala.getSelectedAnswer();
    }
}
