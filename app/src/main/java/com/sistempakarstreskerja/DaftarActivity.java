package com.sistempakarstreskerja;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.InputType;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class DaftarActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    public static final String url = "https://streskerja.000webhostapp.com/daftar.php";
    private EditText et_nama_lengkap;
    private EditText et_username;
    private EditText et_password;
    private String nama_lengkap;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        setTitle("Daftar User");

        et_nama_lengkap = findViewById(R.id.et_nama_lengkap);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
//        CheckBox checkBoxShowPassword = findViewById(R.id.checkBox_show_password);

        Button login = findViewById(R.id.btn_login);
        Button daftar = findViewById(R.id.btn_daftar);

        // Kode CheckBox untuk menampilkan atau menyembunyikan sandi
//        checkBoxShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    // Tampilkan sandi
//                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//                } else {
//                    // Sembunyikan sandi
//                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                }
//            }
//        });

        daftar.setOnClickListener(v -> {
            nama_lengkap = et_nama_lengkap.getText().toString().trim();
            username = et_username.getText().toString().toLowerCase().trim();
            password = et_password.getText().toString().trim();
            if (validateInputs()) {
                daftar();
            }
        });

        login.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
        });
    }

    private boolean validateInputs() {
        TextView tvPasswordError = findViewById(R.id.tv_password_error);
        tvPasswordError.setVisibility(View.GONE); // Sembunyikan TextView pesan kesalahan awalnya

        if (nama_lengkap.equals("")) {
            et_nama_lengkap.setError("Nama Lengkap tidak boleh kosong");
            et_nama_lengkap.requestFocus();
            return false;
        }
        if (username.equals("")) {
            et_username.setError("Username tidak boleh kosong");
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

    private void displayLoader() {
        pDialog = new ProgressDialog(DaftarActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void daftar() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
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
                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i);
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

}