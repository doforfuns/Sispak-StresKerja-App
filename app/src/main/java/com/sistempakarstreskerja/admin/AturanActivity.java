package com.sistempakarstreskerja.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aturan);
        setTitle("Data Aturan");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                                adapter = new AturanAdapter(list, AturanActivity.this);

                                // Set item click listener for the adapter
                                adapter.setOnItemClickListener(new AturanAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        HashMap<String, String> aturan = list.get(position);
                                        String idPenyakit = aturan.get("id_penyakit");
                                        Intent intent = new Intent(AturanActivity.this, AturanViewActivity.class);
                                        intent.putExtra("id_penyakit", idPenyakit);
                                        startActivity(intent);
                                    }
                                });

                                recyclerView.setAdapter(adapter);

                                // Add DividerItemDecoration to show dividers between items
                                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
                                recyclerView.addItemDecoration(dividerItemDecoration);
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
            onBackPressed();
            return true;
        }
        return false;
    }
}
