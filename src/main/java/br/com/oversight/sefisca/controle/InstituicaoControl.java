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
import br.com.oversight.sefisca.entidade.Atividade;
import br.com.oversight.sefisca.entidade.EnumTipoPessoa;
import br.com.oversight.sefisca.entidade.EnumUf;
import br.com.oversight.sefisca.entidade.Instituicao;
import br.com.oversight.sefisca.entidade.MotivoDesativacao;
import br.com.oversight.sefisca.entidade.Municipio;
import br.com.oversight.sefisca.entidade.NaturezaJuridica;
import br.com.oversight.sefisca.entidade.TipoInstituicao;
import br.com.oversight.sefisca.entidade.TipoUnidade;
import br.com.oversight.sefisca.entidade.TurnoAtendimento;
import br.com.oversight.sefisca.persistencia.AtividadeDao;
import br.com.oversight.sefisca.persistencia.InstituicaoDao;
import br.com.oversight.sefisca.persistencia.MotivoDesativacaoDao;
import br.com.oversight.sefisca.persistencia.MunicipioDao;
import br.com.oversight.sefisca.persistencia.NaturezaJuridicaDao;
import br.com.oversight.sefisca.persistencia.TipoInstituicaoDao;
import br.com.oversight.sefisca.persistencia.TipoUnidadeDao;
import br.com.oversight.sefisca.persistencia.TurnoAtendimentoDao;
import br.com.oversight.sefisca.services.CepService;
import br.com.oversight.sefisca.util.UtilMessages;
import lombok.Getter;
import lombok.Setter;

@Scope("conversation")
@Controller("InstituicaoControl")
public class InstituicaoControl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private CepService cepService;

	@Autowired
	private InstituicaoDao instituicaoDao;

	@Autowired
	private MunicipioDao municipioDao;

	@Autowired
	private TipoUnidadeDao tipoUnidadeDao;

	@Autowired
	private TurnoAtendimentoDao turnoAtendimentoDao;

	@Autowired
	private NaturezaJuridicaDao naturezaJuridicaDao;

	@Autowired
	private TipoInstituicaoDao tipoInstituicaoDao;

	@Autowired
	private AtividadeDao atividadeDao;

	@Autowired
	private MotivoDesativacaoDao motivoDesativacaoDao;

	@Getter
	private Instituicao instituicao;

	@Getter
	@Setter
	private EnumUf uf;

	@Getter
	private List<Municipio> municipios = new ArrayList<>();

	@PostConstruct
	private void init() {
		novaInstituicao();
	}

	private void novaInstituicao() {
		this.instituicao = new Instituicao();
		this.instituicao.setTipoPessoa(EnumTipoPessoa.PJ);
	}

	public void confirmar() {
		try {
			this.instituicaoDao.alterar(instituicao);
			UtilMessages.addMessage("Registro inserido com sucesso");
		} catch (Exception e) {
			e.printStackTrace();
			UtilMessages.addMessage(e);
		}
	}

	public void consultarCep() {
		if (this.instituicao.getEndereco().getCep() != null) {
			try {
				ViaCEPDTO viaCEPDTO = cepService.consultarCep(this.instituicao.getEndereco().getCep());

				if (viaCEPDTO != null) {
					this.instituicao.getEndereco().setLogradouro(viaCEPDTO.getLogradouro());
					this.instituicao.getEndereco().setMunicipio(viaCEPDTO.getMunicipio());
					this.uf = this.instituicao.getEndereco().getMunicipio().getUf();
					listarMunicipiosPorUfs();
				} else {
					this.instituicao.getEndereco().setLogradouro(null);
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

	public void listarMunicipiosPorUfs() {
		try {
			municipios = municipioDao.listarPorUfNome(this.uf, null);
		} catch (Exception e) {
			UtilMessages.addMessage(e);
		}
	}

	public void setInstituicao(Instituicao instituicao) {
		this.instituicao = instituicaoDao.consultar(instituicao.getId());
		if (this.instituicao != null && this.instituicao.getEndereco().getMunicipio() != null) {
			this.uf = this.instituicao.getEndereco().getMunicipio().getUf();
		} else {
			preencherMunicipio();
		}
		listarMunicipiosPorUfs();
	}

	public void preencherMunicipio() {
		ViaCEPDTO viaCEPDTO = cepService.consultarCep(this.instituicao.getEndereco().getCep());
		this.instituicao.getEndereco().setMunicipio(viaCEPDTO.getMunicipio());
		this.uf = viaCEPDTO.getMunicipio().getUf();
		this.instituicao = instituicaoDao.alterar(this.instituicao);
	}

	public List<SelectItem> getUfs() {
		return UtilFaces.getListEnum(EnumUf.values());
	}

	public List<SelectItem> getTipoPessoa() {
		return UtilFaces.getListEnum(EnumTipoPessoa.values());
	}

	public boolean isPessoaFisica() {
		return this.instituicao.getTipoPessoa() == EnumTipoPessoa.PF;
	}

	public List<TipoUnidade> getTiposUnidade() {
		return this.tipoUnidadeDao.listar();
	}

	public List<TurnoAtendimento> getTurnosAtendimento() {
		return this.turnoAtendimentoDao.listar();
	}

	public List<NaturezaJuridica> getNaturezasJuridica() {
		return this.naturezaJuridicaDao.listar();
	}

	public List<TipoInstituicao> getTiposInstituicao() {
		return this.tipoInstituicaoDao.listar();
	}

	public List<Atividade> getAtividades() {
		return this.atividadeDao.listar();
	}

	public List<MotivoDesativacao> getMotivosDesativacao() {
		return this.motivoDesativacaoDao.listar();
	}
}
