package br.com.oversight.sefisca.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.oversight.sefisca.entidade.Processo;
import br.com.oversight.sefisca.persistencia.ProcessoDao;
import lombok.Getter;

@Scope("conversation")
@Controller("InicioControl")
public class InicioControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
	private ProcessoDao processoDao;
    
    @Getter
    private List<Processo> processos = new ArrayList<>();
    
    @PostConstruct
	public void init() {
		this.processos = processoDao.listar();
	}
}