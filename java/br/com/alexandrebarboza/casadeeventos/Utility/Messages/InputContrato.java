package br.com.alexandrebarboza.casadeeventos.Utility.Messages;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.alexandrebarboza.casadeeventos.R;
import br.com.alexandrebarboza.casadeeventos.Utility.Dates;
import br.com.alexandrebarboza.casadeeventos.Utility.Utility;

import static android.content.DialogInterface.BUTTON_NEGATIVE;

/**
 * Created by Alexandre on 13/04/2017.
 */

public class InputContrato implements DialogInterface.OnClickListener, Button.OnClickListener, EditText.OnFocusChangeListener {
    private Activity activity;
    private View view;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private EditText ed_data_ini;
    private EditText ed_data_fim;
    private EditText ed_hora_ini;
    private EditText ed_hora_fim;
    private EditText ed_min_ini;
    private EditText ed_min_fim;
    private Button   button_ok;
    private String   title;
    private boolean  focus;
    private class DataFilter implements TextWatcher {
        private EditText edit;
        private int     limit;

        private DataFilter(EditText edit, int limit) {
            this.edit  = edit;
            this.limit = limit;
        }

        public void setEdit(EditText edit) {
            this.edit = edit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int value = 0;
            String str = edit.getText().toString();
            try {
                value = Integer.parseInt(str);
            } catch (NumberFormatException e) {
                if (str.isEmpty()) {
                    return;
                } else {
                    e.printStackTrace();
                }
            }
            if (value > this.limit || value < 0) {
                this.edit.setText("");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (this.edit.getText().toString().isEmpty()) {
                return;
            }
            if (s.length() == 2) {
                if (this.edit.getId() == ed_hora_ini.getId()) {
                    ed_min_ini.requestFocus();
                } else if (this.edit.getId() == ed_hora_fim.getId()) {
                    ed_min_fim.requestFocus();
                } else if (this.edit.getId() == ed_min_ini.getId()) {
                    ed_hora_fim.requestFocus();
                } else if (this.edit.getId() == ed_min_fim.getId()) {
                    //ed_min_fim.clearFocus();
                    InputMethodManager im = (InputMethodManager) ed_min_fim.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(ed_min_fim.getWindowToken(), 0); // Oculta teclado.
                }
            }
        }
    }

    private class selecionaDataListener implements DatePickerDialog.OnDateSetListener {
        private EditText edit;

        public selecionaDataListener(EditText edit) {
            this.edit = edit;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            Date date = calendar.getTime();
            String str = Dates.ShortDateFromString(date.toString(), "en", "US");
            edit.setText(str);
            if (edit.getId() == ed_data_ini.getId()) {
                activity.getIntent().putExtra("DATA_INICIAL", str);
            } else if (edit.getId() == ed_data_fim.getId()) {
                activity.getIntent().putExtra("DATA_FINAL", str);
            }
        }
    }

    private void exibeData(Date dt, EditText ed) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        int year  = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day   = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(activity, new selecionaDataListener(ed), year, month, day);
        InputMethodManager im = (InputMethodManager) ed.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(ed.getWindowToken(), 0); // Oculta teclado.
        dialog.show();
    }

    public InputContrato(Activity activity, String title, String[] input) {
        this.activity = activity;
        this.title = title;
        this.inflater = LayoutInflater.from(activity);
        this.view = inflater.inflate(R.layout.message_input_contrato, null);
        this.ed_data_ini = (EditText) view.findViewById(R.id.edit_data_inicial);
        this.ed_data_fim = (EditText) view.findViewById(R.id.edit_data_final);
        this.ed_hora_ini = (EditText) view.findViewById(R.id.edit_hora_inicial);
        this.ed_hora_fim = (EditText) view.findViewById(R.id.edit_hora_final);
        this.ed_min_ini = (EditText) view.findViewById(R.id.edit_minuto_inicial);
        this.ed_min_fim = (EditText) view.findViewById(R.id.edit_minuto_final);

        ed_data_ini.setKeyListener(null);
        ed_data_fim.setKeyListener(null);

        ed_data_ini.setOnClickListener(this);
        ed_data_fim.setOnClickListener(this);

        DataFilter f1 = new DataFilter(ed_hora_ini, 23);
        DataFilter f2 = new DataFilter(ed_min_ini, 59);
        DataFilter f3 = new DataFilter(ed_hora_fim, 23);
        DataFilter f4 = new DataFilter(ed_min_fim, 59);

        ed_hora_ini.addTextChangedListener(f1);
        ed_min_ini.addTextChangedListener(f2);
        ed_hora_fim.addTextChangedListener(f3);
        ed_min_fim.addTextChangedListener(f4);

        ed_hora_ini.setOnFocusChangeListener(this);
        ed_min_ini.setOnFocusChangeListener(this);
        ed_hora_fim.setOnFocusChangeListener(this);
        ed_min_fim.setOnFocusChangeListener(this);

        if (input != null) {
            try {
                this.ed_data_ini.setText(input[0]);
                this.ed_hora_ini.setText(input[1]);
                this.ed_min_ini.setText(input[2]);
                this.ed_data_fim.setText(input[3]);
                this.ed_hora_fim.setText(input[4]);
                this.ed_min_fim.setText(input[5]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            focus = true;
        } else {
            focus = false;
        }
        ed_data_ini.setOnFocusChangeListener(this);
        ed_data_fim.setOnFocusChangeListener(this);
    }

    public void setInputContrato() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.str_ok, this);
        builder.setNegativeButton(R.string.str_cancel, this);
        dialog = builder.create();
        dialog.setOnDismissListener((DialogInterface.OnDismissListener) activity);
        dialog.setOnCancelListener((DialogInterface.OnCancelListener) activity);
        dialog.show();
        button_ok = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        button_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_NEGATIVE:
                AlertDialog ad = (AlertDialog) dialog;
                ad.setOnDismissListener(null);
                dialog.cancel();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(button_ok)) {
            String[] contrato = {ed_data_ini.getText().toString(), ed_hora_ini.getText().toString(), ed_min_ini.getText().toString(), ed_data_fim.getText().toString(), ed_hora_fim.getText().toString(), ed_min_fim.getText().toString()};
            if (!Utility.contratoIsEmpty(contrato)) {    // Nem todos os campos foram preenchidos.
                if (Utility.contratoIsValid(contrato)) { // Data e hora inicial menor que data e hora final.
                    String str_ini = Utility.contratoGetDateTime(contrato[0], contrato[1], contrato[2]);
                    String str_fim = Utility.contratoGetDateTime(contrato[3], contrato[4], contrato[5]);
                    Intent it = activity.getIntent();
                    it.putExtra("DATA_INICIAL", str_ini);
                    it.putExtra("DATA_FINAL", str_fim);
                    dialog.dismiss();
                } else {
                    Toast.makeText(activity, activity.getResources().getString(R.string.str_inf_time), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, activity.getResources().getString(R.string.str_full), Toast.LENGTH_SHORT).show();
            }
            return;
        }
        Date dt;
        switch (v.getId()) {
            case R.id.edit_data_inicial:
                if (activity.getIntent().hasExtra("DATA_INICIAL")) {
                    dt = Dates.StringToDate(activity.getIntent().getStringExtra("DATA_INICIAL"), "en", "US", false);
                } else {
                    dt = new Date();
                }
                exibeData(dt, ed_data_ini);
                break;
            case R.id.edit_data_final:
                if (activity.getIntent().hasExtra("DATA_FINAL")) {
                    dt = Dates.StringToDate(activity.getIntent().getStringExtra("DATA_FINAL"), "en", "US", false);
                } else {
                    dt = new Date();
                }
                exibeData(dt, ed_data_fim);
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        EditText et = (EditText) v;
        if (hasFocus) {
            if (v.getId() == R.id.edit_data_inicial || v.getId() == R.id.edit_data_final) {
                if (focus) {
                    onClick(v);
                } else {
                    focus = true;
                }
            }
        } else {
            if (v.getId() == R.id.edit_hora_inicial || v.getId() == R.id.edit_hora_final || v.getId() == R.id.edit_minuto_inicial || v.getId() == R.id.edit_minuto_final ) {
                et.setText(Utility.ZeroFill(et.getText().toString()));
            }
        }
    }
}
