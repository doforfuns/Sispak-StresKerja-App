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
import android.view.inputmethod.InputMethodManager;

import com.sistempakarstreskerja.R;

import java.util.ArrayList;

public class AturanEditAdapter extends ArrayAdapter<Gejala> {

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

    public AturanEditAdapter(Context context, int textViewResourceId,
                             ArrayList<Gejala> gejalaList) {
        super(context, textViewResourceId, gejalaList);
        this.gejalaList = new ArrayList<>();
        this.gejalaList.addAll(gejalaList);

    }

    private static class ViewHolder {
        CheckBox name;
        EditText et_certainty_factor;
    }

    public ArrayList<Gejala> getGejalaList() {
        return gejalaList;
    }

    // In AturanEditAdapter class
    public boolean hasEmptyOrInvalidCF() {
        for (Gejala gejala : gejalaList) {
            if (gejala.isSelected()) {
                double cfValue = gejala.getNilaiCf();
                // Check if the CF value is empty or not within the valid range (0.1 to 1.0)
                if (cfValue == 0.0 || cfValue < 0.1 || cfValue > 1.0) {
                    return true; // There is at least one selected symptom with an empty or invalid CF value
                }
            }
        }
        return false; // All selected symptoms have valid CF values
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
                    holder.et_certainty_factor.requestFocus(); // Set fokus ke EditText nilai CF
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(holder.et_certainty_factor, InputMethodManager.SHOW_IMPLICIT); // Tampilkan keyboard
                } else {
                    holder.et_certainty_factor.setVisibility(View.GONE);
                    // Clear the nilai_cf when hiding the EditText
                    holder.et_certainty_factor.setText("");
                }

                // Check if this is the first gejala being selected
                if (!toastShown && cb.isChecked()) {
                    showToastOnce(getContext(), "Harap masukkan nilai CF Pakar yang valid!");
                    toastShown = true; // Set the flag to true to prevent showing the Toast again
                }
            });


            holder.et_certainty_factor.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    EditText et_cf = (EditText) v;
                    String cfValueStr = et_cf.getText().toString();
                    if (!cfValueStr.isEmpty()) {
                        double cfValue = 0.0;
                        try {
                            cfValue = Double.parseDouble(cfValueStr);
                            Gejala gejala = (Gejala) holder.name.getTag();
                            gejala.setNilaiCf(cfValue);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            // Show an error message or handle the invalid input accordingly
                            // For example, you can display a Toast message
                            showToastOnce(getContext(), "Masukkan nilai CF Pakar yang valid!");
                        }
                    } else {
                        // If the EditText is empty, do not change the nilai_cf
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
            // Set the EditText value to the existing CF value if available
            // Otherwise, set it to an empty string
            holder.et_certainty_factor.setText(gejala.getNilaiCf() != 0.0 ? String.valueOf(gejala.getNilaiCf()) : "");
        } else {
            holder.et_certainty_factor.setVisibility(View.GONE);
            // Clear the EditText text when hiding the EditText for non-selected Gejala
            holder.et_certainty_factor.setText("");
        }

        return convertView;
    }
}