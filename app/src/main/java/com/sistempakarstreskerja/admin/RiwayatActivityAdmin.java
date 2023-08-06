package com.sistempakarstreskerja.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

import com.sistempakarstreskerja.PenyakitDetailActivity;
import com.sistempakarstreskerja.R;

public class RiwayatActivityAdmin extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://streskerja.000webhostapp.com/get_daftar_riwayat_admin.php";
    private RecyclerView recyclerView;
    private TextView tv_tidak_ada;
    private PenyakitRiwayatAdminAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_admin);
        setTitle("Riwayat Diagnosa");

        recyclerView = findViewById(R.id.list_riwayat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tv_tidak_ada = findViewById(R.id.tv_tidak_ada);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        getData();
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(RiwayatActivityAdmin.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void getData() {
        displayLoader();
        JSONObject request = new JSONObject();
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, response -> {
                    pDialog.dismiss();
                    try {
                        if (response.getInt("status") == 0) {
                            JSONArray jsonArray = response.getJSONArray("riwayat");
                            ArrayList<RiwayatItemAdmin> riwayatList = new ArrayList<>();

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
                                String idPenyakit = jsonObject.getString("id_penyakit");

                                RiwayatItemAdmin item = new RiwayatItemAdmin(namaPenyakit, tanggal, metode, idPenyakit);
                                riwayatList.add(item);
                            }

                            if (riwayatList.size() > 0) {
                                recyclerView.setVisibility(View.VISIBLE);
                                tv_tidak_ada.setVisibility(View.GONE);
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                tv_tidak_ada.setVisibility(View.VISIBLE);
                            }

                            PenyakitRiwayatAdminAdapter adapter = new PenyakitRiwayatAdminAdapter(riwayatList);
                            recyclerView.setAdapter(adapter);

                            adapter.setOnItemClickListener((view, position) -> {
                                RiwayatItemAdmin riwayatItemAdmin = riwayatList.get(position);
                                String idPenyakit = riwayatItemAdmin.getIdPenyakit();
                                if (idPenyakit != null && !idPenyakit.isEmpty()) {
                                    Intent intent = new Intent(RiwayatActivityAdmin.this, PenyakitDetailActivity.class);
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