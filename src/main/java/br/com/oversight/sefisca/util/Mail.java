package br.com.oversight.sefisca.util;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import br.com.ambientinformatica.util.UtilException;
import br.com.ambientinformatica.util.UtilLog;
import br.com.ambientinformatica.util.UtilRecurso;


/**
 * Classe que envia email
 * 
 */
public class Mail {

    private static final String MENSAGEM_ERRO = "Erro ao enviar email...";

    /**
     * Envia um email
     * 
     * @param pacoteProp nome do pacote acrescido do nome do arquivo
     *  em que encontram as propriedades (sem a extensao .properties)
     * @param mensagem
     * @param login
     * @param senha
     * @throws UtilException 
     * @throws GeneralSecurityException 
     * @throws IOException
     */
    public void enviarEmail(String pacoteProp, Mensagem mensagem,String login,String senha) throws UtilException{
        Properties prop = UtilRecurso.getProperties(pacoteProp);
        enviarEmail(prop, mensagem, login,senha);
    }

    public void enviarEmail(Properties prop, Mensagem mensagem, String login, String senha) {
        final String log = login;
        final String pass = senha;
        Session session = Session.getDefaultInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(log, pass);
            }
        });
        session.setDebug(false);
        MimeMessage msg = new MimeMessage(session);

        try {
            msg.setFrom(new InternetAddress(mensagem.getDe()));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mensagem.getPara()));

            if (mensagem.getComCopiaOcultaPara() != null) {
                msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(mensagem.getComCopiaOcultaPara()));
            }

            msg.setSentDate(new Date());
            msg.setSubject(mensagem.getAssunto(), "UTF-8");

            if (mensagem.getResponderPara() != null) {
                msg.setReplyTo(new Address[] { new InternetAddress(mensagem.getResponderPara()) });
            }

            MimeMultipart mp = new MimeMultipart();
            mp.setSubType("related");
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setContent(mensagem.getConteudo(), "text/html; charset=UTF-8");
            mp.addBodyPart(mbp1);

            if (mensagem.getAnexos() != null && !mensagem.getAnexos().isEmpty()) {
                for (File caminho : mensagem.getAnexos()) {
                    MimeBodyPart mbp2 = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(caminho);
                    mbp2.setFileName(fds.getName());
                    mbp2.setDataHandler(new DataHandler(fds));
                    mbp2.setHeader("Content-ID", "anexo");
                    mp.addBodyPart(mbp2);
                }
            }
            msg.setContent(mp);
            Transport.send(msg);
        } catch (AddressException e) {
            UtilLog.getLog().error(MENSAGEM_ERRO, e);
        } catch (MessagingException e) {
            UtilLog.getLog().error(MENSAGEM_ERRO, e);
        }
    }

    /**
     * Validar emails - boolean 
     * @param email
     * @return true
     */
    public boolean validarEmail(String email){
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");  
        //Match the given string with the pattern  
        Matcher m = p.matcher(email);  
        //check whether match is found   
        return m.matches();  
    }
}