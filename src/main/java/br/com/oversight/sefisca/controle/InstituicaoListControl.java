package br.com.oversight.sefisca.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.oversight.sefisca.entidade.Instituicao;
import br.com.oversight.sefisca.persistencia.InstituicaoDao;
import br.com.oversight.sefisca.util.UtilMessages;
import lombok.Getter;

@Scope("conversation")
@Controller("InstituicaoListControl")
public class InstituicaoListControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private InstituicaoDao instituicaoDao;

    @Getter
    private List<Instituicao> instituicaoList = new ArrayList<>();

    @PostConstruct
    public void init() {
        try {
            this.instituicaoList = instituicaoDao.listar();
        } catch (Exception e) {
            e.printStackTrace();
            UtilMessages.addMessage(e);
        }
    }

}
