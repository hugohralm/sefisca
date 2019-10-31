package br.com.oversight.sefisca.controle;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.ambientinformatica.ambientjsf.util.UtilFaces;
import br.com.oversight.sefisca.persistencia.UsuarioDao;

@Scope("request")
@Controller("RecuperarSenhaControl")
public class RecuperarSenhaControl implements Serializable {
    
    private static final long serialVersionUID = 1L;

   private String email;

   private String cpf;

   @Autowired
   private UsuarioDao usuarioDao;

   public void recuperarSenha(){
      try{
         usuarioDao.recuperarSenha(cpf, email);
         UtilFaces.addMensagemFaces("Enviamos um link de redefinição de senha para o seu email " + email);
         this.email = "";
         this.cpf= "";
      } catch (Exception e) {
         UtilFaces.addMensagemFaces(e);
      }
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getCpf() {
      return cpf;
   }

   public void setCpf(String cpf) {
      this.cpf = cpf;
   }

}
