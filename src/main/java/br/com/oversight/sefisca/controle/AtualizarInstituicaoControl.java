package br.com.oversight.sefisca.controle;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.oversight.sefisca.persistencia.InstituicaoDao;

@Scope("conversation")
@Controller("AtualizarInstituicaoControl")
public class AtualizarInstituicaoControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private InstituicaoDao instituicaoDao;

    public void atualizarInstituicaoCsv() throws Exception {
        instituicaoDao.atualizarInstituicaoCsv();
    }
}
