package com.sistempakarstreskerja.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sistempakarstreskerja.MySingleton;
import com.sistempakarstreskerja.R;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

public class PenyakitEditActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://streskerja.000webhostapp.com/get_penyakit.php";
    private static final String url_update = "https://streskerja.000webhostapp.com/update_penyakit.php";
    private static final String url_delete = "https://streskerja.000webhostapp.com/delete_penyakit.php";
    private EditText et_kode_penyakit;
    private EditText et_nama_penyakit;
    private EditText et_deskripsi;
    private EditText et_solusi;
    private String kode_penyakit;
    private String nama_penyakit;
    private String deskripsi;
    private String solusi;
    private String id_penyakit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penyakit_edit);
        setTitle("Ubah Diagnosa");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_penyakit = extras.getString("id_penyakit");
        }

        et_kode_penyakit = findViewById(R.id.et_kode_penyakit);
        et_nama_penyakit = findViewById(R.id.et_nama_penyakit);
        et_deskripsi = findViewById(R.id.et_deskripsi);
        et_solusi = findViewById(R.id.et_solusi);

        Button btn_simpan = findViewById(R.id.btn_simpan);

        btn_simpan.setOnClickListener(view -> {
            kode_penyakit = et_kode_penyakit.getText().toString().trim();
            nama_penyakit = et_nama_penyakit.getText().toString().trim();
            deskripsi = et_deskripsi.getText().toString();
            solusi = et_solusi.getText().toString();
            if (validateInputs()) {
                ubahPenyakit();
            }
        });

        getPenyakit();
    }

    public class DataRefreshEvent {
        // Tambahkan atribut atau data yang dibutuhkan (jika ada)
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
            new AlertDialog.Builder(PenyakitEditActivity.this)
                    .setTitle("Konfirmasi")
                    .setMessage("Anda yakin penyakit akan dihapus?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Ya, Hapus", (dialog, whichButton) -> {
                        hapusPenyakit();
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

    private void hapusPenyakit() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("id_penyakit", id_penyakit);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url_delete, request, response -> {
                    pDialog.dismiss();
                    try {
                        Toast.makeText(getApplicationContext(),
                                response.getString("message"), Toast.LENGTH_SHORT).show();

                        // Send data refresh event to PenyakitActivity
                        EventBus.getDefault().post(new DataRefreshEvent());
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

    private void getPenyakit() {
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
                            et_kode_penyakit.setText(response.getString("kode_penyakit"));
                            et_nama_penyakit.setText(response.getString("nama_penyakit"));
                            et_deskripsi.setText(response.getString("deskripsi"));
                            et_solusi.setText(response.getString("solusi"));
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
        pDialog = new ProgressDialog(PenyakitEditActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void ubahPenyakit() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("id_penyakit", id_penyakit);
            request.put("kode_penyakit", kode_penyakit);
            request.put("nama_penyakit", nama_penyakit);
            request.put("deskripsi", deskripsi);
            request.put("solusi", solusi);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url_update, request, response -> {
                    pDialog.dismiss();
                    try {
                        if (response.getInt("status") == 0) {
                            Toast.makeText(getApplicationContext(),
                                    response.getString("message"), Toast.LENGTH_SHORT).show();
                            // Send data refresh event to PenyakitActivity
                            EventBus.getDefault().post(new DataRefreshEvent());
                            finish();
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

    private boolean validateInputs() {
        if (kode_penyakit.equals("")) {
            et_kode_penyakit.setError("Kode Penyakit tidak boleh kosong");
            et_kode_penyakit.requestFocus();
            return false;
        }
        if (nama_penyakit.equals("")) {
            et_nama_penyakit.setError("Nama Penyakit tidak boleh kosong");
            et_nama_penyakit.requestFocus();
            return false;
        }
        if (deskripsi.equals("")) {
            et_deskripsi.setError("Deskripsi tidak boleh kosong");
            et_deskripsi.requestFocus();
            return false;
        }
        if (solusi.equals("")) {
            et_solusi.setError("Solusi tidak boleh kosong");
            et_solusi.requestFocus();
            return false;
        }
        return true;
    }
}

