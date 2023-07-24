package com.sistempakarstreskerja;

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
import com.sistempakarstreskerja.PenyakitDetailActivity;
import com.sistempakarstreskerja.admin.PenyakitEditActivity;
import com.sistempakarstreskerja.admin.PenyakitTambahActivity;
import com.sistempakarstreskerja.MySingleton;
import com.sistempakarstreskerja.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PenyakitActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://streskerja.000webhostapp.com/get_daftar_diagnosa.php";
    private RecyclerView recyclerView;
    private PenyakitAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penyakit);
        setTitle("Daftar Diagnosa");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Add DividerItemDecoration to show dividers between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        getData();
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(PenyakitActivity.this);
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
                            JSONArray jsonArray = response.getJSONArray("penyakit");
                            ArrayList<HashMap<String, String>> list = new ArrayList<>();
                            boolean kosong = true;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<>();
                                map.put("id_penyakit", jsonObject.getString("id_penyakit"));
                                map.put("nama_penyakit", jsonObject.getString("nama_penyakit"));
                                list.add(map);
                                kosong = false;
                            }

                            adapter = new PenyakitAdapter(list, PenyakitActivity.this);
                            recyclerView.setAdapter(adapter);

                            adapter.setOnItemClickListener((view, position) -> {
                                HashMap<String, String> penyakit = list.get(position);
                                String idPenyakit = penyakit.get("id_penyakit");
                                Intent intent = new Intent(PenyakitActivity.this, PenyakitDetailActivity.class);
                                intent.putExtra("id_penyakit", idPenyakit);
                                startActivity(intent);
                            });

                            if (kosong) {
                                Toast.makeText(PenyakitActivity.this, "Tidak ada data penyakit",
                                        Toast.LENGTH_SHORT).show();
                            }

                            adapter.notifyDataSetChanged();
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
