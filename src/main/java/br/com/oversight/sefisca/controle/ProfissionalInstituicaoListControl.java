package br.com.oversight.sefisca.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.oversight.sefisca.entidade.Instituicao;
import br.com.oversight.sefisca.entidade.Profissional;
import br.com.oversight.sefisca.persistencia.InstituicaoDao;
import lombok.Getter;
import lombok.Setter;

@Scope("conversation")
@Controller("ProfissionalInstituicaoListControl")
public class ProfissionalInstituicaoListControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
	private InstituicaoDao instituicaoDao;
    
    @Getter @Setter
    private Instituicao instituicao;
    
    @Getter
    private List<Profissional> profissionais = new ArrayList<>();
    
    @PostConstruct
	public void init() {
	}
    
    public void buscarProfissionais() throws Exception {
		Set<Profissional> profissionais = instituicaoDao.profissionaisServicePorCnes(this.instituicao.getCnes());
		this.profissionais.addAll(profissionais);
	}
}