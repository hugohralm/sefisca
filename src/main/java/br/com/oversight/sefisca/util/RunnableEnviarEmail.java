package br.com.oversight.sefisca.util;

import br.com.ambientinformatica.util.UtilLog;

public class RunnableEnviarEmail implements Runnable {

    private static final String EMAIL_PROPERTIES = "mail.mailSefisca";
    private static final String EMAIL_ORIGEM = "sefiscasistema@gmail.com";
    private static final String SENHA = "sefiscasistema123";
    private Mensagem msg;

    public RunnableEnviarEmail(final Mensagem msg) {
        this.msg = msg;
    }

    public void run() {
        try {
            Mail mail = new Mail();
            msg.setDe(EMAIL_ORIGEM);
            mail.enviarEmail(EMAIL_PROPERTIES, msg, EMAIL_ORIGEM, SENHA);
        } catch (Exception e) {
            UtilLog.getLog().error("Erro ao enviar E-mail de Confirmação. " + e.getMessage(), e);
        }
    }
}