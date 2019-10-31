package br.com.oversight.sefisca.persistencia;

import java.util.List;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.ambientinformatica.jpa.exception.ValidacaoException;
import br.com.ambientinformatica.jpa.persistencia.Persistencia;
import br.com.oversight.sefisca.entidade.EnumPapel;
import br.com.oversight.sefisca.entidade.Usuario;

public interface UsuarioDao extends Persistencia<Usuario> {

    Usuario criarNovoUsuario(Usuario usuario, String confirmarSenha) throws PersistenciaException;

    void validarSenha(Usuario usuario, String senha) throws ValidacaoException;

    void validarUsuario(Usuario usuario, String confirmarSenha) throws PersistenciaException;

    Usuario consultarPorCpf(String cpf) throws PersistenciaException;

    public void recuperarSenha(String cpf, String email) throws PersistenciaException;

    List<Usuario> listarPorCpfEmailNomeMenosAdmin(String cpf, String nome, String email, boolean admin,
            EnumPapel papelFiltro) throws PersistenciaException;

    Usuario consultarPorEmail(String email) throws PersistenciaException;

    Usuario consultarPorToken(String token) throws PersistenciaException;

    Usuario consultarPorCpfSemValidacao(String cpf) throws PersistenciaException;
}
