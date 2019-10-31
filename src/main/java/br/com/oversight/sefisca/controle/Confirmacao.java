package br.com.oversight.sefisca.controle;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.ambientinformatica.ambientjsf.util.UtilFaces;
import br.com.oversight.sefisca.entidade.Usuario;
import br.com.oversight.sefisca.persistencia.UsuarioDao;

@Scope("request")
@Controller("Confirmacao")
public class Confirmacao implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;

    @Autowired
    private UsuarioDao usuarioDao;

    @PostConstruct
    public void init(){
        try {
            consultarUsuario();
        } catch (Exception e) {
            UtilFaces.addMensagemFaces(e);
        }
    }

    private void consultarUsuario(){
        try {
            String cpf = (String) UtilFaces.getRequest().getSession().getAttribute("_cpf");
            setUsuario(usuarioDao.consultarPorCpf(cpf));
        } catch (Exception e) {
            UtilFaces.addMensagemFaces(e);
        }
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }


}
