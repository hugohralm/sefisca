package br.com.oversight.sefisca.controle;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.ambientinformatica.ambientjsf.util.UtilFaces;
import br.com.oversight.sefisca.entidade.ModeloDocumento;
import br.com.oversight.sefisca.persistencia.ModeloDocumentoDao;
import br.com.oversight.sefisca.util.UtilMessages;
import lombok.Getter;
import lombok.Setter;

@Controller("ModeloDocumentoListControl")
public class ModeloDocumentoListControl {

    @Autowired
    private ModeloDocumentoDao modeloDocumentoDao;

    @Getter
    @Setter
    private String descricao;

    @Getter
    @Setter
    private ModeloDocumento modeloDocumentoExcluir;

    @Getter
    @Setter
    private List<ModeloDocumento> modelosDeDocumentos = new ArrayList<>();

    public void listar() {
        try {
            this.modelosDeDocumentos = modeloDocumentoDao.listarPorDescricao(this.descricao);
            if(this.modelosDeDocumentos.isEmpty()) UtilMessages.addMessage("Não foram encontrados registros");
        } catch (Exception e) {
            UtilMessages.addMessage(e);
        }
    }

    public void excluir() {
        try {
            modeloDocumentoDao.excluirPorId(this.modeloDocumentoExcluir.getId());
            listar();
            UtilFaces.addMensagemFaces("Excluído com sucesso!");
        } catch (Exception e) {
            UtilMessages.addMessage(e);
        }
    }
}
