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
import br.com.ambientinformatica.util.UtilHash;
import br.com.ambientinformatica.util.UtilHash.Algoritimo;
import br.com.oversight.sefisca.controle.dto.ViaCEPDTO;
import br.com.oversight.sefisca.entidade.DadosProfissionais;
import br.com.oversight.sefisca.entidade.EnumEstadoCivil;
import br.com.oversight.sefisca.entidade.EnumSexo;
import br.com.oversight.sefisca.entidade.EnumUf;
import br.com.oversight.sefisca.entidade.Municipio;
import br.com.oversight.sefisca.entidade.Usuario;
import br.com.oversight.sefisca.persistencia.CargoDao;
import br.com.oversight.sefisca.persistencia.DadosProfissionaisDao;
import br.com.oversight.sefisca.persistencia.MunicipioDao;
import br.com.oversight.sefisca.persistencia.UsuarioDao;
import br.com.oversight.sefisca.services.CepService;
import br.com.oversight.sefisca.util.UtilMessages;
import lombok.Getter;
import lombok.Setter;

@Scope("conversation")
@Controller("AtualizarPerfilControl")
public class AtualizarPerfilControl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private UsuarioDao usuarioDao;

	@Autowired
	private MunicipioDao municipioDao;;

	@Autowired
	private UsuarioLogadoControl usuarioLogadoControl;

	@Autowired
	private CepService cepService;

	@Autowired
	private CargoDao cargoDao;

	@Autowired
	private DadosProfissionaisDao dadosProfissionaisDao;

	@Getter
	@Setter
	private Usuario usuario;

	@Getter
	@Setter
	private String novaSenha;

	@Getter
	@Setter
	private String novaSenhaConfirm;

	@Getter
	@Setter
	private String senhaAtual;

	@Getter
	@Setter
	private EnumUf uf;

	@Getter
	@Setter
	private DadosProfissionais dadosProfissionais;

	@Getter
	private List<Municipio> municipios = new ArrayList<>();

	@PostConstruct
	public void init() {
		verificarUsuarioLogado();
	}

	public void iniciarDadosProfissionais() {
		dadosProfissionais = dadosProfissionaisDao.consultarDadosPorPessoa(usuario.getPessoa());
	}

	public String verificarUsuarioLogado() {
		try {
			this.usuario = usuarioLogadoControl.getUsuario();
			if (this.usuario != null) {
				if (this.usuario.getPessoa().getEndereco().getMunicipio() != null) {
					this.uf = this.usuario.getPessoa().getEndereco().getMunicipio().getUf();
				}
			}
			listarMunicipiosPorUfs();
			iniciarDadosProfissionais();
		} catch (Exception e) {
			UtilMessages.addMessage(e);
		}
		return "/atualizarPerfil?faces-redirect=true";
	}

	public void listarMunicipiosPorUfs() {
		try {
			municipios = municipioDao.listarPorUfNome(this.uf, null);
		} catch (Exception e) {
			UtilMessages.addMessage(e);
		}
	}

	public void confirmar() {
		try {
			this.usuario = usuarioDao.alterar(this.usuario);
			this.dadosProfissionais.setPessoa(usuario.getPessoa());
			if (this.dadosProfissionais != null && this.dadosProfissionais.getCargo() != null)
				this.dadosProfissionaisDao.alterar(this.dadosProfissionais);
			UtilMessages.addMessage("Cadastro salvo com sucesso!");
		} catch (Exception e) {
			UtilMessages.addMessage(e);
		}
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
					UtilMessages.addMessage(FacesMessage.SEVERITY_WARN, "CEP não encontrado.");
					UtilMessages.addMessage(FacesMessage.SEVERITY_WARN,
							"Caso seja um endereço novo preencha todos os campos.");
				}
			} catch (Exception e) {
				UtilMessages.addMessage(e);
			}
		}
	}

	public void alterarSenha() {
		try {
			String senhaAtualCripto = UtilHash.gerarStringHash(senhaAtual, Algoritimo.MD5);
			if (senhaAtualCripto.equals(this.usuario.getSenha())) {
				if (novaSenha != null && novaSenha.equals(novaSenhaConfirm)) {
					this.usuario.setSenhaNaoCriptografada(novaSenha);
					this.usuario.setAlterarSenha(false);
					this.usuario = usuarioDao.alterar(this.usuario);
					UtilMessages.addMessage("Senha alterada com sucesso!");
				} else {
					UtilMessages.addMessage(FacesMessage.SEVERITY_WARN, "Senhas diferentes!");
				}
			} else {
				UtilFaces.addMensagemFaces("Senha atual incorreta!", FacesMessage.SEVERITY_ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			UtilMessages.addMessage(e);
		} finally {
			this.setSenhaAtual("");
			this.setNovaSenha("");
			this.setNovaSenhaConfirm("");
		}
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
