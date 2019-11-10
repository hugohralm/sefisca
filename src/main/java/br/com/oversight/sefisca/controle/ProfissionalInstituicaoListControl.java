package br.com.oversight.sefisca.controle;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.oversight.sefisca.entidade.Instituicao;
import br.com.oversight.sefisca.persistencia.InstituicaoDao;
import lombok.Getter;

@Scope("conversation")
@Controller("ProfissionalInstituicaoListControl")
public class ProfissionalInstituicaoListControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
	private InstituicaoDao instituicaoDao;
    
    @Getter
    private Instituicao instituicao;
    
    @PostConstruct
	public void init() {
		this.instituicao = instituicaoDao.consultar(Integer.parseInt("21"));
	}
}