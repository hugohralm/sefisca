package br.com.oversight.sefisca.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.ambientinformatica.ambientjsf.util.UtilFaces;
import br.com.oversight.sefisca.controle.dto.ViaCEPDTO;
import br.com.oversight.sefisca.entidade.EnumEstadoCivil;
import br.com.oversight.sefisca.entidade.EnumPapel;
import br.com.oversight.sefisca.entidade.EnumSexo;
import br.com.oversight.sefisca.entidade.EnumUf;
import br.com.oversight.sefisca.entidade.Municipio;
import br.com.oversight.sefisca.entidade.PapelUsuario;
import br.com.oversight.sefisca.entidade.Usuario;
import br.com.oversight.sefisca.persistencia.MunicipioDao;
import br.com.oversight.sefisca.persistencia.UsuarioDao;
import br.com.oversight.sefisca.services.CepService;
import lombok.Getter;
import lombok.Setter;

@Scope("conversation")
@Controller("UsuarioListControl")
public class UsuarioListControl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private UsuarioDao usuarioDao;

	@Autowired
	private MunicipioDao municipioDao;

	@Autowired
	private UsuarioLogadoControl usuarioLogadoControl;

	@Autowired
	private CepService cepService;

	@Getter
	private Usuario usuario;

	@Getter
	@Setter
	private EnumPapel papel;

	@Getter
	@Setter
	private EnumPapel papelFiltro;

	@Getter
	@Setter
	private String cpf;

	@Getter
	@Setter
	private String nome;

	@Getter
	@Setter
	private String email;

	@Getter
	@Setter
	private EnumUf uf;

	@Getter
	private List<Usuario> usuarios = new ArrayList<>();

	@Getter
	private List<Municipio> municipios = new ArrayList<>();

	@PostConstruct
	public void init() {
			listar();
	}

	public void confirmar() {
		try {
			this.usuario = usuarioDao.alterar(this.usuario);
			UtilFaces.addMensagemFaces("Usuário salvo com sucesso!");
		} catch (Exception e) {
			UtilFaces.addMensagemFaces(e);
		}
	}

	public void listar() {
		try {
			this.usuarios = usuarioDao.listarPorCpfEmailNomeMenosAdmin(cpf, nome, email,
					usuarioLogadoControl.getUsuario().isContemPapel(EnumPapel.ADMIN), papelFiltro);
		} catch (Exception e) {
			UtilFaces.addMensagemFaces(e);
		}
	}

	public void adicionarPapel() {
		try {
			if (papel.equals(EnumPapel.GERENTE) && !usuarioLogadoControl.getUsuario().isContemPapel(EnumPapel.ADMIN)) {
				UtilFaces.addMensagemFaces("Somente usuários Administradores podem adicionar esse papel.",
						FacesMessage.SEVERITY_ERROR);
				return;
			}
			this.usuario.addPapel(new PapelUsuario(papel));
		} catch (Exception e) {
			UtilFaces.addMensagemFaces(e);
		}
	}

	public void removerPapel(PapelUsuario papelUsuarioParam) {
		try {
			this.usuario.removerPapel(papelUsuarioParam);
			UtilFaces.addMensagemFaces("Papel removido!");
		} catch (Exception e) {
			UtilFaces.addMensagemFaces(e);
		}
	}

	public void listarMunicipiosPorUfs() {
		try {
			municipios = municipioDao.listarPorUfNome(uf, null);
		} catch (Exception e) {
			UtilFaces.addMensagemFaces(e);
		}
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuarioDao.consultarPorId(usuario.getId());

		if (this.usuario != null && this.usuario.getPessoa().getEndereco().getMunicipio() != null) {
			this.uf = this.usuario.getPessoa().getEndereco().getMunicipio().getUf();
		} else {
			try {
				this.uf = EnumUf.GO;
			} catch (Exception e) {
				UtilFaces.addMensagemFaces("Erro ao consultar UF");
			}
		}
		listarMunicipiosPorUfs();
	}

	public void consultarCep() {
		if (this.usuario.getPessoa().getEndereco().getCep() != null) {
			try {
				ViaCEPDTO viaCEPDTO = cepService.consultarCep(this.usuario.getPessoa().getEndereco().getCep());

				if (viaCEPDTO != null) {
					this.usuario.getPessoa().getEndereco().setEndereco(viaCEPDTO.getEnderecoCompleto());
					this.usuario.getPessoa().getEndereco().setMunicipio(viaCEPDTO.getMunicipio());
					this.uf = this.usuario.getPessoa().getEndereco().getMunicipio().getUf();
					listarMunicipiosPorUfs();
				} else {
					this.usuario.getPessoa().getEndereco().setEndereco(null);
					this.uf = EnumUf.GO;
					listarMunicipiosPorUfs();
					UtilFaces.addMensagemFaces("CEP não encontrado.", FacesMessage.SEVERITY_WARN);
					UtilFaces.addMensagemFaces("Caso seja um endereço novo preencha todos os campos.",
							FacesMessage.SEVERITY_WARN);
				}
			} catch (Exception e) {
				UtilFaces.addMensagemFaces(e);
			}
		}
	}

	public List<SelectItem> getPapeis() {
		List<SelectItem> listaPapeis = new ArrayList<>();
		listaPapeis = UtilFaces.getListEnum(EnumPapel.values());
		listaPapeis.remove(0);
		return listaPapeis;
	}

	public List<SelectItem> getUfs() {
		return UtilFaces.getListEnum(EnumUf.values());
	}

	public List<SelectItem> getEstadosCivis() {
		return UtilFaces.getListEnum(EnumEstadoCivil.values());
	}

	public List<SelectItem> getSexos() {
		return UtilFaces.getListEnum(EnumSexo.valuesVisivel());
	}

}