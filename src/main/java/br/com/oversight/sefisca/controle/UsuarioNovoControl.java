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
import br.com.oversight.sefisca.entidade.EnumSexo;
import br.com.oversight.sefisca.entidade.EnumUf;
import br.com.oversight.sefisca.entidade.Municipio;
import br.com.oversight.sefisca.entidade.TermoResponsabilidade;
import br.com.oversight.sefisca.entidade.TermoResponsabilidadeTemplate;
import br.com.oversight.sefisca.entidade.Usuario;
import br.com.oversight.sefisca.persistencia.MunicipioDao;
import br.com.oversight.sefisca.persistencia.TermoResponsabilidadeDao;
import br.com.oversight.sefisca.persistencia.TermoResponsabilidadeTemplateDao;
import br.com.oversight.sefisca.persistencia.UsuarioDao;
import br.com.oversight.sefisca.services.CepService;
import lombok.Getter;
import lombok.Setter;

@Scope("conversation")
@Controller("UsuarioNovoControl")
public class UsuarioNovoControl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Usuario usuario;

	@Getter
	@Setter
	private String confirmarSenha;

	@Getter
	@Setter
	private EnumUf uf;

	@Getter
	private TermoResponsabilidade termoResponsabilidade;

	@Getter
	private List<Municipio> municipios = new ArrayList<>();

	@Autowired
	private UsuarioDao usuarioDao;

	@Autowired
	private MunicipioDao municipioDao;

	@Autowired
	private TermoResponsabilidadeDao termoResponsabilidadeDao;

	@Autowired
	private TermoResponsabilidadeTemplateDao termoResponsabilidadeTemplateDao;

	@Autowired
	private CepService cepService;

	@PostConstruct
	public void init() {
		novoUsuario();
		this.uf = EnumUf.GO;
		listarMunicipiosPorUfs();
	}

	private void consultarTemplateECriarTermoResponsabilidade() {
		try {
			TermoResponsabilidadeTemplate template = termoResponsabilidadeTemplateDao.consultarUltimo();
			this.termoResponsabilidade = new TermoResponsabilidade(template);
		} catch (Exception e) {
			UtilFaces.addMensagemFaces(e);
		}
	}

	public void listarMunicipiosPorUfs() {
		try {
			this.municipios = municipioDao.listarPorUfNome(uf, null);
		} catch (Exception e) {
			UtilFaces.addMensagemFaces(e);
		}
	}

	public void concluir() {
		try {
			usuarioDao.validarUsuario(this.usuario, this.confirmarSenha);
			if (this.termoResponsabilidade == null || !this.termoResponsabilidade.isAceitou()) {
				UtilFaces.addMensagemFaces(
						"Você deve ler e aceitar o termo de responsabilidade antes de concluir o cadastro.",
						FacesMessage.SEVERITY_ERROR);
				return;
			}
			Usuario usuarioCadastrado = usuarioDao.criarNovoUsuario(this.usuario, this.confirmarSenha);
			this.termoResponsabilidade.setUsuario(usuarioCadastrado);
			termoResponsabilidadeDao.incluir(this.termoResponsabilidade);
			novoUsuario();
			UtilFaces.addMensagemFaces("Seu cadastro foi realizado com sucesso. ");
			UtilFaces.addMensagemFaces("Acesse o seu endereço de email para confirmar o cadastro.");
		} catch (Exception e) {
			UtilFaces.addMensagemFaces(e);
		}
	}

	public void novoUsuario() {
		this.usuario = new Usuario();
		consultarTemplateECriarTermoResponsabilidade();
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

	public void consultarCep() {
		if (this.usuario.getPessoaFisica().getEndereco().getCep() != null) {
			try {
				ViaCEPDTO viaCEPDTO = cepService.consultarCep(this.usuario.getPessoaFisica().getEndereco().getCep());

				if (viaCEPDTO != null) {
					this.usuario.getPessoaFisica().getEndereco().setEndereco(viaCEPDTO.getEnderecoCompleto());
					this.usuario.getPessoaFisica().getEndereco().setMunicipio(viaCEPDTO.getMunicipio());
					this.uf = this.usuario.getPessoaFisica().getEndereco().getMunicipio().getUf();
					listarMunicipiosPorUfs();
				} else {
					this.usuario.getPessoaFisica().setEndereco(null);
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
}
