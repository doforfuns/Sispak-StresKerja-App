package com.sistempakarstreskerja.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.VolleyError;
import com.sistempakarstreskerja.MySingleton;
import com.sistempakarstreskerja.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AturanViewActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://streskerja.000webhostapp.com/get_aturan.php";
    private TextView tv_nama_penyakit;
    private ListView listDaftarGejala;
    private String id_penyakit;
    private String nama_penyakit = "";

    private ArrayList<String> symptomsList = new ArrayList<>();
    private ArrayList<String> nilaiCfList = new ArrayList<>();

    private static final int EDIT_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aturan_view);
        setTitle("Detail Aturan");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_penyakit = extras.getString("id_penyakit");
        }

        tv_nama_penyakit = findViewById(R.id.tv_nama_penyakit);
        listDaftarGejala = findViewById(R.id.list_daftar_gejala);

        Button btn_atur_ulang = findViewById(R.id.btn_atur_ulang);

        btn_atur_ulang.setOnClickListener(view -> {
            Intent intent = new Intent(AturanViewActivity.this, AturanEditActivity.class);
            intent.putExtra("id_penyakit", id_penyakit);
            intent.putExtra("nama_penyakit", nama_penyakit);
            startActivity(intent);
        });

        // Set item click listener for the ListView
        listDaftarGejala.setOnItemClickListener((parent, view, position, id) -> {
            String selectedGejala = symptomsList.get(position);
            String selectedNilaiCf = nilaiCfList.get(position);

            Intent intent = new Intent(AturanViewActivity.this, CFEditActivity.class);
            intent.putExtra("selected_penyakit", tv_nama_penyakit.getText().toString()); // Send the selected penyakit name
            intent.putExtra("selected_gejala", selectedGejala);
            intent.putExtra("selected_nilai_cf", selectedNilaiCf);
            intent.putExtra("position", position);
            startActivityForResult(intent, EDIT_REQUEST_CODE);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAturan();
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

    private void getAturan() {
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
                            tv_nama_penyakit.setText(response.getString("nama_penyakit"));

                            JSONArray symptomsArray = response.getJSONArray("daftar_gejala");
                            JSONArray nilaiCfArray = response.getJSONArray("nilai_cf");

                            symptomsList.clear();
                            nilaiCfList.clear();

                            for (int i = 0; i < symptomsArray.length(); i++) {
                                symptomsList.add(symptomsArray.getString(i));
                                nilaiCfList.add(nilaiCfArray.getString(i));
                            }

                            CustomListAdapter adapter = new CustomListAdapter(this, symptomsList, nilaiCfList);
                            listDaftarGejala.setAdapter(adapter);

                            nama_penyakit = response.getString("nama_penyakit");
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
        pDialog = new ProgressDialog(AturanViewActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private static class CustomListAdapter extends ArrayAdapter<String> {

        private Context context;
        private ArrayList<String> symptomsList;
        private ArrayList<String> nilaiCfList;

        public CustomListAdapter(Context context, ArrayList<String> symptomsList, ArrayList<String> nilaiCfList) {
            super(context, R.layout.list_gejala_cf, symptomsList);
            this.context = context;
            this.symptomsList = symptomsList;
            this.nilaiCfList = nilaiCfList;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItemView = convertView;
            if (listItemView == null) {
                listItemView = LayoutInflater.from(context).inflate(R.layout.list_gejala_cf, parent, false);
            }

            TextView tvListItem = listItemView.findViewById(R.id.tv_list_gejala);
            TextView tvNilaiCf = listItemView.findViewById(R.id.tv_nilai_cf);

            String symptom = symptomsList.get(position);
            tvListItem.setText(symptom);

            String nilaiCf = nilaiCfList.get(position);
            tvNilaiCf.setText("Nilai CF : " + nilaiCf);

            return listItemView;
        }
    }

    private void updateNilaiCfInDatabase(String aturanId, String editedNilaiCf) {
        // Build the request JSON object
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("aturan_id", aturanId);
            requestObject.put("edited_nilai_cf", editedNilaiCf);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Make the request to update the nilai_cf in the database
        String url = "https://streskerja.000webhostapp.com/update_nilai_cf.php"; // Replace with the actual URL for update_nilai_cf.php
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            if (status == 0) {
                                Toast.makeText(AturanViewActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AturanViewActivity.this, "Failed to update nilai_cf", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AturanViewActivity.this, "Error updating nilai_cf: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Set a timeout for the request to prevent the application from freezing indefinitely
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,  // Timeout in milliseconds
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            String editedGejala = data.getStringExtra("edited_gejala");
            String editedNilaiCf = data.getStringExtra("edited_nilai_cf");
            int editedPosition = data.getIntExtra("edited_position", -1);

            if (editedPosition != -1) {
                symptomsList.set(editedPosition, editedGejala);
                nilaiCfList.set(editedPosition, editedNilaiCf);
                ((CustomListAdapter) listDaftarGejala.getAdapter()).notifyDataSetChanged();

                // Update the edited nilai_cf in the database
                String aturanId = ""; // Replace with the actual aturan_id of the edited item
                updateNilaiCfInDatabase(aturanId, editedNilaiCf);
            }
        }
    }
}
