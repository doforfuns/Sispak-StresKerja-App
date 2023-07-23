package com.sistempakarstreskerja.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sistempakarstreskerja.MySingleton;
import com.sistempakarstreskerja.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AturanViewActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://streskerja.000webhostapp.com/get_aturan.php";
    private TextView tv_nama_penyakit;
    private String id_penyakit;
    private String nama_penyakit = "";

    private ArrayList<String> symptomsList = new ArrayList<>();
    private ArrayList<String> nilaiCfList = new ArrayList<>();
    private ArrayList<String> id_aturanList = new ArrayList<>();

    private static final int EDIT_REQUEST_CODE = 1;

    private AturanViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aturan_view);
        setTitle("Detail Aturan");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_penyakit = extras.getString("id_penyakit");
        }

        tv_nama_penyakit = findViewById(R.id.tv_nama_penyakit);

        Button btn_atur_ulang = findViewById(R.id.btn_atur_ulang);
        btn_atur_ulang.setOnClickListener(view -> {
            Intent intent = new Intent(AturanViewActivity.this, AturanEditActivity.class);
            intent.putExtra("id_penyakit", id_penyakit);
            intent.putExtra("nama_penyakit", nama_penyakit);
            startActivityForResult(intent, EDIT_REQUEST_CODE);
        });

        RecyclerView recyclerDaftarGejala = findViewById(R.id.recycler_daftar_gejala);
        recyclerDaftarGejala.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AturanViewAdapter(symptomsList, nilaiCfList, id_aturanList);
        adapter.setOnItemClickListener((selectedGejala, selectedNilaiCf, selectedIdAturan, position) -> {
            handleItemClick(selectedGejala, selectedNilaiCf, selectedIdAturan, position);
        });

        recyclerDaftarGejala.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAturan();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }

    private void getAturan() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("id_penyakit", id_penyakit);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, response -> {
                    pDialog.dismiss();
                    try {
                        if (response.getInt("status") == 0) {
                            tv_nama_penyakit.setText(response.getString("nama_penyakit"));

                            JSONArray symptomsArray = response.getJSONArray("daftar_gejala");
                            JSONArray nilaiCfArray = response.getJSONArray("nilai_cf");
                            JSONArray id_aturanArray = response.getJSONArray("id_aturan");

                            symptomsList.clear();
                            nilaiCfList.clear();
                            id_aturanList.clear();

                            for (int i = 0; i < symptomsArray.length(); i++) {
                                symptomsList.add(symptomsArray.getString(i));
                                nilaiCfList.add(nilaiCfArray.getString(i));
                                id_aturanList.add(id_aturanArray.getString(i));
                            }

                            // Notify the adapter of data changes
                            adapter.notifyDataSetChanged();

                            nama_penyakit = response.getString("nama_penyakit");
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

    private void displayLoader() {
        pDialog = new ProgressDialog(AturanViewActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void handleItemClick(String selectedGejala, String selectedNilaiCf, String selectedIdAturan, int position) {
        Intent intent = new Intent(AturanViewActivity.this, CFEditActivity.class);
        intent.putExtra("selected_penyakit", nama_penyakit); // Use the 'nama_penyakit' variable
        intent.putExtra("selected_gejala", selectedGejala);
        intent.putExtra("selected_nilai_cf", selectedNilaiCf);
        intent.putExtra("selected_id_aturan", selectedIdAturan);
        intent.putExtra("position", position);
        startActivityForResult(intent, EDIT_REQUEST_CODE);
    }
}
