package br.com.oversight.sefisca.controle;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.ambientinformatica.ambientjsf.util.UtilFaces;
import br.com.oversight.sefisca.entidade.Municipio;
import br.com.oversight.sefisca.persistencia.MunicipioDao;

@Controller("MunicipioControl")
@Scope("conversation")
public class MunicipioControl implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Autowired
    private MunicipioDao municipioDao;

    private Municipio municipio = new Municipio();

    public void confirmar() {
        try {
            municipioDao.alterar(municipio);
            UtilFaces.addMensagemFaces("Salvo com sucesso!");
        } catch (Exception e) {
            UtilFaces.addMensagemFaces(e);
        }
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

}
