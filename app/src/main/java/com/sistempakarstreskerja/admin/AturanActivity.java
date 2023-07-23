package com.sistempakarstreskerja.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import java.util.HashMap;

public class AturanActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://streskerja.000webhostapp.com/get_daftar_aturan.php";
    private RecyclerView recyclerView;
    private AturanAdapter adapter;
    private static final int VIEW_REQUEST_CODE = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aturan);
        setTitle("Data Aturan");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Panggil metode getData() saat aktivitas dijalankan atau dilanjutkan dari aktivitas lain
        getData();
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(AturanActivity.this);
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
                            JSONArray jsonArray = response.getJSONArray("aturan");
                            ArrayList<HashMap<String, String>> list = new ArrayList<>();
                            boolean kosong = true;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("id_penyakit", jsonObject.getString("id_penyakit"));
                                map.put("nama_penyakit", "Nama Diagnosa : " + jsonObject.getString("nama_penyakit"));
                                map.put("daftar_gejala", "Daftar Gejala : " + jsonObject.getString("daftar_gejala"));
                                list.add(map);
                                kosong = false;
                            }

                            if (kosong) {
                                Toast.makeText(AturanActivity.this, "Tidak ada data aturan",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                if (adapter == null) {
                                    adapter = new AturanAdapter(list, AturanActivity.this);
                                    recyclerView.setAdapter(adapter);
                                    // Set item click listener for the adapter
                                    adapter.setOnItemClickListener((view, position) -> {
                                        HashMap<String, String> aturan = list.get(position);
                                        String idPenyakit = aturan.get("id_penyakit");
                                        Intent intent = new Intent(AturanActivity.this, AturanViewActivity.class);
                                        intent.putExtra("id_penyakit", idPenyakit);
                                        startActivityForResult(intent, VIEW_REQUEST_CODE);
                                    });

                                    // Add DividerItemDecoration to show dividers between items
                                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
                                    recyclerView.addItemDecoration(dividerItemDecoration);
                                } else {
                                    adapter.clearData();
                                    adapter.addAllData(list);
                                }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIEW_REQUEST_CODE && resultCode == RESULT_OK) {
            // If the result is OK, reload the data
            getData();
        }
    }
}
