package br.com.oversight.sefisca.template;

import java.util.HashMap;

import org.apache.commons.lang.StringEscapeUtils;

import br.com.oversight.sefisca.entidade.Usuario;
import br.com.oversight.sefisca.util.Constantes;
import br.com.oversight.sefisca.util.StringXorCriptografia;

public class TemplateUsuario {

    public static String gerarHtmlConfirmacao(Usuario usuario) throws Exception {
        String nomeUsuario = usuario.getPessoa().getNome();
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("nome_sistema", Constantes.Sistema.NOME);
        parametros.put("url_sistema", Constantes.Sistema.URL_SISTEMA);
        parametros.put("logo_sistema", Constantes.Sistema.URL_SISTEMA + "/resources/imagens/logo_sefisca.png");
        parametros.put("x", new StringXorCriptografia().encode(usuario.getPessoa().getCpf(), StringXorCriptografia.getChave()));

        parametros.put("titulo", StringEscapeUtils.escapeHtml("Confirmação de Cadastro"));
        parametros.put("ola_usuario", StringEscapeUtils.escapeHtml("Olá, " + nomeUsuario + ","
                + " seu cadastro foi realizado, agora é só confirmar seu e-mail e acessar o sistema!"));
        parametros.put("clique_aqui", StringEscapeUtils.escapeHtml("Clique aqui e confirme seu cadastro"));
        parametros.put("link_quebrado", StringEscapeUtils
                .escapeHtml("Caso o link não funcione," + " copie e cole a linha abaixo no seu navegador:"));
        parametros.put("nao_solicitou",
                StringEscapeUtils.escapeHtml("Você não solicitou o cadastro? Então ignore este e-mail."));
        parametros.put("duvidas",
                StringEscapeUtils.escapeHtml("Em caso de dúvidas, estamos à disposição atráves do telefone:"));
        parametros.put("telefones", "(62) 6262-6262 || (63) 6363-6363");

        return TemplateLoader.getText("template-usuario-confirmacao", parametros);
    }

    public static String gerarHtmlRecuperacaoSenha(Usuario usuario) throws Exception {
        String nomeUsuario = usuario.getPessoa().getNome();
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("nome_sistema", Constantes.Sistema.NOME);
        parametros.put("url_sistema", Constantes.Sistema.URL_SISTEMA);
        parametros.put("logo_sistema", Constantes.Sistema.URL_SISTEMA + "/resources/imagens/logo_sefisca.png");
        parametros.put("token", usuario.getToken());

        parametros.put("titulo", StringEscapeUtils.escapeHtml("Redefinição de Senha"));
        parametros.put("ola_usuario", StringEscapeUtils.escapeHtml(
                "Olá, " + nomeUsuario + "," + " atráves do link abaixo você conseguirá redefinir sua senha!"));
        parametros.put("clique_aqui", StringEscapeUtils.escapeHtml("Clique aqui e redefina sua senha"));
        parametros.put("link_quebrado", StringEscapeUtils
                .escapeHtml("Caso o link não funcione," + " copie e cole a linha abaixo no seu navegador:"));
        parametros.put("nao_solicitou", StringEscapeUtils
                .escapeHtml("Você não solicitou a alteração de sua senha?" + " Então ignore este e-mail."));
        parametros.put("duvidas",
                StringEscapeUtils.escapeHtml("Em caso de dúvidas, estamos à disposição atráves dos telefones:"));
        parametros.put("telefones", "(62) 6262-6262 || (63) 6363-6363");

        return TemplateLoader.getText("template-usuario-recuperacao-senha", parametros);
    }
}
