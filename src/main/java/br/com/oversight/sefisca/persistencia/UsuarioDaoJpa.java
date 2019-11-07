package br.com.oversight.sefisca.persistencia;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.ambientinformatica.jpa.exception.ValidacaoException;
import br.com.ambientinformatica.jpa.persistencia.PersistenciaJpa;
import br.com.ambientinformatica.util.UtilCpfCnpj;
import br.com.ambientinformatica.util.UtilLog;
import br.com.oversight.sefisca.entidade.EnumPapel;
import br.com.oversight.sefisca.entidade.Usuario;
import br.com.oversight.sefisca.template.TemplateUsuario;
import br.com.oversight.sefisca.util.Constantes;
import br.com.oversight.sefisca.util.Mensagem;
import br.com.oversight.sefisca.util.ThreadEnviarEmail;
import br.com.oversight.sefisca.util.UtilSefisca;

@Repository("usuarioDao")
public class UsuarioDaoJpa extends PersistenciaJpa<Usuario> implements UsuarioDao {

	private static final long serialVersionUID = 1L;

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public Usuario criarNovoUsuario(Usuario usuario, String confirmarSenha) throws PersistenciaException {
		validarUsuario(usuario, confirmarSenha);
		try {
			Usuario usuarioBanco = consultarPorCpf(usuario.getPessoaFisica().getCpf());
			if (usuarioBanco != null) {
				if (!usuarioBanco.isConfirmado()) {
					usuarioBanco.setSenhaNaoCriptografada(usuario.getSenha());
					usuarioBanco.setPessoaFisica(usuario.getPessoaFisica());
					usuario = alterar(usuarioBanco);
				}
			} else {
				usuario.setSenhaNaoCriptografada(usuario.getSenha());
				incluir(usuario);
			}
			enviarEmailConfirmacaoCadastro(usuario);
			return usuario;
		} catch (Exception e) {
			UtilLog.getLog().error(e.getMessage(), e);
			throw new PersistenciaException("Erro ao salvar usuário", e);
		}
	}

	@Override
	public void recuperarSenha(String cpf, String email) throws PersistenciaException {
		Usuario usuarioEmail;
		try {
			usuarioEmail = this.consultarPorCpf(cpf);
		} catch (Exception e) {
			UtilLog.getLog().error(e.getMessage(), e);
			throw new PersistenciaException(e);
		}

		if (usuarioEmail != null) {
			if (!usuarioEmail.getPessoaFisica().getEmail().equals(email)) {
				String[] aEmail = usuarioEmail.getPessoaFisica().getEmail().split("@");
				String emailT = aEmail[0].substring(0, aEmail[0].length() - aEmail[0].length() / 2) + "*****@"
						+ aEmail[1];
				throw new ValidacaoException(String
						.format("O e-mail informado está diferente do e-email cadastrado para este cpf. (%s)", emailT));
			}

			usuarioEmail.gerarToken();
			usuarioEmail.setAlterarSenha(true);

			try {
				super.alterar(usuarioEmail);
				enviarEmailRecuperacaoSenha(usuarioEmail);
			} catch (Exception e) {
				UtilLog.getLog().error(e.getMessage(), e);
				throw new PersistenciaException("Erro ao Recuperar Senha do Usuário.", e);
			}
		} else {
			throw new ValidacaoException("Não encontramos nenhum usuário com este número de CPF");
		}
	}

	private void enviarEmailRecuperacaoSenha(Usuario usuario) throws PersistenciaException {
		try {
			Mensagem mensagem = new Mensagem();
			mensagem.setPara(usuario.getPessoaFisica().getEmail());
			mensagem.setConteudo(TemplateUsuario.gerarHtmlRecuperacaoSenha(usuario));
			mensagem.setAssunto(Constantes.Sistema.NOME + " - Recuperação de senha");
			ThreadEnviarEmail.enviarEmail(mensagem);
		} catch (Exception e) {
			UtilLog.getLog().error(e.getMessage(), e);
			throw new PersistenciaException("Erro ao enviar email de recuperação de senha.", e);
		}
	}

	@Override
	public void validarUsuario(Usuario usuario, String confirmarSenha) throws PersistenciaException {
		this.validar(usuario);
		validarSenha(usuario, confirmarSenha);

		if (UtilSefisca.validarMenorIdade16(usuario.getPessoaFisica().getDataNascimento())) {
			throw new ValidacaoException("Data de nascimento inválida!");
		}

		Usuario pesEmail = consultarPorEmail(usuario.getPessoaFisica().getEmail());
		if (pesEmail != null && !pesEmail.getPessoaFisica().getCpf().equals(usuario.getPessoaFisica().getCpf())
				&& pesEmail.getPessoaFisica().getEmail().equals(usuario.getPessoaFisica().getEmail())) {
			throw new ValidacaoException("Já existe um usuário cadastrado com este E-mail! Utilize outro e-mail.");
		}

		Usuario pes = consultarPorCpf(usuario.getPessoaFisica().getCpf());
		if (pes != null && pes.getPessoaFisica().getCpf() != null
				&& pes.getPessoaFisica().getCpf().equals(usuario.getPessoaFisica().getCpf()) && pes.isConfirmado()) {
			throw new ValidacaoException(
					"Já existe um usuário cadastrado com este CPF. Tente a opção de Recuperar Senha da página inicial.");
		}
	}

	@Override
	public void validarSenha(Usuario usuario, String senha) throws ValidacaoException {
		if (!usuario.getSenha().equals(senha)) {
			throw new ValidacaoException("Senhas divergêntes!");
		}
		if (usuario.getSenha().length() < 3) {
			throw new ValidacaoException("A senha devara conter pelo menos 3 caracteres.");
		}
	}

	@Override
	public Usuario consultarPorCpfSemValidacao(String cpf) throws PersistenciaException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select distinct u from Usuario u ");
			sql.append("left join fetch u.pessoaFisica ps ");
			sql.append("left join fetch ps.endereco e ");
			sql.append("where ps.cpf =:cpf ");

			TypedQuery<Usuario> query = em.createQuery(sql.toString(), Usuario.class);
			query.setParameter("cpf", cpf);
			return query.getSingleResult();
		} catch (NoResultException ne) {
			return null;
		} catch (Exception e) {
			UtilLog.getLog().error(e.getMessage(), e);
			throw new PersistenciaException("Erro ao consultar usuário por CPF.", e);
		}
	}

	private void enviarEmailConfirmacaoCadastro(Usuario usuarioEmail) throws PersistenciaException {
		try {
			Mensagem mensagem = new Mensagem();
			mensagem.setPara(usuarioEmail.getPessoaFisica().getEmail());
			mensagem.setConteudo(TemplateUsuario.gerarHtmlConfirmacao(usuarioEmail));
			mensagem.setAssunto(Constantes.Sistema.NOME + " - Cadastro de usuário");
			ThreadEnviarEmail.enviarEmail(mensagem);
		} catch (Exception e) {
			UtilLog.getLog().error(e.getMessage(), e);
			throw new PersistenciaException("Erro ao enviar email de confirmação de cadastro.", e);
		}
	}

	@Override
	public Usuario consultarPorEmail(String email) throws PersistenciaException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select distinct u from Usuario u ");
			sql.append("left join fetch u.pessoaFisica ps ");
			sql.append("where ps.email =:email ");

			TypedQuery<Usuario> query = em.createQuery(sql.toString(), Usuario.class);
			query.setParameter("email", email);

			List<Usuario> usuarios = query.getResultList();

			if (usuarios.isEmpty()) {
				return null;
			} else {
				return usuarios.get(0);
			}
		} catch (Exception e) {
			UtilLog.getLog().error(e.getMessage(), e);
			throw new PersistenciaException("Erro ao consultar usuário por e-mail.", e);
		}
	}

	@Override
	public Usuario consultarPorCpf(String cpf) throws PersistenciaException {
		validarCpf(cpf);
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select distinct u from Usuario u ");
			sql.append("left join fetch u.pessoaFisica ps ");
			sql.append("where ps.cpf =:cpf ");

			TypedQuery<Usuario> query = em.createQuery(sql.toString(), Usuario.class);
			query.setParameter("cpf", cpf);
			query.setMaxResults(1);
			return query.getSingleResult();

		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			UtilLog.getLog().error(e.getMessage(), e);
			throw new PersistenciaException("Erro ao consultar usuário por CPF.", e);
		}
	}

	private static void validarCpf(String cpf) throws ValidacaoException {
		if (!UtilCpfCnpj.validar(cpf) | UtilSefisca.isCPFComDigitosIguais(cpf)) {
			throw new ValidacaoException("CPF do usuário inválido!");
		}
	}

	@Override
	public List<Usuario> listarPorCpfEmailNomeMenosAdmin(String cpf, String nome, String email, boolean admin,
			EnumPapel papelFiltro) throws PersistenciaException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select distinct u from Usuario u ");
			sql.append("left join fetch u.papeisUsuario p ");
			sql.append("left join fetch u.pessoaFisica ps ");
			sql.append("where 1=1 ");

			if (!admin) {
				sql.append("and ps.cpf != '111.111.111-11' ");
			}
			if (cpf != null && !cpf.isEmpty()) {
				sql.append("and ps.cpf =:cpf ");
			}
			if (nome != null && !nome.isEmpty()) {
				sql.append("and upper(ps.nome) like upper(:nome) ");
			}
			if (email != null && !email.isEmpty()) {
				sql.append("and upper(ps.email) like upper(:email) ");
			}
			if (papelFiltro != null) {
				sql.append("and p.papel =:papelFiltro ");
			}

			TypedQuery<Usuario> query = em.createQuery(sql.toString(), Usuario.class);

			if (cpf != null && !cpf.isEmpty()) {
				query.setParameter("cpf", cpf);
			}
			if (nome != null && !nome.isEmpty()) {
				query.setParameter("nome", "%" + nome + "%");
			}
			if (email != null && !email.isEmpty()) {
				query.setParameter("email", "%" + email + "%");
			}
			if (papelFiltro != null) {
				query.setParameter("papelFiltro", papelFiltro);
			}

			return query.getResultList();

		} catch (Exception e) {
			UtilLog.getLog().error(e.getMessage(), e);
			throw new PersistenciaException("Erro ao listar usuários", e);
		}
	}

	@Override
	public Usuario consultarPorToken(String token) throws PersistenciaException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select distinct u from Usuario u ");
			sql.append("where u.token =:token ");

			TypedQuery<Usuario> query = em.createQuery(sql.toString(), Usuario.class);
			query.setParameter("token", token);
			List<Usuario> usuarios = query.getResultList();

			if (usuarios.isEmpty()) {
				return null;
			} else {
				return usuarios.get(0);
			}
		} catch (Exception e) {
			UtilLog.getLog().error(e.getMessage(), e);
			throw new PersistenciaException("Erro ao consultar usuário por token", e);
		}
	}

	@Override
	public Usuario consultarPorId(Integer id) throws PersistenciaException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select distinct u from Usuario u ");
			sql.append("left join fetch u.papeisUsuario p ");
			sql.append("left join fetch u.pessoaFisica ps ");
			sql.append("left join fetch ps.endereco e ");
			sql.append("where u.id =:id");

			TypedQuery<Usuario> query = em.createQuery(sql.toString(), Usuario.class);
			query.setParameter("id", id);
			
			return query.getSingleResult();

		} catch (Exception e) {
			UtilLog.getLog().error(e.getMessage(), e);
			throw new PersistenciaException("Erro ao consultar usuário por id", e);
		}
	}

}