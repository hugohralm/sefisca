package br.com.oversight.sefisca.util;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ambientinformatica.util.UtilLog;
import br.com.oversight.sefisca.entidade.Endereco;
import br.com.oversight.sefisca.entidade.EnumEstadoCivil;
import br.com.oversight.sefisca.entidade.EnumPapel;
import br.com.oversight.sefisca.entidade.EnumSexo;
import br.com.oversight.sefisca.entidade.PapelUsuario;
import br.com.oversight.sefisca.entidade.PessoaFisica;
import br.com.oversight.sefisca.entidade.Usuario;
import br.com.oversight.sefisca.persistencia.MunicipioDao;
import br.com.oversight.sefisca.persistencia.UsuarioDao;

@Service("inicializadorSistema")
public class InicializadorSistema {

	@Autowired
	private UsuarioDao usuarioDao;

	@Autowired
	private MunicipioDao municipioDao;

	@PostConstruct
	public void iniciar() {
		inicializarUsuarioAdmin();
	}

	private void inicializarUsuarioAdmin() {
		try {
			Usuario usuario = usuarioDao.consultarPorCpfSemValidacao("111.111.111-11");
			if (usuario == null) {
				usuario = new Usuario();
				usuario.setSenhaNaoCriptografada("123456");
				usuario.addPapel(new PapelUsuario(EnumPapel.ADMIN));
				usuario.setConfirmado(true);
				usuario.setAlterarSenha(false);
				usuario.setPessoaFisica(new PessoaFisica("Administrador", "111.111.111-11", "121212", EnumSexo.M, "admin@admin",
						new Date(), EnumEstadoCivil.SOLTEIRO, "(62)99999-9999", "(62)99999-9999",
						new Endereco("Rua", municipioDao.municipioPorCodigoIBGE(5208707), "74000000", "Bairro", "0")));

				usuarioDao.incluir(usuario);

				UtilLog.getLog().info("*** USUÁRIO admin CRIADO com a senha 123456 ***");
			}
		} catch (Exception e) {
			UtilLog.getLog().error(e.getMessage(), e);
		}
	}

}