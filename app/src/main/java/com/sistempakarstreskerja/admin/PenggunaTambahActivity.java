package com.sistempakarstreskerja.admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sistempakarstreskerja.MySingleton;
import com.sistempakarstreskerja.R;

import org.json.JSONException;
import org.json.JSONObject;

public class PenggunaTambahActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://streskerja.000webhostapp.com/tambah_pengguna.php";
    private EditText et_id_pengguna;
    private EditText et_nama_lengkap;
    private EditText et_username;
    private EditText et_password;
    private String id_pengguna;
    private String nama_lengkap;
    private String username;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengguna_tambah);
        setTitle("Tambah Pengguna");

        et_id_pengguna = findViewById(R.id.et_id_pengguna);
        et_nama_lengkap = findViewById(R.id.et_nama_lengkap);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);

        Button btn_simpan = findViewById(R.id.btn_simpan);

        btn_simpan.setOnClickListener(view -> {

            id_pengguna = et_id_pengguna.getText().toString().trim();
            nama_lengkap = et_nama_lengkap.getText().toString().trim();
            username = et_username.getText().toString();
            password = et_password.getText().toString();
            if (validateInputs()) {
                tambahPengguna();
            }
        });
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(PenggunaTambahActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void tambahPengguna() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("id_pengguna", id_pengguna);
            request.put("nama_lengkap", nama_lengkap);
            request.put("username", username);
            request.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, response -> {
                    pDialog.dismiss();
                    try {
                        if (response.getInt("status") == 0) {
                            Toast.makeText(getApplicationContext(),
                                    response.getString("message"), Toast.LENGTH_SHORT).show();
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
        TextView tvPasswordError = findViewById(R.id.tv_password_error);
        tvPasswordError.setVisibility(View.GONE); // Sembunyikan TextView pesan kesalahan awalnya

        if (id_pengguna.equals("")) {
            et_id_pengguna.setError("NIK tidak boleh kosong");
            et_id_pengguna.requestFocus();
            return false;
        }
        if (nama_lengkap.equals("")) {
            et_nama_lengkap.setError("Nama lengkap tidak boleh kosong");
            et_nama_lengkap.requestFocus();
            return false;
        }
        if (username.equals("")) {
            et_username.setError("username tidak boleh kosong");
            et_username.requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            tvPasswordError.setVisibility(View.VISIBLE); // Tampilkan TextView pesan kesalahan
            tvPasswordError.setText("Password tidak boleh kosong");
            et_password.requestFocus();
            return false;
        }
        return true;
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