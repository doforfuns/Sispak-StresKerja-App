package com.sistempakarstreskerja;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.sistempakarstreskerja.admin.PanduanUserActivity;

public class MainActivity extends AppCompatActivity {

    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Menu Utama User");

        session = new SessionHandler(getApplicationContext());

    // Menyambut pengguna yang login
        User user = session.getUserDetails();
        String username = user.getNamaLengkap(); // Mengambil username dari session
        String greetingMessage = "Halo " + username + "!\nSelamat Datang";
// Pesan sapaan dengan baris baru setelah nama pengguna


        // Mengubah teks pada TextView
        TextView greetingTextView = findViewById(R.id.textView2);
        greetingTextView.setText(greetingMessage);

    // Menampilkan pesan sapaan pada saat pengguna berhasil login
        //Toast.makeText(MainActivity.this, greetingMessage, Toast.LENGTH_LONG).show();


    //        Button btn_diagnosa_forward = findViewById(R.id.btn_diagnosa_forward);
        Button btn_diagnosa_cf = findViewById(R.id.btn_diagnosa_cf);
        Button btn_riwayat = findViewById(R.id.btn_riwayat);
        Button btn_penyakit = findViewById(R.id.btn_penyakit);
        Button btn_panduanuser = findViewById(R.id.btn_panduanuser);
        Button btn_logout = findViewById(R.id.btn_logout);

//        btn_diagnosa_forward.setOnClickListener(view -> {
//            Intent intent = new Intent(MainActivity.this, DiagnosaForwardActivity.class);
//            startActivity(intent);
//        });

        btn_diagnosa_cf.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, DiagnosaCfActivity.class);
            startActivity(intent);
        });

        btn_riwayat.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RiwayatActivity.class);
            startActivity(intent);
        });

        btn_penyakit.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PenyakitActivity.class);
            startActivity(intent);
        });

        btn_panduanuser.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PanduanUserActivity.class);
            startActivity(intent);
        });

        btn_logout.setOnClickListener(view -> new AlertDialog.Builder(MainActivity.this)
                .setTitle("Konfirmasi")
                .setMessage("Anda yakin mau logout ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Ya, Logout", (dialog, whichButton) -> {
                    session.logoutUser();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Tidak", null).show());

    }

}