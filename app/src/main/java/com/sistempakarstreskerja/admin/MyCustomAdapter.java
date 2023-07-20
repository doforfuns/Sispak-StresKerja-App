package com.sistempakarstreskerja.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.view.Gravity;

import com.sistempakarstreskerja.R;

import java.util.ArrayList;

public class MyCustomAdapter extends ArrayAdapter<Gejala> {

    private ArrayList<Gejala> gejalaList;

    private static boolean toastShown = false; // Static flag to track whether the toast has been shown

    private static void showToastOnce(Context context, String message) {
        if (!toastShown) {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0); // Set gravity to center
            toast.show();
            toastShown = true; // Set the flag to true to prevent showing the Toast again
        }
    }
    public MyCustomAdapter(Context context, int textViewResourceId,
                           ArrayList<Gejala> gejalaList) {
        super(context, textViewResourceId, gejalaList);
        this.gejalaList = new ArrayList<>();
        this.gejalaList.addAll(gejalaList);

        // Set nilai default untuk nilai CF ketika objek Gejala baru ditambahkan
        for (Gejala gejala : this.gejalaList) {
            if (gejala.getNilaiCf() == 0.0) {
                gejala.setNilaiCf(0.0);
            }
        }
    }

    private static class ViewHolder {
        CheckBox name;
        EditText et_certainty_factor;
    }

    public ArrayList<Gejala> getGejalaList() {
        return gejalaList;
    }

    public boolean hasEmptyCF() {
        for (Gejala gejala : gejalaList) {
            if (gejala.isSelected() && gejala.getNilaiCf() == 0.0) {
                return true;
            }
        }
        return false;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.gejala_list_ceklis, null);

            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.checkBox1);
            holder.et_certainty_factor = convertView.findViewById(R.id.et_certainty_factor);
            convertView.setTag(holder);

            holder.name.setOnClickListener(v -> {
                CheckBox cb = (CheckBox) v;
                Gejala gejala = (Gejala) cb.getTag();
                gejala.setSelected(cb.isChecked());
                if (cb.isChecked()) {
                    holder.et_certainty_factor.setVisibility(View.VISIBLE);
                } else {
                    holder.et_certainty_factor.setVisibility(View.GONE);
                    // Reset the value to 0.0 when hiding the EditText
                    gejala.setNilaiCf(0.0);
                    holder.et_certainty_factor.setText(""); // Clear the EditText text
                }

                // Check if this is the first gejala being selected
                if (!toastShown && cb.isChecked()) {
                    showToastOnce(getContext(), "Harap masukkan nilai CF yang valid!");
                    toastShown = true; // Set the flag to true to prevent showing the Toast again
                }
            });

            holder.et_certainty_factor.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    EditText et_cf = (EditText) v;
                    String cfValueStr = et_cf.getText().toString();
                    if (!cfValueStr.isEmpty()) { // Check if the EditText is not empty
                        double cfValue = 0.0;
                        try {
                            cfValue = Double.parseDouble(cfValueStr);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        Gejala gejala = (Gejala) holder.name.getTag();
                        gejala.setNilaiCf(cfValue);
                    } else {
                        // Show an error message or handle the empty input accordingly
                        // For example, you can display a Toast message
                        if (!toastShown) {
                            Toast toast = Toast.makeText(getContext(), "Harap masukkan nilai CF yang valid!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0); // Set gravity to center
                            toast.show();
                            toastShown = true; // Set the flag to true so that the toast won't be shown again
                        }
                        // If you want to reset the value to 0.0 when EditText is empty, uncomment the line below
                        // gejala.setNilaiCf(0.0);
                    }
                }
            });

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Gejala gejala = gejalaList.get(position);
        holder.name.setText(gejala.getName());
        holder.name.setChecked(gejala.isSelected());
        holder.name.setTag(gejala);

        // Set the visibility and value of EditText based on the isSelected() state
        if (gejala.isSelected()) {
            holder.et_certainty_factor.setVisibility(View.VISIBLE);
            holder.et_certainty_factor.setText(String.valueOf(gejala.getNilaiCf()));
        } else {
            holder.et_certainty_factor.setVisibility(View.GONE);
            // Reset the value to 0.0 when hiding the EditText
            gejala.setNilaiCf(0.0);
            holder.et_certainty_factor.setText(""); // Clear the EditText text
        }

        return convertView;
    }
}
