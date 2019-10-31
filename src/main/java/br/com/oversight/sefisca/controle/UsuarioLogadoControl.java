package br.com.oversight.sefisca.controle;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.ambientinformatica.ambientjsf.util.UtilFaces;
import br.com.oversight.sefisca.entidade.Usuario;
import br.com.oversight.sefisca.persistencia.UsuarioDao;


@Scope("session")
@Controller("UsuarioLogadoControl")
public class UsuarioLogadoControl implements Serializable {

   private static final long serialVersionUID = 1L;

   private Usuario usuario;

   @Autowired
   private UsuarioDao usuarioDao;

   @PostConstruct
   public void init() {
      try {
         consultarUsuarioLogado();
      } catch (Exception e) {
         UtilFaces.addMensagemFaces(e);
      }
   }

   public void atualizaSessao() {
      HttpServletRequest req = UtilFaces.getRequest();
      HttpSession session = req.getSession();
      String windowNameToSet = (String) session.getAttribute("windowNameToSet");
      if(windowNameToSet != null && !windowNameToSet.isEmpty()){
         session.removeAttribute("windowNameToSet");    
      }
   }

   private void consultarUsuarioLogado(){
      try {
         HttpServletRequest req = UtilFaces.getRequest();
         usuario = usuarioDao.consultarPorCpfSemValidacao(req.getUserPrincipal().getName());
      } catch(Exception e){
         UtilFaces.addMensagemFaces(e);
      }
   }

   public Usuario getUsuario() {
      return usuario;
   }

   public void setUsuario(Usuario usuario) {
      this.usuario = usuario;
   }

}
