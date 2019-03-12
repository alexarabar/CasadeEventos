package br.com.alexandrebarboza.casadeeventos.Utility.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import br.com.alexandrebarboza.casadeeventos.R;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Tipo;
import br.com.alexandrebarboza.casadeeventos.Utility.Messages.Input;
import br.com.alexandrebarboza.casadeeventos.Utility.Utility;

/**
 * Created by Alexandre on 19/04/2017.
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class TipoAdapter extends ArrayAdapter<Tipo> implements View.OnClickListener, View.OnScrollChangeListener, RadioGroup.OnCheckedChangeListener{
    private Context  context;
    private int      resource;
    private Activity activity;
    private Intent   intent;
    private RadioGroup   radio_group;
    private RadioButton radio_button;
    private String     default_text;

    public TipoAdapter(Context context, int resource) {
        super(context, resource);
        this.context     = context;
        this.resource    = resource;
        this.activity = (Activity) context;
        this.intent = activity.getIntent();
        this.radio_group = new RadioGroup(context);
        this.radio_group.setOnCheckedChangeListener(this);
        this.default_text = intent.getStringExtra("INPUT");
        ListView list_view = (ListView) activity.findViewById(R.id.list_tipos);
        list_view.setOnScrollChangeListener(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        ViewHolder holder;
        radio_button = new RadioButton(context);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_tipos, parent, false);
            holder = new ViewHolder();
            convertView.setTag(holder);
            try {
                radio_group.addView(radio_button, position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Tipo tipo = getItem(position);
        holder.radio_tipo = (RadioButton) convertView.findViewById(R.id.radio_tipo);
        holder.radio_tipo.setOnClickListener(this);
        holder.text_tipo = (TextView) convertView.findViewById(R.id.text_tipo);
        holder.text_tipo.setOnClickListener(this);
        holder.text_tipo.setText(tipo.getDescricao());
        holder.radio_tipo.setTag(holder.text_tipo);
        radio_button.setTag(holder.radio_tipo);
        if (!default_text.isEmpty() && holder.text_tipo.getText().toString().equals(default_text)) {
            holder.radio_tipo.setChecked(true);
        } else {
            holder.radio_tipo.setChecked(false);
        }
        radio_button.setChecked(holder.radio_tipo.isChecked());
        return convertView;
    }

    @Override
    public void onClick(View v) {
        TextView tmp;
        String   txt;
        switch (v.getId()) {
            case R.id.radio_tipo:
                tmp = (TextView) v.getTag();
                txt = tmp.getText().toString();
                intent.putExtra("INPUT", txt);
                intent.putExtra("ACTION", 0);
                intent.putExtra("OLD", "");
                RadioButton rb1 = (RadioButton) v;
                if (rb1.isChecked()) {
                    Utility.clearAllRadioButtons(radio_group);
                    rb1.setChecked(true);
                }
                RadioButton rb2 = Utility.getRadioButton(radio_group, rb1);
                if (rb2 != null) {
                    rb2.setChecked(rb1.isChecked());
                }
                break;
            case R.id.text_tipo:
                tmp = (TextView) v;
                txt = tmp.getText().toString();
                intent.putExtra("ACTION", 1);
                intent.putExtra("OLD", txt);
                Input input = new Input(activity, context.getResources().getString(R.string.str_tipo), txt, 25);
                input.setInput();
                break;
        }
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        String in  = intent.getStringExtra("INPUT");
        if (in != null && !in.isEmpty()) {
            Utility.clearAllRadioButtons(radio_group);
            Utility.setRadioButton(in, radio_group);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        RadioButton rb1 = (RadioButton) group.findViewById(checkedId);
        RadioButton rb2 = (RadioButton) rb1.getTag();
        TextView tv = (TextView) rb2.getTag();
        String str = tv.getText().toString();
        String in  = intent.getStringExtra("INPUT");
        if (in.isEmpty()) {
            if (rb1.isChecked()) {
                intent.putExtra("INPUT", str);
                intent.putExtra("ACTION", 0);
                intent.putExtra("OLD", "");
            }
        }
    }
    static class ViewHolder {
        RadioButton radio_tipo;
        TextView    text_tipo;
    }
}
