package com.sistempakarstreskerja.admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sistempakarstreskerja.MySingleton;
import com.sistempakarstreskerja.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AturanEditActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://streskerja.000webhostapp.com/get_daftar_gejala.php";
    private static final String url_update = "https://streskerja.000webhostapp.com/update_aturan.php";
    private AturanEditAdapter dataAdapter = null;
    private ArrayList<Gejala> gejalaList = new ArrayList<>();
    private Gejala gejala;
    private String id_penyakit;
    private String nama_penyakit = "";
    private StringBuffer responseText;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aturan_edit);
        setTitle("Atur Ulang Gejala");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_penyakit = extras.getString("id_penyakit");
            nama_penyakit = extras.getString("nama_penyakit");
        }

        TextView tv_nama_penyakit = findViewById(R.id.tv_nama_penyakit);
        tv_nama_penyakit.setText(nama_penyakit);

        recyclerView = findViewById(R.id.lv_gejala);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getDaftarGejala();

        FloatingActionButton fabSimpan = findViewById(R.id.btn_simpan);
        fabSimpan.setOnClickListener(v -> {
            responseText = new StringBuffer();
            ArrayList<Double> cfValues = new ArrayList<>(); // List untuk menyimpan nilai_cf

            if (!gejalaList.isEmpty()) {
                for (int i = 0; i < gejalaList.size(); i++) {
                    Gejala gejala = gejalaList.get(i);
                    if (gejala.isSelected()) {
                        responseText.append(gejala.getName()).append("#");
                        // Ambil nilai_cf dari EditText dan tambahkan ke cfValues
                        double cfValue = gejala.getNilaiCf();
                        cfValues.add(cfValue);
                    }
                }
            }

            if (!dataAdapter.hasSelectedGejala()) {
                showNoGejalaSelectedWarning();
                return; // Jika tidak ada gejala yang dipilih, hentikan proses penyimpanan
            }
            if (dataAdapter.hasEmptyOrInvalidCF()) {
                showCFEmptyWarning(); // Show the warning dialog
            } else {
                updateAturan(responseText.toString(), cfValues); // Kirim nilai_cf ke method updateAturan()
            }

            // Sembunyikan keyboard setelah menekan tombol simpan
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            View focusedView = getCurrentFocus();
            if (focusedView != null) {
                imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
            }
        });
    }

    private void getDaftarGejala() {
        displayLoader();
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, response -> {
                    pDialog.dismiss();
                    try {
                        if (response.getInt("status") == 0) {
                            gejalaList = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("gejala");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String name = jsonObject.getString("nama_gejala");
                                gejala = new Gejala(name, false);
                                gejalaList.add(gejala);
                            }
                            dataAdapter = new AturanEditAdapter(this, gejalaList);
                            recyclerView.setAdapter(dataAdapter);
                            // Add DividerItemDecoration to show dividers between items
                            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
                            recyclerView.addItemDecoration(dividerItemDecoration);
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
        pDialog = new ProgressDialog(AturanEditActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void updateAturan(String daftar_gejala, ArrayList<Double> cfValues) {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("id_penyakit", id_penyakit);
            request.put("daftar_gejala", daftar_gejala);

            // Konversi ArrayList cfValues menjadi String dan pisahkan dengan tanda "#"
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < cfValues.size(); i++) {
                sb.append(cfValues.get(i));
                if (i < cfValues.size() - 1) {
                    sb.append("#");
                }
            }
            String nilai_cf = sb.toString();
            request.put("nilai_cf", nilai_cf);
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

    private void showCFEmptyWarning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AturanEditActivity.this);
        builder.setTitle("Peringatan!");
        builder.setMessage("Pastikan semua gejala yang terpilih memiliki nilai CF Pakar yang valid!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void showNoGejalaSelectedWarning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Peringatan!");
        builder.setMessage("Harap pilih daftar gejala diagnosa yang sesuai dengan arahan pakar!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }
}
