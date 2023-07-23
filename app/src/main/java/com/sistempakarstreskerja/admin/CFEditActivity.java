package com.sistempakarstreskerja.admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sistempakarstreskerja.MySingleton;
import com.sistempakarstreskerja.R;

import org.json.JSONException;
import org.json.JSONObject;

public class CFEditActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://streskerja.000webhostapp.com/get_nilai_cf.php";
    private static final String url_update = "https://streskerja.000webhostapp.com/update_nilai_cf.php";
    private static final String url_delete = "https://streskerja.000webhostapp.com/delete_nilai_cf.php";
    private EditText tv_nama_penyakit;
    private EditText tv_nama_gejala;
    private EditText edit_nilai_cf;
    private String penyakit_nama;
    private String nama_gejala;
    private String nilai_cf;
    private String id_aturan;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cf_edit);
        setTitle("Ubah Nilai Aturan CF");

        // Enable back button in ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_aturan = extras.getString("selected_id_aturan");
        }

        tv_nama_penyakit = findViewById(R.id.tv_nama_penyakit);
        tv_nama_gejala = findViewById(R.id.tv_nama_gejala);
        edit_nilai_cf = findViewById(R.id.edit_nilai_cf);

        Button btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(view -> {
            penyakit_nama = tv_nama_penyakit.getText().toString().trim();
            nama_gejala = tv_nama_gejala.getText().toString().trim();
            nilai_cf = edit_nilai_cf.getText().toString();
            if (validateInputs()) {
                ubahCF();
            }
        });

        getCF();
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
            new AlertDialog.Builder(CFEditActivity.this)
                    .setTitle("Konfirmasi")
                    .setMessage("Anda yakin gejala akan dihapus ?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Ya, Hapus", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            hapusCF();
                            finish();
                        }
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



    private void hapusCF() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("id_aturan", id_aturan);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url_delete, request, response -> {
                    pDialog.dismiss();
                    try {
                        Toast.makeText(getApplicationContext(),
                                response.getString("message"), Toast.LENGTH_SHORT).show();

                        // Refresh the list view after successful deletion
                        if (response.getInt("status") == 0) {
                            int deletedPosition = getIntent().getIntExtra("position", -1);
                            if (deletedPosition != -1) {
                                // Set the result and pass back the position of the deleted item
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("deleted_position", deletedPosition);
                                setResult(RESULT_OK, resultIntent);
                            }
                            finish();
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


    private void getCF() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("id_aturan", id_aturan);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, response -> {
                    pDialog.dismiss();
                    try {
                        if (response.getInt("status") == 0) {
                            tv_nama_penyakit.setText(response.getString("nama_penyakit"));
                            tv_nama_gejala.setText(response.getString("nama_gejala"));
                            edit_nilai_cf.setText(response.getString("nilai_cf"));
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
        pDialog = new ProgressDialog(CFEditActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void ubahCF() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("id_aturan", id_aturan);
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
                            // Set the result and pass back the updated position
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("updated_position", getIntent().getIntExtra("position", -1));
                            setResult(RESULT_OK, resultIntent);
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
        if (penyakit_nama.equals("")) {
            tv_nama_penyakit.setError("Nama Penyakit tidak boleh kosong");
            tv_nama_penyakit.requestFocus();
            return false;
        }
        if (nama_gejala.equals("")) {
            tv_nama_gejala.setError("Nama Gejala tidak boleh kosong");
            tv_nama_gejala.requestFocus();
            return false;
        }
        if (nilai_cf.equals("")) {
            edit_nilai_cf.setError("Nilai CF tidak boleh kosong");
            edit_nilai_cf.requestFocus();
            return false;
        }
        // Validasi nilai_cf harus berada dalam rentang 0.1 hingga 1
        double cfValue;
        try {
            cfValue = Double.parseDouble(nilai_cf);
        } catch (NumberFormatException e) {
            edit_nilai_cf.setError("Masukkan nilai angka yang valid");
            edit_nilai_cf.requestFocus();
            return false;
        }

        if (cfValue <= 0.0 || cfValue > 1.0) {
            edit_nilai_cf.setError("Harap masukkan nilai CF Pakar yang valid!");
            edit_nilai_cf.requestFocus();
            return false;
        }
        return true;
    }
}



//    private EditText editNilaiCf;
//    private TextView tvNamaPenyakit;
//    private TextView tvNamaGejala;
//    private int position;
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == android.R.id.home) {
//            onBackPressed();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cf_edit);
//        setTitle("Ubah Nilai CF Rule");
//
//        // Enable back button in ActionBar
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        // Get data from Intent
//        Intent intent = getIntent();
//        String selectedPenyakit = intent.getStringExtra("selected_penyakit");
//        String selectedGejala = intent.getStringExtra("selected_gejala");
//        String selectedNilaiCf = intent.getStringExtra("selected_nilai_cf");
//        position = intent.getIntExtra("position", -1);
//
//        // Find views
//        tvNamaPenyakit = findViewById(R.id.tv_nama_penyakit);
//        tvNamaGejala = findViewById(R.id.tv_nama_gejala);
//        editNilaiCf = findViewById(R.id.edit_nilai_cf);
//
//        // Set data to TextViews
//        tvNamaPenyakit.setText(selectedPenyakit);
//        tvNamaGejala.setText(selectedGejala);
//        editNilaiCf.setText(selectedNilaiCf);
//
//        Button btnSave = findViewById(R.id.btn_save);
//        btnSave.setOnClickListener(view -> {
//            float editedNilaiCf = Float.parseFloat(editNilaiCf.getText().toString());
//
//            // Send request to the server to update nilai_cf
//            updateNilaiCf(editedNilaiCf);
//        });
//    }
//
//    private void updateNilaiCf(float editedNilaiCf) {
//        // Build the request JSON object
//        JSONObject requestObject = new JSONObject();
//        try {
//            requestObject.put("aturan_id", getAturanIdFromPosition(position));
//            requestObject.put("edited_nilai_cf", editedNilaiCf);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        // Make the request to update the nilai_cf in the database
//        String url = "https://streskerja.000webhostapp.com/update_nilai_cf.php";
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.POST,
//                url,
//                requestObject,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            int status = response.getInt("status");
//                            if (status == 0) {
//                                Toast.makeText(CFEditActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
//                                setResult(RESULT_OK);
//                                finish();
//                            } else {
//                                Toast.makeText(CFEditActivity.this, "Failed to update nilai_cf", Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(CFEditActivity.this, "Error updating nilai_cf: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        );
//
//        // Set a timeout for the request to prevent the application from freezing indefinitely
//        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
//                5000,  // Timeout in milliseconds
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        ));
//
//        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
//    }
//
//    // This method is to get the aturan_id from position in the list
//    // Replace this with the appropriate logic to get the aturan_id based on your data structure
//    private String getAturanIdFromPosition(int position) {
//        // Replace this with the logic to get the aturan_id based on the position in the list
//        // For example, you can use your symptomsList and nilaiCfList to get the aturan_id.
//        // It depends on how your data is structured.
//        // For now, this method just returns an empty string.
//        return "";
//    }
//}
