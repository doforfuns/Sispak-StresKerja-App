package com.sistempakarstreskerja;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class DiagnosaCfActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://streskerja.000webhostapp.com/get_gejala_cf.php";
    private ArrayList<HashMap<String, String>> list;
    private ArrayList<Double> hasil;
    private TextView text_pertanyaan;
    private TextView kode;
    private MaterialAutoCompleteTextView spinnerGejala;
    private int counter;
    private final int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosa_cf);
        setTitle("Diagnosa Certainty Factor");

        // Tampilkan peringatan
        showAlertDialog();

        getData();

        text_pertanyaan = findViewById(R.id.text_pertanyaan);
        kode = findViewById(R.id.kode);
        spinnerGejala = findViewById(R.id.spinner_gejala);

        // Set up the adapter for the AutoCompleteTextView
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gejala_options, R.layout.dropdown_item_diagnosa);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGejala.setAdapter(adapter);

        spinnerGejala.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (view != null) {
                    TextView textOption = view.findViewById(R.id.text_option);
                    String selectedValue = parent.getItemAtPosition(position).toString();
                    textOption.setText(selectedValue);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set default selection to "Pilih"
        spinnerGejala.setText("Pilih Jawaban", false);

        Button btn_lanjut = findViewById(R.id.btn_lanjut);

        btn_lanjut.setOnClickListener(view -> {
            String selectedValue = spinnerGejala.getText().toString();
            hasil.add(konversiJawaban(selectedValue));
            showPertanyaan(++counter);
        });
    }

    private double konversiJawaban(String value) {
        switch (value) {
            case "Pasti":
                return 1;
            case "Hampir Pasti":
                return 0.8;
            case "Kemungkinan Besar":
                return 0.6;
            case "Mungkin":
                return 0.4;
            case "Tidak Tahu":
                return 0.2;
            case "Mungkin Tidak":
                return -0.4;
            case "Kemungkinan Besar Tidak":
                return -0.6;
            case "Hampir Pasti Tidak":
                return -0.8;
            case "Pasti Tidak":
                return -1;
            default:
                return 0; // Default value jika tidak ada nilai yang sesuai
        }
    }

    private void setSpinnerToDefault() {
        spinnerGejala.setSelection(0); // 0 corresponds to the index of "Pilih"
    }

    private void showPertanyaan(int index) {
        if (index >= list.size()) {
            Intent intent = new Intent(getApplicationContext(), DiagnosaCfHasilActivity.class);
            intent.putExtra("hasil", android.text.TextUtils.join("#", hasil));
            startActivityForResult(intent, REQUEST_CODE);
            // Set Spinner back to default
            setSpinnerToDefault();
        } else {
            HashMap<String, String> item = list.get(index);
            text_pertanyaan.setText("Apakah " + item.get("nama_gejala").toLowerCase() + "?");
            kode.setText(item.get("kode_gejala"));
            // Set Spinner back to default
            setSpinnerToDefault();
            spinnerGejala.setText("Pilih Jawaban", false); // Set default text for the spinner
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE) {
            getData();
        }
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(DiagnosaCfActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void getData() {
        displayLoader();
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, response -> {
                    pDialog.dismiss();
                    try {
                        if (response.getInt("status") == 0) {
                            JSONArray jsonArray = response.getJSONArray("gejala");
                            list = new ArrayList<>();
                            boolean kosong = true;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<>();
                                map.put("id_gejala", jsonObject.getString("id_gejala"));
                                map.put("nama_gejala", jsonObject.getString("nama_gejala"));
                                map.put("kode_gejala", jsonObject.getString("kode_gejala"));
                                list.add(map);
                                kosong = false;
                            }

                            if (kosong) {
                                Toast.makeText(DiagnosaCfActivity.this, "Tidak ada data gejala", Toast.LENGTH_SHORT).show();
                            } else {
                                hasil = new ArrayList<>();
                                counter = 0;
                                showPertanyaan(counter);
                            }

                        } else {
                            Toast.makeText(getApplicationContext(),
                                    response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    pDialog.dismiss();
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            exitByBackKey();
            return true;
        }
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Anda yakin mau kembali ?")
                .setPositiveButton("Ya", (arg0, arg1) -> finish())
                .setNegativeButton("Tidak", (arg0, arg1) -> {
                })
                .show();
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Perhatian!")
                .setMessage("Jawablah dengan hati-hati, semakin yakin artinya nilainya akan semakin besar. Jika tidak merasakan gejala yang ditampilkan, maka pilih jawaban tidak.")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Aksi jika pengguna menekan tombol OK (bisa kosongkan jika tidak ada aksi tambahan)
                })
                .show();
    }
}