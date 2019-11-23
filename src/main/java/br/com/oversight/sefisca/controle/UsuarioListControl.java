package br.com.oversight.sefisca.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.ambientinformatica.ambientjsf.util.UtilFaces;
import br.com.oversight.sefisca.entidade.EnumPapel;
import br.com.oversight.sefisca.entidade.Usuario;
import br.com.oversight.sefisca.persistencia.UsuarioDao;
import lombok.Getter;
import lombok.Setter;

@Scope("conversation")
@Controller("UsuarioListControl")
public class UsuarioListControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private UsuarioDao usuarioDao;

    @Getter
    @Setter
    private EnumPapel papel;

    @Getter
    private List<Usuario> usuarios = new ArrayList<>();

    @PostConstruct
    public void init() {
        listar();
    }

    public void listar() {
        try {
            this.usuarios = usuarioDao.listar();
        } catch (Exception e) {
            UtilFaces.addMensagemFaces(e);
        }
    }
}
