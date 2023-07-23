package com.sistempakarstreskerja.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sistempakarstreskerja.MySingleton;
import com.sistempakarstreskerja.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PenyakitActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://streskerja.000webhostapp.com/get_daftar_penyakit.php";
    private RecyclerView recyclerView;
    private PenyakitAdapter adapter;

    private static final int REQUEST_ADD_PENYAKIT = 1;
    private static final int REQUEST_EDIT_PENYAKIT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penyakit_admin);
        setTitle("Data Diagnosa");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(PenyakitActivity.this, PenyakitTambahActivity.class);
            startActivityForResult(intent, REQUEST_ADD_PENYAKIT);
        });

        // Register EventBus
        EventBus.getDefault().register(this);

        // Load data when first opening the activity
        loadData();
    }

    public class DataRefreshEvent {
        // Tambahkan atribut atau data yang dibutuhkan (jika ada)
    }

    @Override
    protected void onDestroy() {
        // Unregister EventBus to avoid memory leaks
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(PenyakitActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void loadData() {
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

                            if (kosong) {
                                Toast.makeText(PenyakitActivity.this, "Tidak ada data penyakit",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                adapter = new PenyakitAdapter(list, PenyakitActivity.this);

                                // Set item click listener for the adapter
                                adapter.setOnItemClickListener((view, position) -> {
                                    HashMap<String, String> penyakit = list.get(position);
                                    String idPenyakit = penyakit.get("id_penyakit");
                                    Intent intent = new Intent(PenyakitActivity.this, PenyakitEditActivity.class);
                                    intent.putExtra("id_penyakit", idPenyakit);
                                    startActivityForResult(intent, REQUEST_EDIT_PENYAKIT);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_PENYAKIT && resultCode == RESULT_OK) {
            // Refresh data if data is added successfully
            loadData();
        } else if (requestCode == REQUEST_EDIT_PENYAKIT && resultCode == RESULT_OK) {
            // Refresh data if data is edited successfully
            loadData();
        }
    }

    // Method to handle the data refresh event from EventBus
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataRefreshEvent(DataRefreshEvent event) {
        loadData();
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

