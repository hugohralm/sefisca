package br.com.oversight.sefisca.controle;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.oversight.sefisca.persistencia.Instituicao2Dao;

@Scope("conversation")
@Controller("AtualizarBancoDeDadosControl")
public class AtualizarBancoDeDadosControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private Instituicao2Dao instituicao2Dao;

    public void atualizarInstituicaoCsv() throws Exception {
        instituicao2Dao.atualizarInstituicaoCsv();
    }
}
