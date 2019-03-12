package br.com.alexandrebarboza.casadeeventos.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Contrato;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Email;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Endereco;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Servico;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Telefone;
import br.com.alexandrebarboza.casadeeventos.R;

/**
 * Created by Alexandre on 11/04/2017.
 */

public class Utility {
    public static void resetImages(boolean flag, ImageButton ib_update, ImageButton ib_delete) {
        if (flag) {
            if (ib_update != null) {
                ib_update.setImageResource(R.drawable.ic_update);
            }
            if (ib_delete != null) {
                ib_delete.setImageResource(R.drawable.ic_delete);
            }
        } else {
            if (ib_update != null) {
                ib_update.setImageResource(R.drawable.ic_update_disabled);
            }
            if (ib_delete != null) {
                ib_delete.setImageResource(R.drawable.ic_delete_disabled);
            }
        }
        if (ib_update != null) {
            ib_update.setEnabled(flag);
        }
        if (ib_delete != null) {
            ib_delete.setEnabled(flag);
        }
    }

    public static void resetImages(int count, ImageButton ib_update, ImageButton ib_delete) {
        if (count == 0) {
            ib_update.setImageResource(R.drawable.ic_update_disabled);
            ib_delete.setImageResource(R.drawable.ic_delete_disabled);
            ib_update.setEnabled(false);
            ib_delete.setEnabled(false);
        } else {
            if (count == 1) {
                ib_update.setImageResource(R.drawable.ic_update);
                ib_delete.setImageResource(R.drawable.ic_delete);
                ib_update.setEnabled(true);
                ib_delete.setEnabled(true);
            }
        }
    }

    public static boolean enderecoIsEmpty(String[] values) {
        try {
            if (values[0].trim().isEmpty() ||
                    values[1].trim().isEmpty() ||
                    values[2].trim().isEmpty() ||
                    values[3].trim().isEmpty() ||
                    values[4].trim().isEmpty() ||
                    values[5].trim().isEmpty() ||
                    values[6].trim().isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void setUF(ArrayAdapter<String> adapter) {
        adapter.add("AC");
        adapter.add("AL");
        adapter.add("AP");
        adapter.add("AM");
        adapter.add("BA");
        adapter.add("CE");
        adapter.add("DF");
        adapter.add("ES");
        adapter.add("GO");
        adapter.add("MA");
        adapter.add("MT");
        adapter.add("MS");
        adapter.add("MG");
        adapter.add("PA");
        adapter.add("PB");
        adapter.add("PR");
        adapter.add("PE");
        adapter.add("PI");
        adapter.add("RJ");
        adapter.add("RN");
        adapter.add("RS");
        adapter.add("RO");
        adapter.add("RR");
        adapter.add("SC");
        adapter.add("SP");
        adapter.add("SE");
        adapter.add("TO");
    }

    public static double getDoubleOfCurrencyForBR(String str) {
        if (str.isEmpty()) {
            return 0.00;
        }
        boolean flag;
        do {
            try {
                flag = false;
                Integer.parseInt(str.substring(0, 1));
            } catch (NumberFormatException e) {
                str = str.substring(1);
                flag = true;
            }
        } while (flag == true);
        String[] vet = str.split(",");
        String integer, decimal;
        Double result;
        if (vet.length == 2) {
            integer = vet[0];
            decimal = vet[1];
        } else {
            if (!str.isEmpty()) {
                try {
                    result = Double.parseDouble(str);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    result = 0.0;
                }
                return result;
            } else {
                return 0.00;
            }
        }
        flag  = false;
        String    tmp = "";
        vet = integer.split("\\.");
        for (int i = 0; i < vet.length; i++) {
            tmp += vet[i];
            flag = true;
        }
        if (!flag) {
            tmp = integer;
        }
        tmp += "." + decimal;
        try {
            result = Double.parseDouble(tmp);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            result = 0.00;
        }
        return result;
    }

    public static String getDoubleFormatForUS(double dbl, boolean commas) {
        String f;
        if (commas) {
            f = "%1$,.2f";
        } else {
            f = "%1$.2f";
        }
        return String.format(f, dbl);
    }

    public static String convertCurrencyForBR(String str) {
        /*
        while (!Character.isDigit(str.charAt(0))) {
            str = str.substring(1, str.length());
        }
        */
        String[] vet = str.split("\\.");
        String integer, decimal;
        if (vet.length == 2) {
            integer = vet[0];
            decimal = vet[1];
        } else {
            return str;
        }
        boolean flag  = false;
        String result = "";
        vet = integer.split(",");
        for (int i = 0; i < vet.length; i++) {
             result += vet[i] + ".";
             flag = true;
        }
        if (flag) {
            result = result.substring(0, result.length() - 1);
        } else {
            result = integer;
        }
        result += "," + decimal;
        return result;
    }

    public static void enderecoToArray(String[] array, Endereco endereco) {
        try {
            array[0] = endereco.getLogradouro();
            array[1] = String.valueOf(endereco.getNumero());
            array[2] = endereco.getComplemento();
            array[3] = endereco.getBairro();
            array[4] = endereco.getCidade();
            array[5] = endereco.getUf();
            array[6] = String.valueOf(endereco.getCep());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteData(Spinner sp_data, ArrayAdapter<String> ad_data, ImageButton ib_update, ImageButton ib_delete) {
        ad_data.remove(sp_data.getSelectedItem().toString());
        ad_data.notifyDataSetChanged();
        resetImages(ad_data.getCount(), ib_update, ib_delete);
    }

    public static boolean updateData(Activity activity, String str, Spinner sp_data, ArrayAdapter<String> ad_data, String field, String found) {
        if (ad_data.getPosition(str) == -1) {      // Não existe.
            ad_data.remove(sp_data.getSelectedItem().toString());
            ad_data.add(str);
            return true;
        }
        if (!sp_data.getSelectedItem().toString().equals(str)) {
            field = field.substring(0, field.length() - 1) + " ";
            Toast.makeText(activity, field + found, Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public static boolean insertData(Activity activity, String str, ArrayAdapter<String> ad_data, ImageButton ib_update, ImageButton ib_delete, String field, String found, String empty) {
        if (!str.trim().isEmpty()) {
            if (ad_data.getPosition(str) == -1) { // Não existe.
                ad_data.add(str);
                resetImages(ad_data.getCount(), ib_update, ib_delete);
                return true;
            }
            field = field.substring(0, field.length() -1) + " ";
            Toast.makeText(activity, field + found, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, empty, Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public static void deleteEndereco(Spinner sp_endereco, ArrayAdapter<String> ad_endereco, ArrayAdapter<Endereco> enderecos, ImageButton ib_update_endereco, ImageButton ib_delete_endereco) {
        Endereco endereco = enderecos.getItem(sp_endereco.getSelectedItemPosition());
        enderecos.remove(endereco);
        ad_endereco.remove(sp_endereco.getSelectedItem().toString());
        ad_endereco.notifyDataSetChanged();
        resetImages(ad_endereco.getCount(), ib_update_endereco, ib_delete_endereco);
    }

    public static boolean updateEndereco(Activity activity, Intent intent, Spinner sp_endereco, ArrayAdapter<String> ad_endereco, ArrayAdapter<Endereco> enderecos, String field, String found, String[] vec) {
        String   text;
        String[] array = {"", "", "", "", "", "", ""};
        Endereco endereco;
        enderecoLoad(intent, array);
        if (!enderecoIsEmpty(array)) {
            endereco = enderecos.getItem(sp_endereco.getSelectedItemPosition());
            if (!enderecoExists(enderecos, array)) {  // Não existe;
                text = enderecoReduce(array);
                enderecoUpdate(endereco, array);
                ad_endereco.remove(sp_endereco.getSelectedItem().toString());
                ad_endereco.add(text);
                vec[0] = text;
                return true;
            }
            if (!enderecoComparator(enderecos.getItem(sp_endereco.getSelectedItemPosition()), array)) {
                field = field.substring(0, field.length() - 1) + " ";
                Toast.makeText(activity, field + found, Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    public static boolean insertEndereco(Activity activity, Intent intent, ArrayAdapter <String> ad_endereco, ArrayAdapter <Endereco> enderecos, ImageButton ib_update_endereco, ImageButton ib_delete_endereco, String field, String found, String[] vec) {
        String   text;
        String[] array = {"", "", "", "", "", "", ""};
        enderecoLoad(intent, array);
        if (!enderecoIsEmpty(array)) {
            if (!enderecoExists(enderecos, array)) {  // Não existe;
                Endereco result = enderecoInsert(array);
                text = enderecoReduce(result);
                enderecos.add(result);
                ad_endereco.add(text);
                resetImages(ad_endereco.getCount(), ib_update_endereco, ib_delete_endereco);
                vec[0] = text;
                return true;
            }
            field = field.substring(0, field.length() -1) + " ";
            Toast.makeText(activity, field + found, Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public static void enderecoLoad(Intent intent, String[] values) {
        try {
            values[0] = intent.getStringExtra("LOGRADOURO");
            values[1] = intent.getStringExtra("NUMERO");
            values[2] = intent.getStringExtra("COMPLEMENTO");
            values[3] = intent.getStringExtra("BAIRRO");
            values[4] = intent.getStringExtra("CIDADE");
            values[5] = intent.getStringExtra("UF");
            values[6] = intent.getStringExtra("CEP");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean enderecoExists(ArrayAdapter <Endereco> enderecos, String[] values) {
        try {
            for (int i = 0; i < enderecos.getCount(); i++) {
                if (enderecos.getItem(i).getLogradouro().equals(values[0]) &&
                        String.valueOf(enderecos.getItem(i).getNumero()).equals(values[1]) &&
                        enderecos.getItem(i).getComplemento().equals(values[2]) &&
                        enderecos.getItem(i).getBairro().equals(values[3]) &&
                        enderecos.getItem(i).getCidade().equals(values[4]) &&
                        enderecos.getItem(i).getUf().equals(values[5]) &&
                        String.valueOf(enderecos.getItem(i).getCep()).equals(values[6])) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Endereco enderecoInsert(String[] values) {
        try {
            Endereco endereco = new Endereco();
            endereco.setLogradouro(values[0]);
            endereco.setNumero(Integer.parseInt(values[1]));
            endereco.setComplemento(values[2]);
            endereco.setBairro(values[3]);
            endereco.setCidade(values[4]);
            endereco.setUf(values[5]);
            endereco.setCep(Integer.parseInt(values[6]));
            return endereco;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String enderecoReduce(Endereco endereco) {
        return (endereco.getLogradouro() + ", " + endereco.getNumero() + " - " + endereco.getComplemento());
    }

    public static String enderecoReduce(String[] array) {
        return (array[0] + ", " + array[1] + " - " + array[2]);
    }

    public static void enderecoUpdate(Endereco endereco, String[] values) {
        try {
            endereco.setLogradouro(values[0]);
            endereco.setNumero(Integer.parseInt(values[1]));
            endereco.setComplemento(values[2]);
            endereco.setBairro(values[3]);
            endereco.setCidade(values[4]);
            endereco.setUf(values[5]);
            endereco.setCep(Integer.parseInt(values[6]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean enderecoComparator(Endereco end1, Endereco end2) {
        if (end1.getLogradouro().equals(end2.getLogradouro()) &&
                end1.getNumero() == end2.getNumero() &&
                end1.getComplemento().equals(end2.getComplemento()) &&
                end1.getBairro().equals(end2.getBairro()) &&
                end1.getCidade().equals(end2.getCidade()) &&
                end1.getUf().equals(end2.getUf()) &&
                end1.getCep() == end2.getCep()) {
            return true;
        }
        return false;
    }

    public static boolean enderecoComparator(Endereco endereco, String array[]) {
        if (array[0].equals(endereco.getLogradouro()) &&
                array[1].equals(String.valueOf(endereco.getNumero())) &&
                array[2].equals(endereco.getComplemento()) &&
                array[3].equals(endereco.getBairro()) &&
                array[4].equals(endereco.getCidade()) &&
                array[5].equals(endereco.getUf()) &&
                array[6].equals(String.valueOf(endereco.getCep()))) {
            return true;
        }
        return false;
    }

    public static void enderecoClear(Intent intent) {
        try {
            intent.removeExtra("LOGRADOURO");
            intent.removeExtra("NUMERO");
            intent.removeExtra("COMPLEMENTO");
            intent.removeExtra("BAIRRO");
            intent.removeExtra("CIDADE");
            intent.removeExtra("UF");
            intent.removeExtra("CEP");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void servicoSort(ArrayAdapter <Servico> servicos) {
        servicos.sort(new Comparator<Servico>() {

            @Override
            public int compare(Servico o1, Servico o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
    }

    public static void telefoneSort(ArrayAdapter <Telefone> telefones) {
        telefones.sort(new Comparator<Telefone>() {

            @Override
            public int compare(Telefone o1, Telefone o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
    }

    public static void emailSort(ArrayAdapter <Email> emails) {
        emails.sort(new Comparator<Email>() {

            @Override
            public int compare(Email o1, Email o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
    }

    public static void enderecoSort(ArrayAdapter <Endereco> enderecos) {
        enderecos.sort(new Comparator<Endereco>() {

            @Override
            public int compare(Endereco o1, Endereco o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
    }

    public static void contratoSort(ArrayAdapter <Contrato> contratos) {
        contratos.sort(new Comparator<Contrato>() {

            @Override
            public int compare(Contrato o1, Contrato o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
    }

    public static boolean contratoIsEmpty(String[] values) {
        try {
            if (values[0].trim().isEmpty() ||  // ed_data_ini
                values[1].trim().isEmpty() ||  // ed_hora_ini
                values[2].trim().isEmpty() ||  // ed_min_ini
                values[3].trim().isEmpty() ||  // ed_data_fim
                values[4].trim().isEmpty() ||  // ed_hora_fim
                values[5].trim().isEmpty() ) { // ed_min_fim
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean contratoIsValid(String[] values) {
        try {
           String s_ini = values[0] + " " + values[1] + ":" + values[2] + ":00";
           String s_fim = values[3] + " " + values[4] + ":" + values[5] + ":00";
           Date   d_ini = Dates.getDateTime(s_ini);
           Date   d_fim = Dates.getDateTime(s_fim);
           int res = d_fim.compareTo(d_ini);
           if (res <= 0) {
               return false;
           }
           return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String contratoGetDateTime(String dt, String h, String min) {
        String s1 = dt + " " + h + ":" + min + ":00";
        Date   d  = Dates.getDateTime(s1);
        String s2 = Dates.getDefaultFormatFromDateTime(d);
        return s2;
    }

    public static boolean insertContrato(Activity activity, Intent intent, ArrayAdapter <String> ad_contrato, ArrayAdapter <Contrato> contratos, ImageButton ib_update_contrato, ImageButton ib_delete_contrato, long cliente_id, String[] vec) {
        String   text;
        String[] array = {"", "", ""};
        String msg = activity.getResources().getString(R.string.str_found_time);
        contratoLoad(intent, array, cliente_id);

        System.out.println("*** LOAD ARRAY: " + array + " ***");

        if (!contratoExists(contratos, array, 0)) {  // Não existe;
            Contrato result = contratoInsert(array);

            System.out.println("*** RESULT: " + result + " ***") ;

            text = contratoReduce(result);
            contratos.add(result);
            ad_contrato.add(text);
            resetImages(ad_contrato.getCount(), ib_update_contrato, ib_delete_contrato);
            vec[0] = text;
            return true;
        }
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
        return false;
    }

    public static boolean updateContrato(Activity activity, Intent intent, Spinner sp_contrato, ArrayAdapter<String> ad_contrato, ArrayAdapter <Contrato> contratos, long cliente_id, String[] vec) {
        String   text;
        String[] array = {"", "", ""};
        Contrato contrato;
        contratoLoad(intent, array, cliente_id);
        contrato = contratos.getItem(sp_contrato.getSelectedItemPosition());

        System.out.println ("*** CONTRATO ID: " + contrato.get_id() + " ***");

        if (!contratoExists(contratos, array, contrato.get_id())) {  // Não existe;
            text = contratoReduce(array);
            contratoUpdate(contrato, array);
            ad_contrato.remove(sp_contrato.getSelectedItem().toString());
            ad_contrato.add(text);
            vec[0] = text;
            return true;
        }
        if (!contratoComparator(contratos.getItem(sp_contrato.getSelectedItemPosition()), array)) {
            Toast.makeText(activity, activity.getResources().getString(R.string.str_found_time), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public static void contratoLoad(Intent intent, String[] values, long cliente_id) {
        System.out.println("*** CONTRATO LOAD! ***");

        try {
            values[0] = String.valueOf(cliente_id);
            values[1] = intent.getStringExtra("DATA_INICIAL");
            values[2] = intent.getStringExtra("DATA_FINAL");
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("*** CLIENTE ID: " + values[0]);
        System.out.println("*** DATA INICIAL: " + values[1]);
        System.out.println("*** DATA FINAL: " + values[2]);
    }

    public static boolean contratoExists(ArrayAdapter <Contrato> contratos, String[] values, long id) {
        try {
            for (int i = 0; i < contratos.getCount(); i++) {

                System.out.println ("*** CONTRATO(" + i + ") ID: " + contratos.getItem(i).get_id() + " ***");
                System.out.println ("*** SOURCE ID: " + id + " ***");

                if (id == 0 || contratos.getItem(i).get_id() != id) {
                    java.sql.Date d1_source = contratos.getItem(i).getData_inicial();
                    java.sql.Date d2_source = contratos.getItem(i).getData_final();
                    java.sql.Date d1_target = Dates.getSQLDateTime(values[1]);
                    java.sql.Date d2_target = Dates.getSQLDateTime(values[2]);

                    /*
                     (d1_target >= d1_source && d1_target <= d2_source) ||
                     (d2_target <= d2_source && d2_target >= d1_source) ||
                     (d1_target <= d1_source && d2_target >= d2_source)
                    */

                    int a = d1_target.compareTo(d1_source);
                    int b = d1_target.compareTo(d2_source);
                    int c = d2_target.compareTo(d2_source);
                    int d = d2_target.compareTo(d1_source);
                    int e = d2_target.compareTo(d2_source);

                    if ((a >= 0 && b <= 0) || (c <= 0 && d >= 0) || (a <= 0 && e >= 0)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean contratoFound(ArrayAdapter <Contrato> contratos, String[] values) {
        try {
            for (int i = 0; i < contratos.getCount(); i++) {
                java.sql.Date d1 = Dates.getSQLDateTime(values[1]);
                java.sql.Date d2 = Dates.getSQLDateTime(values[2]);
                if (contratos.getItem(i).getData_inicial().compareTo(d1) == 0 &&
                        contratos.getItem(i).getData_final().compareTo(d2) == 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void arrayOfContrato(String[] array, Contrato contrato) {
        try {
            array[0] = String.valueOf(contrato.get_cliente());
            array[1] = Dates.getDefaultFormatFromSQLDateTime(contrato.getData_inicial());
            array[2] = Dates.getDefaultFormatFromSQLDateTime(contrato.getData_final());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static Contrato contratoInsert(String[] values) {

        System.out.println("*** CONTRATO INSERT! ***");

        try {

            System.out.println("*** DATA INICIAL: " + values[1] + " ***");
            System.out.println("*** DATA FINAL: "   + values[2] + " ***");

            java.sql.Date ini = Dates.getSQLDateTime(values[1]);
            java.sql.Date fim = Dates.getSQLDateTime(values[2]);

            System.out.println("*** " + ini.toString() + " ***");
            System.out.println("*** " + fim.toString() + " ***");

            Contrato contrato = new Contrato();
            contrato.set_cliente(Long.parseLong(values[0]));
            contrato.setData_inicial(ini);
            contrato.setData_final(fim);

            return contrato;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String contratoReduce(Contrato contrato) {
        return contrato.toString();
    }

    public static String contratoReduce(String[] array) {
        Date   d1 = Dates.getDateTime(array[1]);
        Date   d2 = Dates.getDateTime(array[2]);
        String s1 = Dates.getShortFormatFromDateTime(d1);
        String s2 = Dates.getShortFormatFromDateTime(d2);
        return "de: " + s1 + " a: " + s2;
    }

    public static void contratoToArray(String[] array, Contrato contrato) {
        try {
            Calendar calendar = Calendar.getInstance();
            Date dt1 = Dates.convertFromDefaultDate(contrato.getData_inicial());
            Date dt2 = Dates.convertFromDefaultDate(contrato.getData_final());
            calendar.setTime(dt1);
            int h1 = calendar.get(Calendar.HOUR_OF_DAY);
            int m1 = calendar.get(Calendar.MINUTE);
            array[0] = Dates.getDefaultFormatFromDate(dt1);
            array[1] = ZeroFill(String.valueOf(h1));
            array[2] = ZeroFill(String.valueOf(m1));
            calendar.setTime(dt2);
            int h2 = calendar.get(Calendar.HOUR_OF_DAY);
            int m2 = calendar.get(Calendar.MINUTE);
            array[3] = Dates.getDefaultFormatFromDate(dt2);
            array[4] = ZeroFill(String.valueOf(h2));
            array[5] = ZeroFill(String.valueOf(m2));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void contratoUpdate(Contrato contrato, String[] values) {
        try {
            contrato.setData_inicial(Dates.getSQLDateTime(values[1]));
            contrato.setData_final(Dates.getSQLDateTime(values[2]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean contratoComparator(Contrato contrato, String array[]) {
        if (array[1].equals(Dates.getDefaultFormatFromSQLDateTime(contrato.getData_inicial())) &&
            array[2].equals(Dates.getDefaultFormatFromSQLDateTime(contrato.getData_final()))) {
            return true;
        }
        return false;
    }

    public static boolean contratoComparator(Contrato ct1, Contrato ct2) {
        if (ct1.getData_inicial().equals(ct2.getData_inicial()) &&
            ct1.getData_final().equals(ct2.getData_final())) {
            return true;
        }
        return false;
    }

    public static void contratoClear(Intent intent) {
        try {
            intent.removeExtra("DATA_INICIAL");
            intent.removeExtra("DATA_FINAL");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteContrato(Spinner sp_contrato, ArrayAdapter<String> ad_contrato, ArrayAdapter<Contrato> contratos, ImageButton ib_update_contrato, ImageButton ib_delete_contrato) {
        Contrato contrato = contratos.getItem(sp_contrato.getSelectedItemPosition());
        contratos.remove(contrato);
        ad_contrato.remove(sp_contrato.getSelectedItem().toString());
        ad_contrato.notifyDataSetChanged();
        resetImages(ad_contrato.getCount(), ib_update_contrato, ib_delete_contrato);
    }

    public static String ZeroFill(String s) {
        if (s.length() == 1)
            return "0" + s;
        return s;
    }

    public static void clearAllRadioButtons(RadioGroup radio_group) {
        RadioButton rb1, rb2;
        for (int i = 0 ; i < radio_group.getChildCount(); i++) {
            rb1 = (RadioButton) radio_group.getChildAt(i);
            rb1.setChecked(false);
            rb2 = (RadioButton) rb1.getTag();
            rb2.setChecked(false);
        }
    }

    public static RadioButton getRadioButton(RadioGroup radio_group, RadioButton rb) {
        RadioButton result;
        for (int i = 0 ; i < radio_group.getChildCount(); i++) {
            result = (RadioButton) radio_group.getChildAt(i);
            if (result.getTag().equals(rb)) {
                return result;
            }
        }
        return null;
    }

    public static void setRadioButton(String text, RadioGroup radio_group) {
        RadioButton rb1, rb2;
        for (int i = 0 ; i < radio_group.getChildCount(); i++) {
            rb1 = (RadioButton) radio_group.getChildAt(i);
            rb2 = (RadioButton) rb1.getTag();
            TextView tv = (TextView) rb2.getTag();
            String str = tv.getText().toString();
            if (str.equals(text)) {
                rb1.setChecked(true);
                rb2.setChecked(true);
            }
        }
    }

    public static ProgressDialog createProgressDialog(Context context, String message, int max) {
        ProgressDialog status = new ProgressDialog(context);
        status.setMessage(message);
        status.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        status.setMax(max);
        status.setProgress(0);
        status.setCancelable(true);
        return status;
    }
    public static String exportContacts(final Activity activity, String string, String nome, String[] tels, String[] mails, ArrayAdapter<Endereco> enderecos) {
        Contact contact = new Contact(activity, "Clientes");
        if (contact.mayRequestContacts(activity)) {
            return contact.createContact(nome, tels, mails, enderecos);
        }
        return null;
    }
}
