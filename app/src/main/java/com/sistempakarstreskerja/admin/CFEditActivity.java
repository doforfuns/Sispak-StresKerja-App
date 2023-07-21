package com.sistempakarstreskerja.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sistempakarstreskerja.R;

public class CFEditActivity extends AppCompatActivity {

    private EditText editNilaiCf;
    private TextView tvNamaPenyakit;
    private TextView tvNamaGejala;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cf_edit);
        setTitle("Ubah Nilai CF Rule");

        // Enable back button in ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get data from Intent
        Intent intent = getIntent();
        String selectedPenyakit = intent.getStringExtra("selected_penyakit");
        String selectedGejala = intent.getStringExtra("selected_gejala");
        String selectedNilaiCf = intent.getStringExtra("selected_nilai_cf");

        // Find views
        tvNamaPenyakit = findViewById(R.id.tv_nama_penyakit);
        tvNamaGejala = findViewById(R.id.tv_nama_gejala);
        editNilaiCf = findViewById(R.id.edit_nilai_cf);

        // Set data to TextViews
        tvNamaPenyakit.setText(selectedPenyakit);
        tvNamaGejala.setText(selectedGejala);
        editNilaiCf.setText(selectedNilaiCf);

        Button btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(view -> {
            String editedNilaiCf = editNilaiCf.getText().toString();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("edited_nilai_cf", editedNilaiCf);

            int editedPosition = getIntent().getIntExtra("position", -1);
            resultIntent.putExtra("edited_position", editedPosition);

            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
