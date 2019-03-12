package br.com.alexandrebarboza.casadeeventos.Utility;

import br.com.alexandrebarboza.casadeeventos.Database.Database;
import br.com.alexandrebarboza.casadeeventos.Domain.Domain;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Cliente;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Contrato;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Email;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Endereco;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Evento;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Pagamento;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Servico;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Telefone;
import br.com.alexandrebarboza.casadeeventos.Domain.Relation.ClienteXEmail;
import br.com.alexandrebarboza.casadeeventos.Domain.Relation.ClienteXEndereco;
import br.com.alexandrebarboza.casadeeventos.Domain.Relation.ClienteXTelefone;
import br.com.alexandrebarboza.casadeeventos.Domain.Relation.EventoXServico;
import br.com.alexandrebarboza.casadeeventos.R;
import br.com.alexandrebarboza.casadeeventos.Utility.Adapters.Adapters;
import br.com.alexandrebarboza.casadeeventos.Utility.Messages.Output;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by Alexandre on 11/04/2017.
 */

public class Connector {

    private static boolean TelefoneIsChanged(ArrayAdapter<Telefone> telefones, Spinner sp_telefone, int position) {
        String str = sp_telefone.getItemAtPosition(position).toString();
        for (int i = 0; i < telefones.getCount(); i++) {
            if (telefones.getItem(i).getNumero().equals(str)) {
                return false;
            }
        }
        return true;
    }

    private static boolean EmailIsChanged(ArrayAdapter<Email> emails, Spinner sp_email, int position) {
        String str = sp_email.getItemAtPosition(position).toString();
        for (int i = 0; i < emails.getCount(); i++) {
            if (emails.getItem(i).getEndereco().equals(str)) {
                return false;
            }
        }
        return true;
    }

    private static int checkTelefone(Domain domain, Spinner sp_telefone, ArrayAdapter<Telefone> telefones, int[] vec, boolean changed) {
        int x, flag = 1;
        boolean control;
        for (int i = 0; i < sp_telefone.getCount(); i++) {
            if (changed == true) {
                if (TelefoneIsChanged(telefones, sp_telefone, i) == true) {
                    control = true;
                } else {
                    control = false;
                }
            } else {
                control = true;
            }
            if (control == true) {

                // Verificar se o numero do telefone ja existe!
                x = domain.checkUniqueKeyForTelefone(sp_telefone.getItemAtPosition(i).toString());
                if (x == -1) {
                    flag = -1;
                    break;
                } else if (x == 0) {
                    flag = 1;
                } else if (x == 1) {
                    flag = 4;
                    vec[0] = i;
                    break;
                }
            } else {
                flag = 1;
            }
        }
        return flag;
    }

    private static int checkEmail(Domain domain, Spinner sp_email, ArrayAdapter<Email> emails, int[] vec, boolean changed) {
        int x, flag = 1;
        boolean control;
        for (int i = 0; i < sp_email.getCount(); i++) {
            if (changed == true) {
                if (EmailIsChanged(emails, sp_email, i) == true) {
                    control = true;
                } else {
                    control = false;
                }
            } else {
                control = true;
            }
            if (control == true) {

                // Verificar se o endereço de email ja existe!
                x = domain.checkUniqueKeyForEmail(sp_email.getItemAtPosition(i).toString());

                if (x == -1) {
                    flag = -1;
                    break;
                } else if (x == 0) {
                    flag = 1;
                } else if (x == 1) {
                    flag = 5;
                    vec[0] = i;
                    break;
                }
            } else {
                flag = 1;
            }
        }
        return flag;
    }

    private static int checkEndereco(Domain domain, ArrayAdapter<Endereco> enderecos, int[] vec) {
        int x, flag = 1;
        for (int i = 0; i < enderecos.getCount(); i++) {

            // Verificar se o endereço já existe!
            x = domain.checkAllFieldsForEndereco(enderecos.getItem(i));

            if (x == -1) {
                flag = -1;
                break;
            } else if (x == 0) {
                flag = 1;
            } else {
                flag = 6;
                vec[0] = i;
                break;
            }
        }
        return flag;
    }

    private static int checkContrato(Domain domain, ArrayAdapter<Contrato> contratos, int[] vec) {
        int x, flag = 1;
        for (int i = 0; i < contratos.getCount(); i++) {

            // Verificar se existe contrato nesta data/hora!
            x = domain.checkIfContratoExists(contratos.getItem(i));

            if (x == -1) {
                flag = -1;
                break;
            } else if (x == 0) {
                flag = 1;
            } else {
                flag = 7;
                vec[0] = i;
                break;
            }
        }
        return flag;
    }

    private static boolean ClienteIsChanged(Cliente cliente, EditText ed_razao_social, EditText ed_cnpj) {
        return (!cliente.getRazao_social().equals(ed_razao_social.getText().toString()) || !cliente.getCnpj().equals(ed_cnpj.getText().toString()));
    }

    private static boolean ServicoIsChanged(Servico servico, long i_tipo, String s_descricao) {
        return (servico.get_tipo() != i_tipo) || !servico.getDescricao().equals(s_descricao);
    }

    private static boolean ServicoFound(ArrayAdapter<Servico> servicos, Spinner sp_servicos, int position) {
        String str = sp_servicos.getItemAtPosition(position).toString();
        for (int i = 0; i < servicos.getCount(); i++) {
            if (servicos.getItem(i).getDescricao().equals(str)) {
                return true;
            }
        }
        return false;
    }

    private static Endereco getEndereco(Endereco src) {
        Endereco endereco = new Endereco();
        endereco.setLogradouro(src.getLogradouro());
        endereco.setNumero(src.getNumero());
        endereco.setComplemento(src.getComplemento());
        endereco.setBairro(src.getBairro());
        endereco.setCidade(src.getCidade());
        endereco.setUf(src.getUf());
        endereco.setCep(src.getCep());
        return endereco;
    }

    private static boolean updateEndereco(Activity activity, Domain domain, Endereco endereco) {
        if (domain.updateEndereco(endereco) > -1) { // Operação efetuada com êxito!
            return true;
        } // Erro alterando Endereço!
        errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_endereco), activity.getResources().getString(R.string.str_err_update));
        return false;
    }

    private static boolean updateContrato(Activity activity, Domain domain, Contrato contrato) {
        if (domain.updateContrato(contrato) > -1) { // Operação efetuada com êxito!
            return true;
        } // Erro alterando Contrato!
        errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_contrato), activity.getResources().getString(R.string.str_err_update));
        return false;
    }

    private static boolean updatePagamento(Activity activity, Domain domain, Pagamento pagamento, double total, long id_evento, int[] v) {
        Intent it = activity.getIntent();
        double desconto = pagamento.getDesconto();;
        boolean update = false;
        if (it.hasExtra("PAGAMENTO_TOTAL")) {
            pagamento.setTotal(it.getDoubleExtra("PAGAMENTO_TOTAL", 0.00));
            it.removeExtra("PAGAMENTO_TOTAL");
            update = true;
        }
        if (it.hasExtra("PAGAMENTO_DESCONTO")) {
            pagamento.setDesconto(it.getDoubleExtra("PAGAMENTO_DESCONTO", 0.00));
            it.removeExtra("PAGAMENTO_DESCONTO");
        }
        if ((total == pagamento.getTotal() && desconto == pagamento.getDesconto())) {
            if (total != 0 && update == false) {
                return true;
            }
        }
        boolean flag;
        if (pagamento.get_id() == 0) {
            pagamento.set_evento(id_evento);
            if (total > 0) {
                if (domain.addPagamento(pagamento) > -1) {
                    flag = true;
                    v[0] = 1;
                } else {
                    flag = false;
                }
            } else {
                flag = true;
            }
        } else {
            if (total > 0) {
                if (domain.updatePagamento(pagamento) > -1) {
                    flag = true;
                    v[0] = 1;
                } else {
                    flag = false;
                }
            } else {
                if (domain.deletePagamento(pagamento.get_id()) > -1) {
                    flag = true;
                    v[0] = 1;
                } else {
                    flag = false;
                }
            }

        }
        return flag;
    }

    private static boolean addAllServicos(Activity activity, Domain domain, Spinner sp_servicos, ArrayAdapter<String> ad_servicos, ArrayAdapter<Servico> servicos, boolean update, long id, int[] vec) {
        boolean flag = true, result;
        for (int i = 0; i < ad_servicos.getCount(); i++) {
            if (update == true) {
                result = !ServicoFound(servicos, sp_servicos, i);   // Serviço not exist?
            } else {
                result = true;
            }
            if (result == true) {
                flag = addEventoServico(activity, domain, ad_servicos.getItem(i), id);  // Incluir serviço do evento!

                /*
                String txt = ad_servicos.getItem(i);
                if (update == false) {
                    flag = addEventoServico(activity, domain, txt, id);  // Incluir serviço do evento!
                } else {

                    long id_servico = domain.getIdServico(activity, txt);

                    System.out.println("*** ID SERVICO: " + id_servico + " ***");

                }
                */
                vec[0] = 1;
            }
            if (!flag) {
                vec[0] = 0;
                break;
            }
        }
        return flag;
    }

    private static boolean addAllTelefones(Activity activity, Domain domain, Spinner sp_telefone, ArrayAdapter<String> ad_telefone, ArrayAdapter<Telefone> telefones, boolean update, long id, int[] vec) {
        boolean flag = true, result;
        for (int i = 0; i < ad_telefone.getCount(); i++) {
            if (update == true) {
                result = TelefoneIsChanged(telefones, sp_telefone, i);   // Telefone mudou?
            } else {
                result = true;
            }
            if (result == true) {
                flag = addClienteTelefone(activity, domain, ad_telefone, i, id);  // Incluir telefone de cliente!
                vec[0] = 1;
            }
            if (!flag) {
                vec[0] = 0;
                break;
            }
        }
        return flag;
    }

    private static boolean addAllEmails(Activity activity, Domain domain, Spinner sp_email, ArrayAdapter<String> ad_email, ArrayAdapter<Email> emails, boolean update, long id, int[] vec) {
        boolean flag = true, result;
        for (int i = 0; i < ad_email.getCount(); i++) { // Incluir email!
            if (update == true) {
                result = EmailIsChanged(emails, sp_email, i);
            } else {
                result = true;
            }
            if (result == true) {
                flag = addClienteEmail(activity, domain, ad_email, i, id);
                vec[0] = 1;
            }
            if (!flag) {
                vec[0] = 0;
                break;
            }
        }
        return flag;
    }

    private static boolean addAllEnderecos(Activity activity, Domain domain, ArrayAdapter<Endereco> enderecos, boolean update, long id, int[] vec) {
        boolean add, flag = true;
        Endereco end1, end2;
        for (int i = 0; i < enderecos.getCount(); i++) {
            if (update == true) {
                end1 = enderecos.getItem(i);
                long endereco_id = end1.get_id();
                if (endereco_id > 0) {
                    end2 = domain.selectEndereco(endereco_id);
                    if (!Utility.enderecoComparator(end1, end2)) {
                        flag = updateEndereco(activity, domain, end1); // Alterar endereço!
                        vec[0] = 1;
                    }
                    add = false; // Não incluir endereço!
                } else {
                    add = true;
                }
            } else {
                add = true;
            }
            if (add == true) {
                flag = addClienteEndereco(activity, domain, enderecos, i, id);
                vec[0] = 1;
            }
            if (!flag) {
                vec[0] = 0;
                break;
            }
        }
        return flag;
    }

    private static boolean addAllContratos(Activity activity, Domain domain, ArrayAdapter<Contrato> contratos, boolean update, long id, int[] vec) {
        boolean add, flag = true;
        Contrato ct1, ct2;
        for (int i = 0; i < contratos.getCount(); i++) {
            if (update == true) {
                ct1 = contratos.getItem(i);
                long contrato_id = ct1.get_id();
                if (contrato_id > 0) {
                    ct2 = domain.selectContrato(contrato_id);
                    if (!Utility.contratoComparator(ct1, ct2)) {
                        flag = updateContrato(activity, domain, ct1); // Alterar contrato!
                        vec[0] = 1;
                    }
                    add = false; // Não incluir contrato!
                } else {
                    add = true;
                }
            } else {
                add = true;
            }
            if (add == true) {
                flag = addClienteContrato(activity, domain, contratos, i);
                vec[0] = 1;
            }
            if (!flag) {
                vec[0] = 0;
                break;
            }
        }
        return flag;
    }

    private static boolean removeAllTelefones(Activity activity, Domain domain, ArrayAdapter <String> ad_telefone, ArrayAdapter <Telefone> telefones, int[] vec) {
        boolean flag = true;
        for (int i = 0; i < telefones.getCount(); i++) {
            if (!Adapters.DataFound(telefones.getItem(i).getNumero(), ad_telefone)) { // Excluir telefone!
                flag = deleteClienteTelefone(activity, domain, telefones.getItem(i).get_id());
                if (!flag) {
                    vec[0] = 0;
                    break;
                } else {
                    vec[0] = 1;
                }
            }
        }
        return flag;
    }

    private static boolean removeAllEmails(Activity activity, Domain domain, ArrayAdapter<String> ad_email, ArrayAdapter<Email> emails, int[] vec) {
        boolean flag = true;
        for (int i = 0; i < emails.getCount(); i++) {
            if (!Adapters.DataFound(emails.getItem(i).getEndereco(), ad_email)) { // Excluir email!
                flag = deleteClienteEmail(activity, domain, emails.getItem(i).get_id());
                if (!flag) {
                    vec[0] = 0;
                    break;
                } else {
                    vec[0] = 1;
                }
            }
        }
        return flag;
    }

    private static boolean removeAllEnderecos(Activity activity, Domain domain, ArrayAdapter<Endereco> enderecos, long id, int[] vec) {
        boolean flag = true;
        String[] array = {"", "", "", "", "", "", ""};
        ArrayAdapter<Endereco> enderecos2 = domain.selectEnderecos(activity, id);;
        if (enderecos2 != null) {
            for (int i = 0; i < enderecos2.getCount(); i++) {
                Utility.enderecoToArray(array, enderecos2.getItem(i));
                if (!Utility.enderecoExists(enderecos, array)) {     // Excluir endereço!
                    flag = deleteClienteEndereco(activity, domain, enderecos2.getItem(i).get_id());
                    if (!flag) {
                        vec[0] = 0;
                        break;
                    } else {
                        vec[0] = 1;
                    }
                }
            }
        }
        return flag;
    }

    private static boolean removeAllContratos(Activity activity, Domain domain, ArrayAdapter<Contrato> contratos, long id, int[] vec) {
        boolean flag = true;
        String[] array = {"", "", ""};
        ArrayAdapter<Contrato> contratos2 = domain.selectContratos(activity, id);;
        if (contratos2 != null) {
            for (int i = 0; i < contratos2.getCount(); i++) {
                 Utility.arrayOfContrato(array, contratos2.getItem(i));
                 if (!Utility.contratoFound(contratos, array)) {     // Excluir contrato!
                     flag = deleteClienteContrato(activity, domain, contratos2.getItem(i).get_id());
                     if (!flag) {
                         vec[0] = 0;
                         break;
                     } else {
                         vec[0] = 1;
                     }
                }
            }
        }
        return flag;
    }

    private static boolean removeAllServicos(Activity activity, Domain domain, ArrayAdapter <String> ad_servico, ArrayAdapter <Servico> servicos, long evento_id, int[] vec) {
        boolean flag = true;
        for (int i = 0; i < servicos.getCount(); i++) {
            if (!Adapters.DataFound(servicos.getItem(i).getDescricao(), ad_servico)) { // Excluir serviço!
                flag = deleteEventoServico(activity, domain, evento_id, servicos.getItem(i).get_id());
                if (!flag) {
                    vec[0] = 0;
                    break;
                } else {
                    vec[0] = 1;
                }
            }
        }
        return flag;
    }

    private static boolean deleteClienteTelefone(Activity activity, Domain domain, long id) {
        if (domain.deleteTelefone(id) > -1) {
            if (domain.deleteClienteXTelefone(id, true) > -1) { // Operação efetuada com êxito!
                return true;
            } else { // Erro excluindo a relação Cliente X Telefone!
                errorMessageRelation(activity, domain, activity.getResources().getString(R.string.str_cli_x_tel), activity.getResources().getString(R.string.str_err_delete));
            }
        } else { // Erro excluindo Telefone!
            errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_telefone), activity.getResources().getString(R.string.str_err_delete));
        }
        return false;
    }

    private static boolean deleteClienteEmail(Activity activity, Domain domain, long id) {
        if (domain.deleteEmail(id) > -1) {
            if (domain.deleteClienteXEmail(id, true) > -1) { // Operação efetuada com êxito!
                return true;
            } else { // Erro excluindo a relação Cliente X Email!
                errorMessageRelation(activity, domain, activity.getResources().getString(R.string.str_cli_x_mai), activity.getResources().getString(R.string.str_err_delete));
            }
        } else { // Erro excluindo Email!
            errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_email), activity.getResources().getString(R.string.str_err_delete));
        }
        return false;
    }

    private static boolean deleteClienteEndereco(Activity activity, Domain domain, long id) {
        if (domain.deleteEndereco(id) > -1) {
            if (domain.deleteClienteXEndereco(id, true) > -1) { // Operação efetuada com êxito!
                return true;
            } else { // Erro excluindo a relação Cliente X Endereço!
                errorMessageRelation(activity, domain, activity.getResources().getString(R.string.str_cli_x_end), activity.getResources().getString(R.string.str_err_delete));
            }
        } else { // Erro excluindo Endereço!
            errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_endereco), activity.getResources().getString(R.string.str_err_delete));
        }
        return false;
    }

    private static boolean deleteClienteContrato(Activity activity, Domain domain, long id) {
        if (domain.deleteContrato(id, false) > -1) {
            return true;
        }
        // Erro excluindo Contrato!
        errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_contrato), activity.getResources().getString(R.string.str_err_delete));
        return false;
    }

    private static boolean deleteEventoServico(Activity activity, Domain domain, long evento_id, long servico_id) {
        if (domain.deleteEventoXServico(evento_id, servico_id) > -1) { // Operação efetuada com êxito!
             return true;
        } else { // Erro excluindo a relação Evento X Servico!
            errorMessageRelation(activity, domain, activity.getResources().getString(R.string.str_eve_x_ser), activity.getResources().getString(R.string.str_err_delete));
        }
        return false;
    }

    private static boolean addEventoServico(Activity activity, Domain domain, String servico, long id_evento) {
        long id_servico = domain.getIdServico(activity, servico);
        boolean result;
        if (id_servico > 0) {
            EventoXServico evento_x_servico = new EventoXServico();
            evento_x_servico.set_evento(id_evento);
            evento_x_servico.set_servico(id_servico);
            if (domain.addEventoXServico(evento_x_servico) > -1) { // Operação efetuada com êxito!
                result = true;
            } else { // Erro inserindo na relação Evento X Serviço!
                errorMessageRelation(activity, domain, activity.getResources().getString(R.string.str_eve_x_ser), activity.getResources().getString(R.string.str_err_insert));
                result = false;
            }
        } else {
            result = false;
        }
        return result;
    }

    private static boolean addClienteTelefone(Activity activity, Domain domain, ArrayAdapter<String> ad_telefone, int i, long id_cliente) {
        Telefone telefone = new Telefone();
        telefone.setNumero(ad_telefone.getItem(i).toString());
        long id_telefone = domain.addTelefone(telefone);
        boolean result;
        if (id_telefone > 0) {
            ClienteXTelefone cliente_x_telefone = new ClienteXTelefone();
            cliente_x_telefone.set_cliente(id_cliente);
            cliente_x_telefone.set_telefone(id_telefone);
            if (domain.addClienteXTelefone(cliente_x_telefone) > -1) { // Operação efetuada com êxito!
                result = true;
            } else { // Erro inserindo na relação Cliente X Telefone!
                errorMessageRelation(activity, domain, activity.getResources().getString(R.string.str_cli_x_tel), activity.getResources().getString(R.string.str_err_insert));
                result = false;
            }
        } else { // Erro inserindo Telefone!
            errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_telefone), activity.getResources().getString(R.string.str_err_insert));
            result = false;
        }
        return result;
    }

    private static boolean addClienteEmail(Activity activity, Domain domain, ArrayAdapter<String> ad_email, int i, long id_cliente) {
        Email email = new Email();
        email.setEndereco(ad_email.getItem(i).toString());
        long id_email = domain.addEmail(email);
        boolean result;
        if (id_email > 0) {
            ClienteXEmail cliente_x_email = new ClienteXEmail();
            cliente_x_email.set_cliente(id_cliente);
            cliente_x_email.set_email(id_email);
            if (domain.addClienteXEmail(cliente_x_email) > -1) { // Operação efetuada com êxito!
                result = true;
            } else { // Erro inserindo na relação Medico X Email!
                errorMessageRelation(activity, domain, activity.getResources().getString(R.string.str_cli_x_mai), activity.getResources().getString(R.string.str_err_insert));
                result = false;
            }
        } else { // Erro inserindo Email!
            errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_email), activity.getResources().getString(R.string.str_err_insert));
            result = false;
        }
        return result;
    }

    private static boolean addClienteEndereco(Activity activity, Domain domain, ArrayAdapter<Endereco> enderecos, int i, long id_cliente) {
        Endereco endereco = getEndereco(enderecos.getItem(i));
        long id_endereco = domain.addEndereco(endereco);
        boolean result;
        if (id_endereco > 0) {
            ClienteXEndereco cliente_x_end = new ClienteXEndereco();
            cliente_x_end.set_cliente(id_cliente);
            cliente_x_end.set_endereco(id_endereco);
            if (domain.addClienteXEndereco(cliente_x_end) > -1) { // Operação efetuada com êxito!
                result = true;
            } else { // Erro inserindo na relação Medico X Endereço!
                errorMessageRelation(activity, domain, activity.getResources().getString(R.string.str_cli_x_end), activity.getResources().getString(R.string.str_err_insert));
                result = false;
            }
        } else { // Erro inserindo Endereço!
            errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_endereco), activity.getResources().getString(R.string.str_err_insert));
            result = false;
        }
        return result;
    }

    private static boolean addClienteContrato(Activity activity, Domain domain, ArrayAdapter<Contrato> contratos, int i) {
        Contrato contrato = contratos.getItem(i); //getContrato(contratos.getItem(i));
        long id_contrato = domain.addContrato(contrato);
        boolean result;
        if (id_contrato > 0) {
            result = true;
        } else { // Erro inserindo Contrato!
            errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_contrato), activity.getResources().getString(R.string.str_err_insert));
            result = false;
        }
        return result;
    }

    private static boolean addServico(Activity activity, Domain domain, ArrayAdapter<Servico> servicos, int i) {
        Servico servico = servicos.getItem(i);
        long id_contrato = domain.addServico(servico);
        boolean result;
        if (id_contrato > 0) {
            result = true;
        } else { // Erro inserindo Serviço!
            errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_servicos), activity.getResources().getString(R.string.str_err_insert));
            result = false;
        }
        return result;
    }

    private static void errorMessageEntity(Activity activity, Domain domain, String data, String operation) {
        String msg, s = data.substring(0, data.length() - 1);
        msg = operation + " " + s + " " + activity.getResources().getString(R.string.str_msg_plus) + "\n" + domain.getError();
        Output.Alert(activity, activity.getResources().getString(R.string.str_fail), msg);
    }

    private static void errorMessageRelation(Activity activity, Domain domain, String data, String operation) {
        String msg = operation + " na " + data + " " + activity.getResources().getString(R.string.str_msg_plus) + "\n" + domain.getError();
        Output.Alert(activity, activity.getResources().getString(R.string.str_fail), msg);
    }

    public static boolean OpenDatabase(Resources resources, Context context, Database database, Domain domain, boolean flag) {
        String error = "";
        if (flag) {
            if (database.setWritable()) {
                // Banco de Dados aberto para escrita!
            } else {
                error = resources.getString(R.string.str_write);
            }
        } else {
            if (database.setReadable()) {
                // Banco de Dados aberto para leitura!
            } else {
                error = resources.getString(R.string.str_read);
            }
        }
        if (!error.isEmpty()) {
            Output.Alert(context, resources.getString(R.string.str_fail), resources.getString(R.string.str_err_sql_open) + " " + error + "!" + resources.getString(R.string.str_msg_plus) + "\n" + database.getError());
            return false;
        }
        domain.setConnection(database.getConnection());
        return true;
    }

    public static long getIdTipo(Activity activity, Domain domain, String text) {
        return domain.getIdTipo(activity, text);
    }

    public static int LoadAddCliente(Domain domain, Spinner sp_telefone, Spinner sp_email, ArrayAdapter<Endereco> enderecos, EditText ed_razao_social, EditText ed_cnpj, int[] vec) {
        int x, flag = 0;

        // Verificar se a Razão Social já existe!
        x = domain.checkRazaoSocialForCliente(ed_razao_social.getText().toString());

        if (x == -1) {
            flag = -1;
        } else if (x == 0) {

            // Verificar se o CNPJ já existe!
            x = domain.checkCnpjForCliente(ed_cnpj.getText().toString());

            if (x == -1) {
                flag = -1;
            } else if (x == 0) {
                flag = 1;
            } else if (x == 1) {
                flag = 3;
            }
        } else if (x == 1) {
            flag = 2;
        }
        if (flag == 1) {
            flag = checkTelefone(domain, sp_telefone, null, vec, false);
        }
        if (flag == 1) {
            flag = checkEmail(domain, sp_email, null, vec, false);
        }
        if (flag == 1) {
            flag = checkEndereco(domain, enderecos, vec);
        }
        return flag;

    }

    public static int LoadUpddateCliente(Domain domain, Cliente cliente, ArrayAdapter<Contrato> contratos, ArrayAdapter<Telefone> telefones, ArrayAdapter<Email> emails, ArrayAdapter<Endereco> enderecos, Spinner sp_telefone, Spinner sp_email, EditText ed_razao_social, EditText ed_cnpj, int[] vec) {
        int x = 0, flag = 0;
        if (!cliente.getRazao_social().equals(ed_razao_social.getText().toString())) {

            // Verificar se a Razão Social já existe!
            x = domain.checkRazaoSocialForCliente(ed_razao_social.getText().toString());

            if (x == 0) {
                flag = 1;
            } else if (x == 1) {
                flag = 2;
            }
        } else {
            flag = 1;
        }
        if (flag == 1) {
            if (!cliente.getCnpj().equals(ed_cnpj.getText().toString())) {

                // Verificar se o CNPJ já existe!
                x = domain.checkCnpjForCliente(ed_cnpj.getText().toString());

                if (x == 0) {
                    flag = 1;
                } else if (x == 1) {
                    flag = 3;
                }
            }
        }
        if (flag == 1) {
            flag = checkTelefone(domain, sp_telefone, telefones, vec, true);
        }
        if (flag == 1) {
            flag = checkEmail(domain, sp_email, emails, vec, true);
        }
        if (flag == 1) {
            flag = checkEndereco(domain, enderecos, vec);
        }
        if (flag == 1) {
            flag = checkContrato(domain, contratos, vec);
        }
        if (x == -1) {
            flag = -1;
        }
        return flag;
    }

    public static int LoadAddServico(Domain domain, long i_tipo, String s_descricao, int[] vec) {
        int x, flag = 0;

        // Verificar se a descricao já existe!
        x = domain.checkUniqueKeyForServico(s_descricao);

        if (x == -1) {
            flag = -1;
        } else if (x == 0) {
            flag = 1;
        } else if (x == 1) {
            flag = 2;
        }
        return flag;
    }

    public static int LoadUpdateServico(Domain domain, Servico servico, long i_tipo, String s_descricao, int[] vet) {
        int x, flag = 0;
        if (!servico.getDescricao().equals(s_descricao)) {

            // Verificar se a descricao já existe!
            x = domain.checkUniqueKeyForServico(s_descricao);

        } else {
            x = 0;
        }
        if (x == -1) {
            flag = -1;
        } else if (x == 0) {
            flag = 1;
        } else if (x == 1) {
            flag = 2;
        }
        return flag;
    }

    public static int LoadAddEvento(Domain domain, String descricao, long id_contrato) {
        int x, flag = 0;

        // Verificar se a descrição já existe!
        x = domain.checkUniqueKeyForDescricaoEvento(descricao);

        if (x == 0) {

            // Verificar se o id do contrato já existe!
            x = domain.checkUniqueKeyForContratoEvento(id_contrato);

            if (x == 0) {
                flag = 1;
            } else if (x == 1) {
                flag = 3;
            }
        } else if (x == 1) {
            flag = 2;
        }
        if (x == -1) {
            flag = -1;
        }
        return flag;
    }

    public static int LoadUpdateEvento(Domain domain, Evento evento, String descricao, long id_contrato) {
        int x, flag = 0;
        if (!evento.getDescricao().equals(descricao)) {

            // Verificar se a descrição já existe!
            x = domain.checkUniqueKeyForDescricaoEvento(descricao);
            if (x == 1) {
                flag = 2;
            }
        } else {
            x = 0;
        }
        if (x == 0) {
            if (evento.get_contrato() != id_contrato) {

                // Verificar se o id do contrato já existe!
                x = domain.checkUniqueKeyForContratoEvento(id_contrato);

                if (x == 1) {
                    flag = 3;
                }
            } else {
                x = 0;
            }
        }
        if (x == -1) {
            flag = -1;
        } else if (x == 0) {
            flag = 1;
        }
        return flag;
    }
    public static boolean SaveAddCliente(Activity activity, Domain domain, Cliente cliente, ArrayAdapter<String> ad_telefone, ArrayAdapter<String> ad_email, ArrayAdapter<Endereco> enderecos, EditText ed_razao_social, EditText ed_cnpj) {
        boolean flag = false;
        cliente.setRazao_social(ed_razao_social.getText().toString());
        cliente.setCnpj(ed_cnpj.getText().toString());
        long id_cliente = domain.addCliente(cliente);
        int v[] = {0};
        if (id_cliente > 0) {
            flag = addAllTelefones(activity, domain, null, ad_telefone, null, false, id_cliente, v);
        } else { // Erro incluindo Cliente!
            errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_clientes), activity.getResources().getString(R.string.str_err_insert));
        }
        if (flag) {
            flag = addAllEmails(activity, domain, null, ad_email, null, false, id_cliente, v);
        }
        if (flag) {
            flag = addAllEnderecos(activity, domain, enderecos, false, id_cliente, v);
        }
        return flag;
    }

    public static boolean SaveUpdateCliente(Activity activity, Domain domain, Cliente cliente, ArrayAdapter<Contrato> contratos, ArrayAdapter<Telefone> telefones, ArrayAdapter<Email> emails, ArrayAdapter<Endereco> enderecos, ArrayAdapter<String> ad_telefone, ArrayAdapter<String> ad_email, Spinner sp_telefone, Spinner sp_email, EditText ed_razao_social, EditText ed_cnpj, int[] v) {
        boolean flag;
        if (ClienteIsChanged(cliente, ed_razao_social, ed_cnpj)) { // Razão social ou CNPJ mudaram
            cliente.setRazao_social(ed_razao_social.getText().toString());
            cliente.setCnpj(ed_cnpj.getText().toString());
            if (domain.updateCliente(cliente) > -1) {
                v[0] = 1;
                flag = true;
            } else {         // Erro Alterando Cliente!
                errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_clientes), activity.getResources().getString(R.string.str_err_update));
                v[0] = 0;
                flag = false;
            }
        } else {
            flag = true;
        }
        if (flag) {
            flag = addAllTelefones(activity, domain, sp_telefone, ad_telefone, telefones, true, cliente.get_id(), v);
        }
        if (flag) {
            flag = removeAllTelefones(activity, domain, ad_telefone, telefones, v);
        }
        if (flag) {
            flag = addAllEmails(activity, domain, sp_email, ad_email, emails, true, cliente.get_id(), v);
        }
        if (flag) {
            flag = removeAllEmails(activity, domain, ad_email, emails, v);
        }
        if (flag) {
            flag = addAllEnderecos(activity, domain, enderecos, true, cliente.get_id(), v);
        }
        if (flag) {
            flag = removeAllEnderecos(activity, domain, enderecos, cliente.get_id(), v);
        }
        if (flag) {
            flag = addAllContratos(activity, domain, contratos, true, cliente.get_id(), v);
        }
        if (flag) {
            flag = removeAllContratos(activity, domain, contratos, cliente.get_id(), v);
        }
        return flag;
    }

    public static boolean DeleteCliente(Activity activity, Domain domain, Cliente cliente, ArrayAdapter<Contrato> contratos, ArrayAdapter <Telefone> telefones, ArrayAdapter <Email> emails, ArrayAdapter <Endereco> enderecos, String[] v) {
        boolean flag = false;
        String field, str = "";
        if (domain.deleteCliente(cliente.get_id()) > -1) {
            flag = true;
        } else {
            str = activity.getResources().getString(R.string.str_clientes);
            str = str.substring(0, str.length() -1)  + " ";
        }
        if (str.isEmpty()) {
            if (domain.deleteContrato(cliente.get_id(), true) > -1) {
                flag = true;
            } else {
                field = activity.getResources().getString(R.string.str_contrato);
                field = field.substring(0, field.length() - 1);
                str = field;
            }
        }
        if (str.isEmpty()) {
            for (int i = 0; i <  telefones.getCount(); i++) {
                if (domain.deleteTelefone(telefones.getItem(i).get_id()) > -1) {
                    flag = true;
                } else {
                    field = activity.getResources().getString(R.string.str_telefone);
                    field = field.substring(0, field.length() -1) + " ";
                    str = field;
                    break;
                }
            }
        }
        if (str.isEmpty()) {
            if (domain.deleteClienteXTelefone(cliente.get_id(), false) > -1) {
                flag = true;
            } else {
                str = activity.getResources().getString(R.string.str_cli_x_tel) + " ";
            }
        }
        if (str.isEmpty()) {
            for (int i  = 0; i < emails.getCount(); i++) {
                if (domain.deleteEmail(emails.getItem(i).get_id()) > -1) {
                    flag = true;
                } else {
                    field = activity.getResources().getString(R.string.str_email);
                    field = field.substring(0, field.length() -1) + " ";
                    str = field;
                    break;
                }
            }
        }
        if (str.isEmpty()) {
            if (domain.deleteClienteXEmail(cliente.get_id(), false) > -1) {
                flag = true;
            } else {
                str = activity.getResources().getString(R.string.str_cli_x_mai) + " ";
            }
        }
        if (str.isEmpty()) {
            for (int i  = 0; i < enderecos.getCount(); i++) {
                if (domain.deleteEndereco(enderecos.getItem(i).get_id()) > -1) {
                    flag = true;
                } else {
                    field = activity.getResources().getString(R.string.str_endereco);
                    field = field.substring(0, field.length() -1) + " ";
                    str = field;
                    break;
                }
            }
        }
        if (str.isEmpty()) {
            if (domain.deleteClienteXEndereco(cliente.get_id(), false) > -1) {
                flag = true;
            } else {
                str = activity.getResources().getString(R.string.str_cli_x_end) + " ";
            }
        }
        v[0] = str;
        return flag;
    }

    public static boolean SaveAddServico(Activity activity, Domain domain, Servico servico, long i_tipo, String s_descricao, String s_valor) {
        boolean flag = false;
        double d_valor = Utility.getDoubleOfCurrencyForBR(s_valor);
        servico.set_tipo(i_tipo);
        servico.setDescricao(s_descricao);
        servico.setValor(d_valor);
        long id_servico = domain.addServico(servico);
        int v[] = {0};
        if (id_servico > 0) {
            flag = true;
        } else { // Erro incluindo Serviço!
            errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_servicos), activity.getResources().getString(R.string.str_err_insert));
        }
        return flag;
    }

    public static boolean SaveUpdateServico(Activity activity, Domain domain, Servico servico, long id_tipo, String s_descricao, String s_valor, int[] v) {
        boolean flag1, flag2, flag3;
        double  dbl = Utility.getDoubleOfCurrencyForBR(s_valor);
        flag1 = ServicoIsChanged(servico, id_tipo, s_descricao); // Tipo ou Descrição mudaram.
        flag2 = dbl != servico.getValor(); // Valor mudou!
        if (flag1) {
            servico.setDescricao(s_descricao);
            servico.set_tipo(id_tipo);
        }
        if (flag2) {
            servico.setValor(dbl);
        }
        if (flag1 || flag2) {
            if (domain.updateServico(servico) > -1) {
                v[0] = 1;
                flag3 = true;
            } else {         // Erro Alterando Serviço!
                errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_servicos), activity.getResources().getString(R.string.str_err_update));
                v[0] = 0;
                flag3 = false;
            }
        } else {
            flag3 = true;
        }
        return flag3;
    }

    public static boolean DeleteServico(Activity activity, Domain domain, Servico servico, String[] v) {
        boolean flag = false;
        String field, str = "";
        if (domain.deleteServico(servico.get_id()) > -1) {
            flag = true;
        } else {
            str = activity.getResources().getString(R.string.str_servicos);
            str = str.substring(0, str.length() - 1) + " ";
        }
        v[0] = str;
        return flag;
    }

    public static boolean SaveAddEvento(Activity activity, Domain domain, Evento evento, String descricao, long id_contrato, ArrayAdapter<String> ad_servicos) {
        boolean flag = false;
        evento.setDescricao(descricao);
        evento.set_contrato(id_contrato);
        long id_evento = domain.addEvento(evento);
        int v[] = {0};
        if (id_evento > 0) {
            flag = addAllServicos(activity, domain, null, ad_servicos, null, false, id_evento, v);
        } else { // Erro incluindo Evento!
            errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_eventos), activity.getResources().getString(R.string.str_err_insert));
        }
        return flag;
    }

    public static boolean SaveUpdateEvento(Activity activity, Domain domain, Evento evento, ArrayAdapter<Servico> servicos, Pagamento pagamento, String descricao, double total, long id_contrato, ArrayAdapter<String> ad_servicos, Spinner sp_servicos, int[] v) {
        boolean flag;
        if (!evento.getDescricao().equals(descricao)) {
            evento.setDescricao(descricao);
            v[0] = 1;
        }
        if (evento.get_contrato() != id_contrato) {
            evento.set_contrato(id_contrato);
            v[0] = 1;
        }
        if (v[0] == 1) {
            if (domain.updateEvento(evento) > -1) {
                flag = true;
            } else {
                // Erro atualizando Evento!
                errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_eventos), activity.getResources().getString(R.string.str_err_update));
                v[0] = 0;
                return false;
            }
        } else {
            flag = true;
        }
        if (flag) {
            flag = addAllServicos(activity, domain, sp_servicos, ad_servicos, servicos, true, evento.get_id(), v);
        }
        if (flag) {
            flag = removeAllServicos(activity, domain, ad_servicos, servicos, evento.get_id(), v);
        }
        if (flag) {
            flag = updatePagamento(activity, domain, pagamento, total, evento.get_id(), v);
        }
        return flag;
    }

    public static boolean DeleteEvento(Activity activity, Domain domain, long id, String[] v) {
        boolean flag = false;
        String field, str = "";
        if (domain.deleteEvento(id) > -1) {
            flag = true;
        } else {
            str = activity.getResources().getString(R.string.str_eventos);
            str = str.substring(0, str.length() -1)  + " ";
        }
        if (str.isEmpty()) {
            if (domain.deleteEventoServico(id) > -1) {
                flag = true;
            } else {
                field = activity.getResources().getString(R.string.str_servicos);
                field = field.substring(0, field.length() - 1);
                str = field;
            }
        }
        if (str.isEmpty()) {
            if (domain.deleteEventoPagamento(id) > -1) {
                flag = true;
            } else {
                field = activity.getResources().getString(R.string.str_telefone);
                field = field.substring(0, field.length() -1) + " ";
                str = field;
            }
        }
        v[0] = str;
        return flag;
    }
}
