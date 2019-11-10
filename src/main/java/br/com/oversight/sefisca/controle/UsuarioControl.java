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
import br.com.oversight.sefisca.entidade.EnumEstadoCivil;
import br.com.oversight.sefisca.entidade.EnumSexo;
import br.com.oversight.sefisca.entidade.EnumUf;
import br.com.oversight.sefisca.entidade.Municipio;
import br.com.oversight.sefisca.entidade.Usuario;
import br.com.oversight.sefisca.persistencia.MunicipioDao;
import br.com.oversight.sefisca.persistencia.UsuarioDao;
import br.com.oversight.sefisca.services.CepService;
import lombok.Getter;
import lombok.Setter;

@Scope("conversation")
@Controller("UsuarioControl")
public class UsuarioControl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private UsuarioDao usuarioDao;

	@Autowired
	private MunicipioDao municipioDao;;

	@Autowired
	private UsuarioLogadoControl usuarioLogadoControl;

	@Autowired
	private CepService cepService;

	@Getter
	@Setter
	private Usuario usuario;

	@Getter
	@Setter
	private String senha1;

	@Getter
	@Setter
	private String senha2;

	@Getter
	@Setter
	private String confirmarSenha;

	@Getter
	@Setter
	private EnumUf uf;

	@Getter
	private List<Municipio> municipios = new ArrayList<>();

	@PostConstruct
	public void init() {
		verificarUsuarioLogado();
	}

	public String verificarUsuarioLogado() {
		try {
			this.usuario = usuarioLogadoControl.getUsuario();
			if (this.usuario != null) {
				if (this.usuario.getPessoa().getEndereco().getMunicipio() != null) {
					this.uf = this.usuario.getPessoa().getEndereco().getMunicipio().getUf();
				} else {
					this.uf = EnumUf.GO;
				}
			}
			listarMunicipiosPorUfs();
		} catch (Exception e) {
			UtilFaces.addMensagemFaces(e);
		}
		return "/atualizarDados?faces-redirect=true";
	}

	public void listarMunicipiosPorUfs() {
		try {
			municipios = municipioDao.listarPorUfNome(this.uf, null);
		} catch (Exception e) {
			UtilFaces.addMensagemFaces(e);
		}
	}

	public void confirmar() {
		try {
			this.usuario = usuarioDao.alterar(this.usuario);
			UtilFaces.addMensagemFaces("Cadastro salvo com sucesso!");
		} catch (Exception e) {
			UtilFaces.addMensagemFaces(e);
		}
	}

	public void alterarSenha() {
		try {
			String senhaAtualCripto = UtilHash.gerarStringHash(confirmarSenha, Algoritimo.MD5);
			if (senhaAtualCripto.equals(this.usuario.getSenha())) {
				if (senha1 != null && senha1.equals(senha2)) {
					this.usuario.setSenhaNaoCriptografada(senha1);
					this.usuario.setAlterarSenha(false);
					this.usuario = usuarioDao.alterar(this.usuario);
					UtilFaces.addMensagemFaces("Senha alterada com sucesso!");
				} else {
					UtilFaces.addMensagemFaces("Senhas diferentes!", FacesMessage.SEVERITY_ERROR);
				}
			} else {
				UtilFaces.addMensagemFaces("Senha atual incorreta!", FacesMessage.SEVERITY_ERROR);
			}
		} catch (Exception e) {
			UtilFaces.addMensagemFaces(e);
		}finally {
			this.setSenha1("");
			this.setSenha2("");
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
					this.usuario.getPessoa().setEndereco(null);
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
