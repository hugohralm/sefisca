package br.com.oversight.sefisca.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.ambientinformatica.ambientjsf.util.UtilFaces;
import br.com.ambientinformatica.ambientjsf.util.UtilFacesRelatorio;
import br.com.oversight.sefisca.entidade.EnumUf;
import br.com.oversight.sefisca.entidade.Municipio;
import br.com.oversight.sefisca.persistencia.MunicipioDao;

@Scope("conversation")
@Controller("MunicipioListControl")
public class MunicipioListControl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private MunicipioDao municipioDao;

	private String descricaoMunicipio;

	private EnumUf uf;

	private List<Municipio> municipios = new ArrayList<>();

	@PostConstruct
	private void init() {
		this.uf = EnumUf.GO;
	}

	public void listar() {
		try {
			this.municipios = municipioDao.listarPorUfNome(this.uf, this.descricaoMunicipio);
		} catch (Exception e) {
			UtilFaces.addMensagemFaces(e);
		}
	}

	public void imprimirMunicipio() {
		try {
			UtilFacesRelatorio.gerarRelatorioFaces("jasper/municipios.jasper", this.municipios,
					new HashMap<String, Object>());
		} catch (Exception e) {
			UtilFaces.addMensagemFaces(e);
		}
	}

	public List<Municipio> getMunicipios() {
		return municipios;
	}

	public String getDescricaoMunicipio() {
		return descricaoMunicipio;
	}

	public void setDescricaoMunicipio(String descricaoMunicipio) {
		this.descricaoMunicipio = descricaoMunicipio;
	}

}
