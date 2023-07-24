package com.sistempakarstreskerja;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import java.util.HashMap;

public class RiwayatActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://streskerja.000webhostapp.com/get_daftar_riwayat.php";
    private static final String url_delete = "https://streskerja.000webhostapp.com/hapus_daftar_riwayat.php";
    private RecyclerView recyclerView;
    private TextView tv_tidak_ada;
    private PenyakitRiwayatAdapter adapter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);
        setTitle("Riwayat Diagnosa");

        SessionHandler session = new SessionHandler(this);
        user = session.getUserDetails();

        recyclerView = findViewById(R.id.list_riwayat); // Replace R.id.recyclerView with the actual ID of your RecyclerView in the XML layout.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        tv_tidak_ada = findViewById(R.id.tv_tidak_ada);

        // Add DividerItemDecoration to show dividers between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete) {
            new AlertDialog.Builder(RiwayatActivity.this)
                    .setTitle("Konfirmasi")
                    .setMessage("Anda yakin akan menghapus semua data riwayat ?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Ya, Hapus", (dialog, whichButton) -> {
                        hapusRiwayat();
                        finish();
                    })
                    .setNegativeButton("Tidak", null).show();
            return true;
        }

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }

    private void hapusRiwayat() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("id_pengguna", user.getIdPengguna());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url_delete, request, response -> {
                    pDialog.dismiss();
                    try {
                        Toast.makeText(getApplicationContext(),
                                response.getString("message"), Toast.LENGTH_SHORT).show();
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
        pDialog = new ProgressDialog(RiwayatActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void getData() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("id_pengguna", user.getIdPengguna());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, response -> {
                    pDialog.dismiss();
                    try {
                        if (response.getInt("status") == 0) {
                            JSONArray jsonArray = response.getJSONArray("riwayat");
                            ArrayList<RiwayatItem> riwayatList = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String namaPenyakit = "";
                                if (jsonObject.getString("nilai").equals("null")) {
                                    namaPenyakit = jsonObject.getString("nama_penyakit");
                                } else {
                                    namaPenyakit = jsonObject.getString("nama_penyakit") +
                                            " (" + jsonObject.getString("nilai") + "%)";
                                }
                                String tanggal = jsonObject.getString("tanggal");
                                String metode = jsonObject.getString("metode");
                                String idPenyakit = jsonObject.getString("id_penyakit"); // Get the id_penyakit from the JSON response

                                RiwayatItem item = new RiwayatItem(namaPenyakit, tanggal, metode, idPenyakit);
                                riwayatList.add(item);
                            }


                            if (riwayatList.size() > 0) {
                                // Hide the "Tidak ada riwayat" message if there are items in the list
                                recyclerView.setVisibility(View.VISIBLE);
                                tv_tidak_ada.setVisibility(View.GONE);
                            } else {
                                // Show the "Tidak ada riwayat" message if the list is empty
                                recyclerView.setVisibility(View.GONE);
                                tv_tidak_ada.setVisibility(View.VISIBLE);
                            }

                            PenyakitRiwayatAdapter adapter = new PenyakitRiwayatAdapter(riwayatList);
                            recyclerView.setAdapter(adapter);

                            adapter.setOnItemClickListener((view, position) -> {
                                RiwayatItem riwayatItem = riwayatList.get(position);
                                String idPenyakit = riwayatItem.getIdPenyakit(); // Modify this if you have an ID field in RiwayatItem
                                if (idPenyakit != null && !idPenyakit.isEmpty()) {
                                    Intent intent = new Intent(RiwayatActivity.this, PenyakitDetailActivity.class);
                                    intent.putExtra("id_penyakit", idPenyakit);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            tv_tidak_ada.setVisibility(View.VISIBLE);
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

}
