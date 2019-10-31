package br.com.oversight.sefisca.controle;

import java.io.Serializable;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.ambientinformatica.ambientjsf.util.UtilFaces;
import br.com.oversight.sefisca.entidade.Usuario;
import br.com.oversight.sefisca.persistencia.UsuarioDao;

@Scope("conversation")
@Controller("AlterarSenhaExternoControl")
public class AlterarSenhaExternoControl implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;

    private String confirmarSenha;

    private String senha1;

    private String senha2;

    private boolean alterou = false;

    private String token;

    @Autowired
    private UsuarioDao usuarioDao;

    public String getAtualizar(){
        if(this.token==null){
            this.token = UtilFaces.getRequest().getParameter("token");
        }else{
            setAlterou(false);
        }
        try {
            this.usuario = usuarioDao.consultarPorToken(this.token);
            if (this.usuario == null && token != null && !alterou) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("erroToken.ovs");
            }
        } catch (Exception e) {
            UtilFaces.addMensagemFaces(e);
        }
        return "";
    }

    public String alterarSenha() {
        try {
            usuario.setSenha(senha1);
            usuarioDao.validarSenha(usuario, senha2);
            usuario.setSenhaNaoCriptografada(senha1);
            usuario.setAlterarSenha(false);
            usuario.setToken(null);
            usuarioDao.alterar(usuario);
            setToken(null);
            setAlterou(true);
            usuario = null;
            UtilFaces.addMensagemFaces("Senha alterada com sucesso!");
        } catch (Exception e) {
            UtilFaces.addMensagemFaces(e);
        }
        return "";
    }

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }

    public String getSenha1() {
        return senha1;
    }

    public void setSenha1(String senha1) {
        this.senha1 = senha1;
    }

    public String getSenha2() {
        return senha2;
    }

    public void setSenha2(String senha2) {
        this.senha2 = senha2;
    }

    public boolean isAlterou() {
        return alterou;
    }

    public void setAlterou(boolean alterou) {
        this.alterou = alterou;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
