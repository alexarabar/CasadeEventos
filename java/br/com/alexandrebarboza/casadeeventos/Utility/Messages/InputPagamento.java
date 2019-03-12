package br.com.alexandrebarboza.casadeeventos.Utility.Messages;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.NumberFormat;
import java.util.Locale;

import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Pagamento;
import br.com.alexandrebarboza.casadeeventos.R;
import br.com.alexandrebarboza.casadeeventos.Utility.Utility;

import static android.content.DialogInterface.BUTTON_NEGATIVE;

/**
 * Created by Alexandre on 25/04/2017.
 */

public class InputPagamento implements DialogInterface.OnClickListener, Button.OnClickListener {
    private Activity activity;
    private View view;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private EditText ed_pagamento_total;
    private EditText ed_pagamento_desconto;
    private EditText ed_valor_pago;
    private Button   button_ok;
    private String   title;
    private DataFilter  filter;
    private Pagamento   pagamento;

    private class DataFilter implements TextWatcher {
        private final Locale locale = new Locale("pt", "BR");
        private String current;

        public DataFilter() {
            current = null;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals(current) && !s.toString().isEmpty()) {
                ed_pagamento_desconto.removeTextChangedListener(this);
                String replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance(locale).getCurrency().getSymbol());
                String cleanString = s.toString().replaceAll(replaceable, "");
                double parsed;
                try {
                    parsed = Double.parseDouble(cleanString);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    parsed = 0.00;
                }
                if (parsed == 0) {
                    ed_pagamento_desconto.setText("");
                    ed_pagamento_desconto.setSelection(0);
                    ed_pagamento_desconto.addTextChangedListener(this);
                    ed_valor_pago.setText(ed_pagamento_total.getText().toString());
                    return;
                }
                String formatted = NumberFormat.getCurrencyInstance().format((parsed / 100));
                current = Utility.convertCurrencyForBR(formatted);
                ed_pagamento_desconto.setText(current);
                ed_pagamento_desconto.setSelection(current.length());
                ed_pagamento_desconto.addTextChangedListener(this);
                CalculaValorPago();
            }
        }
    }

    private void CalculaValorPago() {
        double db1 = Utility.getDoubleOfCurrencyForBR(ed_pagamento_total.getText().toString());
        double db2 = Utility.getDoubleOfCurrencyForBR(ed_pagamento_desconto.getText().toString());
        double res = db1 - db2;
        ed_valor_pago.setText("$" + Utility.convertCurrencyForBR(Utility.getDoubleFormatForUS(res, true)));
    }

    public InputPagamento(Activity activity, String title) {
        this.activity = activity;
        this.title = title;
        this.inflater = LayoutInflater.from(activity);
        this.view = inflater.inflate(R.layout.message_input_pagamento, null);

    }

    public void setInputPagamento(double total, Pagamento pagamento) {
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
        ed_pagamento_total = (EditText) view.findViewById(R.id.edit_pagamento_total);
        ed_pagamento_desconto  = (EditText) view.findViewById(R.id.edit_pagamento_desconto);
        ed_valor_pago  = (EditText) view.findViewById(R.id.edit_valor_pago);
        ed_pagamento_total.setKeyListener(null);
        ed_valor_pago.setKeyListener(null);
        filter = new DataFilter();
        ed_pagamento_desconto.addTextChangedListener(filter);
        ed_pagamento_total.setText("$" + Utility.convertCurrencyForBR(Utility.getDoubleFormatForUS(total, true)));
        Intent it = activity.getIntent();
        double val = 0.00;
        if (it.hasExtra("PAGAMENTO_DESCONTO")) {
            val = it.getDoubleExtra("PAGAMENTO_DESCONTO", 0.00);
        } else {
            if (pagamento.get_id() != 0) {
                val = pagamento.getDesconto();
            }
        }
        if (val != 0) {
            ed_pagamento_desconto.setText("$" + Utility.convertCurrencyForBR(Utility.getDoubleFormatForUS(val, true)));
        }
        this.pagamento = pagamento;
        CalculaValorPago();
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
            Intent it = activity.getIntent();
            double total    = Utility.getDoubleOfCurrencyForBR(ed_pagamento_total.getText().toString());
            double desconto = Utility.getDoubleOfCurrencyForBR(ed_pagamento_desconto.getText().toString());
            it.putExtra("PAGAMENTO_TOTAL", total);
            it.putExtra("PAGAMENTO_DESCONTO", desconto);
            dialog.dismiss();
        }
    }
}
