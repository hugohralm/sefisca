package br.com.oversight.sefisca.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.ambientinformatica.ambientjsf.util.UtilFaces;
import br.com.oversight.sefisca.entidade.Parametro;
import br.com.oversight.sefisca.persistencia.ParametroDao;

@Scope("conversation")
@Controller("ParametroControl")
public class ParametroControl implements Serializable {
    
    private static final long serialVersionUID = 1L;

   private Parametro parametro = new Parametro();

   private Parametro parametroExcluir;

   @Autowired
   private ParametroDao parametroDao;

   private List<Parametro> parametros = new ArrayList<>();

   @PostConstruct
   public void init(){
      listar();
   }

   public void confirmar(){
      try {
         parametroDao.alterar(parametro);
         parametro = new Parametro();
         UtilFaces.addMensagemFaces("Salvo com sucesso!");
         listar();
      } catch (Exception e) {
         UtilFaces.addMensagemFaces(e);
      }
   }

   public void listar(){
      try {
         parametros = parametroDao.listar();
      } catch (Exception e) {
         UtilFaces.addMensagemFaces(e);
      }
   }

   public void excluir(){
      try {
         parametroDao.excluirPorId(parametroExcluir.getId());
         listar();
         UtilFaces.addMensagemFaces("Excluído com sucesso!");
      } catch (Exception e) {
         UtilFaces.addMensagemFaces(e);
      }
   }

   public void editarParametro(ActionEvent evt){
      setParametro((Parametro) UtilFaces.getValorParametro(evt, "parametro"));
   }

   public Parametro getParametro() {
      return parametro;
   }

   public void setParametro(Parametro parametro) {
      this.parametro = parametro;
   }

   public List<Parametro> getParametros() {
      return parametros;
   }

   public Parametro getParametroExcluir() {
      return parametroExcluir;
   }

   public void setParametroExcluir(Parametro parametroExcluir) {
      this.parametroExcluir = parametroExcluir;
   }

}
