package com.sistempakarstreskerja.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.LayoutInflater;
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

import com.android.volley.Request;
import com.android.volley.request.JsonObjectRequest;
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

                            // Get the list of symptoms from the response
                            JSONArray symptomsArray = response.getJSONArray("daftar_gejala");
                            JSONArray nilaiCfArray = response.getJSONArray("nilai_cf"); // Retrieve nilai_cf array

                            // Create ArrayLists to store the symptoms and nilai_cf values
                            ArrayList<String> symptomsList = new ArrayList<>();
                            ArrayList<String> nilaiCfList = new ArrayList<>();

                            // Add each symptom and nilai_cf value to the lists
                            for (int i = 0; i < symptomsArray.length(); i++) {
                                symptomsList.add(symptomsArray.getString(i));
                                nilaiCfList.add(nilaiCfArray.getString(i));
                            }

                            // Create a custom ArrayAdapter to populate the ListView with symptoms and nilai_cf using the custom layout
                            ArrayAdapter<String> adapter = new CustomListAdapter(this, symptomsList, nilaiCfList);

                            // Set the adapter for the ListView
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

    // Custom ArrayAdapter to use the custom layout for the ListView items
    private static class CustomListAdapter extends ArrayAdapter<String> {

        private Context context;
        private ArrayList<String> symptomsList;
        private ArrayList<String> nilaiCfList; // New ArrayList to store nilai_cf values

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

            // Get the TextViews from the custom layout
            TextView tvListItem = listItemView.findViewById(R.id.tv_list_gejala);
            TextView tvNilaiCf = listItemView.findViewById(R.id.tv_nilai_cf);

            // Set the text for the TextViews
            String symptom = symptomsList.get(position);
            tvListItem.setText(symptom);

            String nilaiCf = nilaiCfList.get(position);
            tvNilaiCf.setText("Nilai CF : "  + nilaiCf); // Display nilai_cf in the list item

            return listItemView;
        }
    }
}
