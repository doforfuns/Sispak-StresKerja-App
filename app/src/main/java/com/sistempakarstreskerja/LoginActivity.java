package com.sistempakarstreskerja;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    public static final String login_url = "https://streskerja.000webhostapp.com/login.php";
    private EditText et_username;
    private EditText et_password;
    private String username;
    private String password;
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        session = new SessionHandler(getApplicationContext());

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);

        Button login = findViewById(R.id.btn_login);
        Button daftar = findViewById(R.id.btn_daftar);

        login.setOnClickListener(v -> {
            username = et_username.getText().toString().toLowerCase().trim();
            password = et_password.getText().toString().trim();
            if (validateInputs()) {
                login();
            }
        });

        daftar.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), DaftarActivity.class);
            startActivity(i);
            finish();
        });
    }

    private boolean validateInputs() {
        TextView tvPasswordError = findViewById(R.id.tv_password_error);
        tvPasswordError.setVisibility(View.GONE); // Sembunyikan TextView pesan kesalahan awalnya

        if (username.isEmpty()) {
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
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void login() {
        displayLoader();

        JSONObject request = new JSONObject();
        try {
            request.put("username", username);
            request.put("password", password);
        } catch (JSONException e) {
            pDialog.dismiss();
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Terjadi kesalahan saat memproses data login.", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, login_url, request, response -> {
                    pDialog.dismiss();
                    try {
                        if (response.getInt("status") == 0) {
                            session.loginUser(response.getString("id_pengguna"),
                                    response.getString("nama_lengkap"));
                            if (response.getString("level").equals("Admin")) {
                                Intent i = new Intent(getApplicationContext(), AdminActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        } else {
                            String errorMessage = response.getString("message");
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Terjadi kesalahan dalam memproses respons server.", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
                    pDialog.dismiss();
                    String errorMessage = "Terjadi kesalahan dalam menghubungi server.";
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        try {
                            String errorData = new String(error.networkResponse.data, "UTF-8");
                            JSONObject errorObject = new JSONObject(errorData);
                            if (errorObject.has("message")) {
                                errorMessage = errorObject.getString("message");
                            }
                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                });

        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }


    // Metode ini akan dipanggil ketika TextView "forgot_password" diklik
    public void showContactAdminPopup(View view) {
        // Tampilkan AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Lupa Password?");
        builder.setMessage("Anda dapat menghubungi admin untuk mengganti password.");
        builder.setPositiveButton("Hubungi Admin", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openWhatsApp("0811155298");
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Biarkan kosong jika tidak ada tindakan khusus yang perlu dilakukan saat tombol "Batal" diklik.
            }
        });
        builder.show();
    }

    // Metode untuk membuka WhatsApp dengan nomor tujuan tertentu
    private void openWhatsApp(String phoneNumber) {
        String url = "https://api.whatsapp.com/send?phone=" + phoneNumber;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

}