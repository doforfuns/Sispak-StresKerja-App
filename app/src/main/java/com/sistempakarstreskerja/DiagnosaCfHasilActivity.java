package com.sistempakarstreskerja;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DiagnosaCfHasilActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://streskerja.000webhostapp.com/get_hasil_cf.php";
    private String hasil;
    private Button btn_penyakit;

    private List<HasilDiagnosis> hasilDiagnosisList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosa_cf_hasil);
        setTitle("Hasil Diagnosa");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            hasil = extras.getString("hasil");
        }

        btn_penyakit = findViewById(R.id.btn_penyakit);
        Button btnTampilkanHasilLain = findViewById(R.id.btn_tampilkan_hasil_lain);
        Button user = findViewById(R.id.btn_dashboarduser);

        user.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        });

        btnTampilkanHasilLain.setOnClickListener(view -> showPopupHasilDiagnosa());

        getHasilDiagnosa();
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(DiagnosaCfHasilActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void getHasilDiagnosa() {
        displayLoader();
        JSONObject request = new JSONObject();
        SessionHandler session = new SessionHandler(this);
        User user = session.getUserDetails();
        try {
            request.put("hasil", hasil);
            request.put("metode", "Certainty Factor");
            request.put("id_pengguna", user.getIdPengguna());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, response -> {
                    pDialog.dismiss();
                    try {
                        if (response.getInt("status") == 0) {
                            // Extract data from the response
                            String idPenyakitTerbesar = response.getString("id_penyakit");
                            String namaPenyakitTerbesar = response.getString("nama_penyakit");
                            String nilaiTerbesar = response.getString("nilai");

                            // Add the largest result separately
                            btn_penyakit.setText(namaPenyakitTerbesar + " (" + nilaiTerbesar + "%)");
                            btn_penyakit.setOnClickListener(v -> {
                                Intent myIntent = new Intent(v.getContext(), PenyakitDetailActivity.class);
                                myIntent.putExtra("id_penyakit", idPenyakitTerbesar);
                                startActivity(myIntent);
                            });

                            // Tambahkan data hasil diagnosis ke dalam list
                            hasilDiagnosisList = parseHasilDiagnosis(response);
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

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private List<HasilDiagnosis> parseHasilDiagnosis(JSONObject response) {
        List<HasilDiagnosis> hasilDiagnosisList = new ArrayList<>();
        try {
            if (response.has("hasil_diagnosis")) {
                JSONArray hasilDiagnosisArray = response.getJSONArray("hasil_diagnosis");
                for (int i = 0; i < hasilDiagnosisArray.length(); i++) {
                    JSONObject item = hasilDiagnosisArray.getJSONObject(i);

                    String idPenyakit = item.getString("id_penyakit");
                    String namaPenyakit = item.getString("nama_penyakit");
                    String nilai = item.getString("nilai");

                    HasilDiagnosis hasilDiagnosis = new HasilDiagnosis(idPenyakit, namaPenyakit, nilai);
                    hasilDiagnosisList.add(hasilDiagnosis);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hasilDiagnosisList;
    }



    private void showPopupHasilDiagnosa() {
        // Inflate layout untuk popup
        View popupView = getLayoutInflater().inflate(R.layout.popup_hasil_diagnosa, null);

        // Buat RecyclerView dan adapter untuk menampilkan hasil diagnosis
        RecyclerView recyclerView = popupView.findViewById(R.id.recyclerview_hasil_diagnosis);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        HasilDiagnosisAdapter adapter = new HasilDiagnosisAdapter(this, hasilDiagnosisList);
        recyclerView.setAdapter(adapter);

        // Add DividerItemDecoration to show dividers between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Tampilkan popup menggunakan AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(popupView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
