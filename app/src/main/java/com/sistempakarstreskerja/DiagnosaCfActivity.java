package com.sistempakarstreskerja;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class DiagnosaCfActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://streskerja.000webhostapp.com/get_gejala_cf.php";
    private ArrayList<HashMap<String, String>> list;
    private ArrayList<Double> hasil;
    private MaterialAutoCompleteTextView spinnerGejala;
    private int counter;
    private final int REQUEST_CODE = 1234;
    private RecyclerView recyclerView;
    private DiagnosaCFAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosa_cf);
        setTitle("Diagnosa Certainty Factor");

        // Tampilkan peringatan
        showAlertDialog();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Add DividerItemDecoration to show dividers between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        getData();

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(view -> {
            // Simpan semua jawaban dari adapter ke dalam ArrayList hasil
            for (int i = 0; i < adapter.getItemCount(); i++) {
                String selectedValue = adapter.getSelectedAnswer(i);
                hasil.add(konversiJawaban(selectedValue));
            }

            // Kirim hasil ke aktivitas berikutnya atau lakukan pengiriman data sesuai kebutuhan
            Intent intent = new Intent(getApplicationContext(), DiagnosaCfHasilActivity.class);
            intent.putExtra("hasil", android.text.TextUtils.join("#", hasil));
            startActivityForResult(intent, REQUEST_CODE);
        });

    }


    private double konversiJawaban(String value) {
        if (value == null) {
            return 0; // Atau nilai default sesuai kebutuhan Anda
        }
        switch (value) {
            case "Pasti (1)":
                return 1;
            case "Hampir Pasti (0.8)":
                return 0.8;
            case "Kemungkinan Besar (0.6)":
                return 0.6;
            case "Mungkin (0.4)":
                return 0.4;
            case "Tidak Tahu (0.2)":
                return 0.2;
            case "Mungkin Tidak (-0.4)":
                return -0.4;
            case "Kemungkinan Besar Tidak (-0.6)":
                return -0.6;
            case "Hampir Pasti Tidak (-0.8)":
                return -0.8;
            case "Pasti Tidak (-1)":
                return -1;
            default:
                return 0; // Default value jika tidak ada nilai yang sesuai
        }
    }

    private void setSpinnerToDefault() {
        spinnerGejala.setSelection(0); // 0 corresponds to the index of "Pilih"
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

    private ArrayList<DiagnosaCFGejala> convertToGejalaList(ArrayList<HashMap<String, String>> inputList) {
        ArrayList<DiagnosaCFGejala> gejalaList = new ArrayList<>();
        for (HashMap<String, String> gejalaData : inputList) {
            DiagnosaCFGejala gejala = new DiagnosaCFGejala(
                    gejalaData.get("kode_gejala"),
                    gejalaData.get("nama_gejala"),
                    gejalaData.get("selected_answer")
            );
            gejalaList.add(gejala);
        }
        return gejalaList;
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
                                map.put("selected_answer", "Pilih Jawaban"); // Tambahkan nilai default untuk selected_answer
                                list.add(map);
                                kosong = false;
                            }

                            ArrayList<DiagnosaCFGejala> gejalaList = convertToGejalaList(list);
                            adapter = new DiagnosaCFAdapter(gejalaList, DiagnosaCfActivity.this);
                            recyclerView.setAdapter(adapter);

                            if (kosong) {
                                Toast.makeText(DiagnosaCfActivity.this, "Tidak ada data gejala", Toast.LENGTH_SHORT).show();
                            } else {
                                hasil = new ArrayList<>();
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